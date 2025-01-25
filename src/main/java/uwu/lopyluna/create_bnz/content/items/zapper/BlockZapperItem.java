package uwu.lopyluna.create_bnz.content.items.zapper;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.zapper.PlacementPatterns;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetItemMethods;
import com.simibubi.create.content.equipment.zapper.ZapperBeamPacket;
import com.simibubi.create.content.equipment.zapper.ZapperItem;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import uwu.lopyluna.create_bnz.content.items.zapper.tools.Brush;
import uwu.lopyluna.create_bnz.content.items.zapper.tools.PlacementOptions;
import uwu.lopyluna.create_bnz.content.items.zapper.tools.TerrainBrushes;
import uwu.lopyluna.create_bnz.content.modifiers.Modifiers;
import uwu.lopyluna.create_bnz.registry.BZItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static com.simibubi.create.AllTags.forgeItemTag;
import static uwu.lopyluna.create_bnz.content.modifiers.Modifiers.*;

@ParametersAreNonnullByDefault
public class BlockZapperItem extends ZapperItem {
	public static List<Modifiers> MODIFIERS = new ArrayList<>();
	static final String APPLIED_MODIFIER = "AppliedModifiers";
	static final String MAX_SLOTS = "MaxSlots"; //INT
	static final String BRUSH_PARAMS = "BrushParams"; //BLOCK POS
	static final String BRUSH = "Brush"; //ENUM
	static final String TOOL = "Tool"; //ENUM
	static final String PLACEMENT = "Placement"; //ENUM

	public BlockZapperItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		var nbt = stack.getTag();
		if (stack.hasTag() && nbt != null) {
			int applicator = nbt.getInt(APPLICATOR.baseName);
			int max = 3 + applicator;
			if (applicator != 0) tooltip.add(Component.empty().append(APPLICATOR.getName()).append(" | ").append(applicator + "/" + APPLICATOR.maxLevel).append(" | Desc: ").append(APPLICATOR.getDescription()));

			for (int j = 0; j < max; j++) {
				String slot = APPLIED_MODIFIER + j;
				Modifiers modifiers = NBTHelper.readEnum(nbt, slot, Modifiers.class);
				if (modifiers != EMPTY) tooltip.add(Component.empty().append("Slot" + j + ": ").append(modifiers.getName()).append(" | "+nbt.getInt(modifiers.baseName)+"/"+modifiers.maxLevel).append(" | Desc: ").append(modifiers.getDescription()));
			}
		}
		super.appendHoverText(stack, worldIn, tooltip, flagIn);
	}

	@Override
	@OnlyIn(value = Dist.CLIENT)
	public void openHandgunGUI(ItemStack item, InteractionHand hand) {
		ScreenOpener.open(new BlockZapperScreen(item, hand));
	}

	@Override
	public Component validateUsage(ItemStack item) {
		if (!item.getOrCreateTag().contains(BRUSH_PARAMS))
			return Lang.translateDirect("terrainzapper.shiftRightClickToSet");
		return super.validateUsage(item);
	}

	@Override
	public boolean canActivateWithoutSelectedBlock(ItemStack stack) {
		CompoundTag tag = stack.getOrCreateTag();
		TerrainTools tool = NBTHelper.readEnum(tag, TOOL, TerrainTools.class);
		return !tool.requiresSelectedBlock();
	}

	@Override
	public boolean activate(Level world, Player player, ItemStack stack, BlockState stateToUse, BlockHitResult raytrace, CompoundTag data) {
		BlockPos targetPos = raytrace.getBlockPos();
		List<BlockPos> affectedPositions = new ArrayList<>();

		CompoundTag tag = stack.getOrCreateTag();
		Brush brush = NBTHelper.readEnum(tag, BRUSH, TerrainBrushes.class).get();
		BlockPos params = NbtUtils.readBlockPos(tag.getCompound(BRUSH_PARAMS));
		PlacementOptions option = NBTHelper.readEnum(tag, PLACEMENT, PlacementOptions.class);
		TerrainTools tool = NBTHelper.readEnum(tag, TOOL, TerrainTools.class);

		brush.set(params.getX(), params.getY(), params.getZ());
		targetPos = targetPos.offset(brush.getOffset(player.getLookAngle(), raytrace.getDirection(), option));
		brush.addToGlobalPositions(world, targetPos, raytrace.getDirection(), affectedPositions, tool);
		PlacementPatterns.applyPattern(affectedPositions, stack);
		brush.redirectTool(tool).run(world, affectedPositions, stateToUse, data, player);
		return true;
	}

	public static void configureSettings(ItemStack stack, PlacementPatterns pattern, TerrainBrushes brush, int brushParamX, int brushParamY, int brushParamZ, TerrainTools tool, PlacementOptions placement) {
		ZapperItem.configureSettings(stack, pattern);
		CompoundTag nbt = stack.getOrCreateTag();
		NBTHelper.writeEnum(nbt, BRUSH, brush);
		nbt.put(BRUSH_PARAMS, NbtUtils.writeBlockPos(new BlockPos(brushParamX, brushParamY, brushParamZ)));
		NBTHelper.writeEnum(nbt, TOOL, tool);
		NBTHelper.writeEnum(nbt, PLACEMENT, placement);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(SimpleCustomRenderer.create(this, new BlockZapperItemRenderer()));
	}

	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
		super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
		if (BZItems.BLOCK_ZAPPER.isIn(pStack) && pIsSelected) {
			CompoundTag nbt = pStack.getOrCreateTag();

			MODIFIERS.forEach(modifier -> modifier.update(nbt));
			int max = 3 + nbt.getInt(APPLICATOR.baseName);
			if (!nbt.contains(MAX_SLOTS) || (nbt.getInt(MAX_SLOTS) != max)) nbt.putInt(MAX_SLOTS, max);
		}
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack item = player.getItemInHand(hand);
		ItemStack itemStackOffhand = player.getItemInHand(InteractionHand.MAIN_HAND == hand ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);

		if (player.isShiftKeyDown() && itemStackOffhand.is(forgeItemTag("ingots"))) {
			var bool = false;
			if (itemStackOffhand.is(Items.BRICK)) bool = applyUpgrade(STASIS, item);
			if (itemStackOffhand.is(Items.NETHER_BRICK)) bool = applyUpgrade(APPLICATOR, item);

			if (itemStackOffhand.is(Items.COPPER_INGOT)) bool = applyModifier(AMPLIFIER, item);
			if (itemStackOffhand.is(Items.IRON_INGOT)) bool = applyModifier(BODY, item);
			if (itemStackOffhand.is(Items.GOLD_INGOT)) bool = applyModifier(ACCELERATOR, item);
			if (itemStackOffhand.is(Items.NETHERITE_INGOT)) bool = applyModifier(REINFORCER, item);
			if (itemStackOffhand.is(AllItems.ANDESITE_ALLOY.asItem())) bool = applyModifier(CANISTER, item);
			if (itemStackOffhand.is(AllItems.ZINC_INGOT.asItem())) bool = applyModifier(RETRIEVER, item);
			if (itemStackOffhand.is(AllItems.BRASS_INGOT.asItem())) bool = applyModifier(SCOPE, item);
			if (bool) {
				AllSoundEvents.WRENCH_REMOVE.play(level, player, player.blockPosition());
				itemStackOffhand.shrink(1);
			} else AllSoundEvents.DENY.play(level, player, player.blockPosition());
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
		}


		CompoundTag nbt = item.getOrCreateTag();
		boolean mainHand = hand == InteractionHand.MAIN_HAND;
		BlockState stateToUse = Blocks.AIR.defaultBlockState();
		if (nbt.contains("BlockUsed")) stateToUse = NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), nbt.getCompound("BlockUsed"));
		stateToUse = BlockHelper.setZeroAge(stateToUse);
		CompoundTag data = null;
		if (AllTags.AllBlockTags.SAFE_NBT.matches(stateToUse) && nbt.contains("BlockData", Tag.TAG_COMPOUND)) data = nbt.getCompound("BlockData");
		Vec3 start = player.position().add(0, player.getEyeHeight(), 0);
		Vec3 range = player.getLookAngle().scale(getZappingRange(item));
		BlockHitResult raytrace = level.clip(new ClipContext(start, start.add(range), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
		Vec3 barrelPos = ShootableGadgetItemMethods.getGunBarrelVec(player, mainHand, new Vec3(.35f, -0.1f, 1));

        if (data != null && activate(level, player, item, stateToUse, raytrace, data)) {
			ShootableGadgetItemMethods.applyCooldown(player, item, hand, this::isZapper, getCooldownDelay(item));
			ShootableGadgetItemMethods.sendPackets(player, b -> new ZapperBeamPacket(barrelPos, raytrace.getLocation(), hand, b));

			return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);

		} else return super.use(level, player, hand);
	}

	public boolean applyModifier(Modifiers modifier, ItemStack pStack) {
		CompoundTag nbt = pStack.getOrCreateTag();
		int max = 3 + nbt.getInt(APPLICATOR.baseName);
		String id = modifier.baseName;
		if (!nbt.contains(id)) nbt.putInt(id, 0);
		int i = nbt.getInt(id);
		for (int j = 0; j < max; j++) {
			String slot = APPLIED_MODIFIER + j;
			Modifiers modifiers = NBTHelper.readEnum(nbt, slot, Modifiers.class);
			if (modifiers == modifier && i < modifier.maxLevel) {
				nbt.putInt(id, i+1);
				return true;
			} else if (modifiers == modifier) {
				return false;
			} else if (modifiers == EMPTY) {
				NBTHelper.writeEnum(nbt, slot, modifier);
				nbt.putInt(id, 1);
				return true;
			}
        }
		return false;
	}

	public boolean applyUpgrade(Modifiers modifier, ItemStack pStack) {
		CompoundTag nbt = pStack.getOrCreateTag();
		String id = modifier.baseName;
		int i = nbt.getInt(id);
		if (i < modifier.maxLevel) {
			nbt.putInt(id, i+1);
			return true;
		}
		return false;
	}

	@Override
	public boolean isDamageable(ItemStack pStack) {
		return true;
	}

	@Override
	public int getMaxDamage(ItemStack pStack) {
		CompoundTag nbt = pStack.getOrCreateTag();
		return 1024 * (REINFORCER.getLevel(nbt) + REINFORCER.getLevel(nbt) + 1);
	}

	@Override
	public int getZappingRange(ItemStack pStack) {
		CompoundTag nbt = pStack.getOrCreateTag();
		return 8 * (SCOPE.getLevel(nbt) + 1);
	}

	@Override
	public int getCooldownDelay(ItemStack pStack) {
		CompoundTag nbt = pStack.getOrCreateTag();
		return switch (ACCELERATOR.getLevel(nbt)) {
			case 1 -> 120;
			case 2 -> 90;
			case 3 -> 60;
			case 4 -> 30;
			case 5 -> 15;
			case 6 -> 8;
			case 7 -> 4;
			case 8 -> 2;
			default -> 150;
		};
	}
}
