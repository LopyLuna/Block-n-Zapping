package uwu.lopyluna.create_bnz.content.items.zapper;

import com.google.common.base.Predicates;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.CreateClient;
import com.simibubi.create.content.equipment.zapper.PlacementPatterns;
import com.simibubi.create.content.equipment.zapper.ShootableGadgetItemMethods;
import com.simibubi.create.content.equipment.zapper.ZapperBeamPacket;
import com.simibubi.create.content.equipment.zapper.ZapperItem;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.utility.BlockHelper;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;

import io.github.fabricators_of_create.porting_lib.item.DamageableItem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_bnz.content.items.zapper.tools.*;
import uwu.lopyluna.create_bnz.content.modifiers.ModifierTier;
import uwu.lopyluna.create_bnz.content.modifiers.Modifiers;
import uwu.lopyluna.create_bnz.registry.BZItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static com.simibubi.create.content.equipment.zapper.PlacementPatterns.Solid;
import static com.simibubi.create.foundation.item.TooltipHelper.styleFromColor;
import static net.minecraft.client.gui.screens.Screen.hasControlDown;
import static net.minecraft.client.gui.screens.Screen.hasShiftDown;
import static uwu.lopyluna.create_bnz.BZUtils.holdCtrl;
import static uwu.lopyluna.create_bnz.content.items.zapper.TerrainTools.calculateItemsInInventory;
import static uwu.lopyluna.create_bnz.content.modifiers.Modifiers.*;

@ParametersAreNonnullByDefault
public class BlockZapperItem extends ZapperItem implements DamageableItem, Vanishable {
	public static List<Modifiers> MODIFIERS = new ArrayList<>();
	public static List<ModifierTier> TIERS = new ArrayList<>();
	static final String APPLIED_MODIFIER = "AppliedModifiers";
	static final String APPLIED_UPGRADES = "AppliedUpgrades";
	static final String MAX_SLOTS = "MaxSlots"; //INT
	static final String BRUSH_PARAMS = "BrushParams"; //BLOCK POS
	static final String BRUSH = "Brush"; //ENUM
	static final String TOOL = "Tool"; //ENUM
	static final String PLACEMENT = "Placement"; //ENUM

	public BlockZapperItem(Properties properties) {
		super(properties.defaultDurability(1536));
	}

	@Override
	public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
		var nbt = stack.getTag();
		assert nbt != null;
		boolean jeiDisplay = nbt.contains("jeiView");
		if (!jeiDisplay) tooltip.add(holdCtrl());
		if (!hasShiftDown() || jeiDisplay) {
			if (hasControlDown() || jeiDisplay) {
				int slots = 0;
				int maxSlots = getMaxModifierSlots(nbt);
				for (int j = 0; j < maxSlots; j++) {
					Modifiers modifiers = NBTHelper.readEnum(nbt, APPLIED_MODIFIER + j, Modifiers.class);
					if (modifiers != EMPTY) {
						int tier = modifiers.getLevel(nbt);
						tooltip.add(modifiers.getName().append(tier != 0 ? " " + tier + "/" + modifiers.maxLevel + " Lvl" : "").withStyle(styleFromColor(modifiers.getTierFromModifier(nbt).color.getRGB())));
						tooltip.add(modifiers.getDescription().withStyle(ChatFormatting.DARK_GRAY));
						slots++;
					}
				}
				if (!jeiDisplay) {
					for (int j = 0; j < 3; j++) {
						Modifiers modifiers = NBTHelper.readEnum(nbt, APPLIED_UPGRADES + j, Modifiers.class);
						if (modifiers != null && modifiers != EMPTY) {
                            tooltip.add(modifiers.getName().withStyle(styleFromColor(modifiers.getTierFromModifier(nbt).color.getRGB())));
							tooltip.add(modifiers.getDescription().withStyle(ChatFormatting.DARK_GRAY));
						}
					}

					if (slots == 0) tooltip.add(Component.translatable("create_bnz.handheld_block_zapper.no_modifiers").withStyle(ChatFormatting.GRAY));
					tooltip.add(Component.empty()
							.append(Component.translatable("create_bnz.handheld_block_zapper.modifiers").append(" ").withStyle(ChatFormatting.DARK_GRAY))
							.append(Component.literal(slots + "").withStyle(ChatFormatting.GOLD))
							.append(Component.literal("/").withStyle(ChatFormatting.GRAY))
							.append(Component.literal(maxSlots + ".").withStyle(ChatFormatting.DARK_GRAY))
					);
				}
			}
		}
		if (!hasShiftDown() && !hasControlDown() && !jeiDisplay) {
			int amount = nbt.getInt("Amount");
			int size = nbt.getInt("Size");
			size = size==999999 ? 0 : size;

			if (stack.hasTag() && stack.getTag().contains("BlockUsed")) {
				MutableComponent usedBlock = NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), stack.getTag().getCompound("BlockUsed")).getBlock().getName();
				var color = amount >= size ? ChatFormatting.GOLD : ChatFormatting.RED;
				tooltip.add(Component.empty()
						.append(Lang.translateDirect("terrainzapper.usingBlock", usedBlock.withStyle(ChatFormatting.GRAY)).withStyle(ChatFormatting.DARK_GRAY))
						.append(Component.empty().append(" : ").withStyle(ChatFormatting.DARK_GRAY))
						.append(Component.literal(amount + "").withStyle(color))
						.append(Component.literal("/").withStyle(ChatFormatting.GRAY))
						.append(Component.literal(size + ".").withStyle(ChatFormatting.DARK_GRAY))
				);
			}


		}
	}

	@Override
	@Environment(value = EnvType.CLIENT)
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
	protected boolean activate(Level world, Player player, ItemStack item, BlockState stateToUse, BlockHitResult raytrace, CompoundTag data) {
		return false;
	}

	public float activate(Level world, Player player, ItemStack stack, BlockState stateToUse, BlockHitResult raytrace, CompoundTag data, BlockZapperItem zapperItem, InteractionHand hand, int amount) {
		BlockPos targetPos = raytrace.getBlockPos();
		List<BlockPos> affectedPositions = new ArrayList<>();

		CompoundTag tag = stack.getOrCreateTag();
		Brush brush = NBTHelper.readEnum(tag, BRUSH, TerrainBrushes.class).get();
		BlockPos params = fixSize(NbtUtils.readBlockPos(tag.getCompound(BRUSH_PARAMS)), brush, stack);
		float multiplier = sizeMultiplier(params, brush, stack);

		PlacementOptions option = NBTHelper.readEnum(tag, PLACEMENT, PlacementOptions.class);
		TerrainTools tool = NBTHelper.readEnum(tag, TOOL, TerrainTools.class);
		brush.set(params.getX(), params.getY(), params.getZ());
		targetPos = targetPos.offset(brush.getOffset(player.getLookAngle(), raytrace.getDirection(), option));
		brush.addToGlobalPositions(world, targetPos, raytrace.getDirection(), affectedPositions, tool);
		brush.redirectTool(tool).run(world, affectedPositions, stateToUse, data, player, stack, zapperItem, hand, applyPattern(affectedPositions, stack));
		return multiplier;
	}
	public int activateCalculation(Level world, Player player, ItemStack stack, BlockState stateToUse, BlockHitResult raytrace, BlockZapperItem zapperItem) {
		BlockPos targetPos = raytrace.getBlockPos();
		List<BlockPos> affectedPositions = new ArrayList<>();

		CompoundTag tag = stack.getOrCreateTag();
		Brush brush = NBTHelper.readEnum(tag, BRUSH, TerrainBrushes.class).get();
		BlockPos params = fixSize(NbtUtils.readBlockPos(tag.getCompound(BRUSH_PARAMS)), brush, stack);
		PlacementOptions option = NBTHelper.readEnum(tag, PLACEMENT, PlacementOptions.class);
		TerrainTools tool = NBTHelper.readEnum(tag, TOOL, TerrainTools.class);

		brush.set(params.getX(), params.getY(), params.getZ());
		targetPos = targetPos.offset(brush.getOffset(player.getLookAngle(), raytrace.getDirection(), option));
		brush.addToGlobalPositions(world, targetPos, raytrace.getDirection(), affectedPositions, tool);
		return brush.redirectTool(tool).runCalculate(world, affectedPositions, stateToUse, stack, zapperItem);
	}

	@SuppressWarnings("all")
	public static PlacementPatterns applyPattern(List<BlockPos> blocksIn, ItemStack stack) {
		CompoundTag tag = stack.getTag();
		PlacementPatterns pattern = !tag.contains("Pattern") ? Solid : PlacementPatterns.valueOf(tag.getString("Pattern"));
		Predicate<BlockPos> filter = Predicates.alwaysFalse();

		switch (pattern) {
            case Checkered:
				filter = pos -> (pos.getX() + pos.getY() + pos.getZ()) % 2 == 0;
				break;
			case InverseCheckered:
				filter = pos -> (pos.getX() + pos.getY() + pos.getZ()) % 2 != 0;
				break;
			default:
				break;
		}

		blocksIn.removeIf(filter);
		return pattern;
	}

	public static void configureSettings(ItemStack stack, PlacementPatterns pattern, TerrainBrushes brush, int brushParamX, int brushParamY, int brushParamZ, TerrainTools tool, PlacementOptions placement) {
		ZapperItem.configureSettings(stack, pattern);
		CompoundTag nbt = stack.getOrCreateTag();
		NBTHelper.writeEnum(nbt, BRUSH, brush);
		nbt.put(BRUSH_PARAMS, NbtUtils.writeBlockPos(new BlockPos(brushParamX, brushParamY, brushParamZ)));
		NBTHelper.writeEnum(nbt, TOOL, tool);
		NBTHelper.writeEnum(nbt, PLACEMENT, placement);
	}

	@SuppressWarnings("all")
	@Override
	public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
		super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
		CompoundTag nbt = pStack.getOrCreateTag();
		if (BZItems.BLOCK_ZAPPER.isIn(pStack) && pIsSelected) {

			MODIFIERS.forEach(modifier -> modifier.update(nbt));
			int max = getMaxModifierSlots(nbt);
			if (!nbt.contains(MAX_SLOTS) || (nbt.getInt(MAX_SLOTS) != max)) nbt.putInt(MAX_SLOTS, max);
		}
		if (pEntity instanceof Player player) {
			BlockState stateToUse = Blocks.AIR.defaultBlockState();
			if (nbt.contains("BlockUsed")) stateToUse = NbtUtils.readBlockState(pLevel.holderLookup(Registries.BLOCK), nbt.getCompound("BlockUsed"));
			stateToUse = BlockHelper.setZeroAge(stateToUse);
			Vec3 start = player.position().add(0, player.getEyeHeight(), 0);
			Vec3 range = player.getLookAngle().scale(getZappingRange(pStack));
			BlockHitResult raytrace = pLevel.clip(new ClipContext(start, start.add(range), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));

			int invAmount = calculateItemsInInventory(stateToUse.getBlock(), true, player, nbt.getInt(GENERATOR.baseName) > 0);
			int selSize = activateCalculation(pLevel, player, pStack, stateToUse, raytrace, (BlockZapperItem)pStack.getItem());
			if (!nbt.contains("Amount") || (nbt.getInt("Amount") != invAmount)) nbt.putInt("Amount", invAmount);
			if (!nbt.contains("Size") || (nbt.getInt("Size") != selSize)) nbt.putInt("Size", selSize);
		}
	}

	@Override
	public @NotNull Rarity getRarity(ItemStack pStack) {
		CompoundTag nbt = pStack.getOrCreateTag();
		int echo_slots = 0;
		for (int j = 0; j < 5; j++)
			if (NBTHelper.readEnum(nbt, APPLIED_MODIFIER + j, Modifiers.class).getLevel(nbt) >= ModifierTier.ECHO.level) echo_slots++;
		if (echo_slots == 5) return Rarity.EPIC;
		if (echo_slots > 0) return Rarity.RARE;
		return Rarity.UNCOMMON;
	}


	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack item = player.getItemInHand(hand);

		CompoundTag nbt = item.getOrCreateTag();
		boolean mainHand = hand == InteractionHand.MAIN_HAND;
		BlockState stateToUse = Blocks.AIR.defaultBlockState();
		if (nbt.contains("BlockUsed")) stateToUse = NbtUtils.readBlockState(level.holderLookup(Registries.BLOCK), nbt.getCompound("BlockUsed"));
		stateToUse = BlockHelper.setZeroAge(stateToUse);
		CompoundTag data = nbt.getCompound("BlockData");
		if (nbt.contains("BlockData", Tag.TAG_COMPOUND)) data = nbt.getCompound("BlockData");
		Vec3 start = player.position().add(0, player.getEyeHeight(), 0);
		Vec3 range = player.getLookAngle().scale(getZappingRange(item));
		BlockHitResult raytrace = level.clip(new ClipContext(start, start.add(range), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
		Vec3 barrelPos = ShootableGadgetItemMethods.getGunBarrelVec(player, mainHand, new Vec3(.35f, -0.1f, 1));
		BlockPos lookingPos = raytrace.getBlockPos();

		int amount = nbt.getInt("Amount");
		int size = nbt.getInt("Size");
		boolean items = ((amount >= size || player.isCreative()) && size != 0) || (size == 999999);
		boolean lookingAtBlock = level.getWorldBorder().isWithinBounds(lookingPos) && raytrace.getType() != HitResult.Type.MISS;
		if (level.isClientSide) {
			if (!player.isShiftKeyDown() && (!items || !lookingAtBlock)) {
				//if (size==10001) player.displayClientMessage(Component.translatable("create_bnz.handheld_block_zapper.too_hard").withStyle(ChatFormatting.RED), true)
				//else if (size==10002) player.displayClientMessage(Component.translatable("create_bnz.handheld_block_zapper.failed").withStyle(ChatFormatting.RED), true)
				if (size!=0 && size!=999999 && lookingAtBlock) player.displayClientMessage(Component.translatable("create_bnz.handheld_block_zapper.not_enough_blocks").append(" "+nbt.getInt("Amount")+"/"+nbt.getInt("Size")).withStyle(ChatFormatting.RED), true);
				AllSoundEvents.DENY.play(level, player, player.blockPosition());
				return new InteractionResultHolder<>(InteractionResult.FAIL, item);
			}
			CreateClient.ZAPPER_RENDER_HANDLER.dontAnimateItem(hand);
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
		} else {
			if (!player.isShiftKeyDown() && (!items || !lookingAtBlock)) {
				player.getCooldowns().addCooldown(item.getItem(), 10);
				return new InteractionResultHolder<>(InteractionResult.FAIL, item);
			} else if (!player.isShiftKeyDown() && items && lookingAtBlock) {
				amount = amount==999999?0:amount;
				float multiplier = activate(level, player, item, stateToUse, raytrace, data, (BlockZapperItem) item.getItem(), hand, amount);
				int cooldown = (int) (multiplier * getCooldownDelay(item));
				ShootableGadgetItemMethods.applyCooldown(player, item, hand, this::isZapper, Math.max(cooldown, 5));
				ShootableGadgetItemMethods.sendPackets(player, b -> new ZapperBeamPacket(barrelPos, raytrace.getLocation(), hand, b));
				return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
			}  else return super.use(level, player, hand);
		}
	}

	public ItemStack setModifierToTier(Modifiers modifier, ModifierTier tier, ItemStack pStack) {
        CompoundTag nbt = pStack.getOrCreateTag();
		String id = modifier.baseName;
		nbt.putInt(id, tier.require_level + 1);
		NBTHelper.writeEnum(nbt, "AppliedModifiers0", modifier);
		return pStack;
	}

	public int applyModifier(Modifiers modifier, ItemStack pStack) {
		if (modifier.isUpgrade) return applyUpgrade(modifier, pStack);
		CompoundTag nbt = pStack.getOrCreateTag();
		String id = modifier.baseName;
		modifier.update(nbt);
		int i = modifier.getLevel(nbt);
		int f = 2;
		for (int j = 0; j < getMaxModifierSlots(nbt); j++) {
			String slot = APPLIED_MODIFIER + j;
			Modifiers modifiers = NBTHelper.readEnum(nbt, slot, Modifiers.class);
			if (modifiers == modifier && i < modifier.maxLevel) {
				if (i+1 == modifier.maxLevel) f = 3;
				nbt.putInt(id, i+1);
				return f;
			} else if (modifiers == modifier) {
				return 0;
			} else if (modifiers == EMPTY) {
				NBTHelper.writeEnum(nbt, slot, modifier);
				nbt.putInt(id, 1);
				return 1;
			}
        }
		return 0;
	}

	public int applyUpgrade(Modifiers modifier, ItemStack pStack) {
		CompoundTag nbt = pStack.getOrCreateTag();
		String id = modifier.baseName;
		int i = modifier.getLevel(nbt);
		int max = modifier.maxLevel;

		for (int j = 0; j < 3; j++) {
			String upgrades = APPLIED_UPGRADES + j;
			Modifiers modifiers = NBTHelper.readEnum(nbt, upgrades, Modifiers.class);
			if (modifiers == null || modifiers == EMPTY) {
				NBTHelper.writeEnum(nbt, upgrades, modifier);
				break;
			}
		}

		int f = 4;
		if (i < max) {
			if (i+1 == max) f = 5;
			nbt.putInt(id, i+1);
			return f;
		}
		return 0;
	}

	public BlockPos fixSize(BlockPos params, Brush brush, ItemStack stack) {
		int size = getMaxSize(stack);
		int radius = getMaxRadius(stack);
		int radiusSize = (int) (radius * 1.25);
		int max = 1;
		if (brush instanceof CuboidBrush) max = size;
		if (brush instanceof SphereBrush) max = radiusSize;
		if (brush instanceof CylinderBrush) max = radius;
		if (brush instanceof DynamicBrush) max = radiusSize;
		int x = Math.min(params.getX(), max);
		int y = Math.min(params.getY(), max);
		int z = Math.min(params.getZ(), max);
		return new BlockPos(x, y, z);
	}

	public float sizeMultiplier(BlockPos params, Brush brush, ItemStack stack) {
		float size = getMaxSize(stack);
		float radius = getMaxRadius(stack);
		float radiusSize = (int) (radius * 1.25);
		float max = 1;
		if (brush instanceof CuboidBrush) max = size;
		if (brush instanceof SphereBrush) max = radiusSize;
		if (brush instanceof CylinderBrush) max = radius;
		if (brush instanceof DynamicBrush) max = radiusSize;
		float x = params.getX();
		float y = params.getY();
		float z = params.getZ();
		return (x*y*z) / (max*max*max);
	}

	public int indexMax(int index, Brush brush, ItemStack stack) {
		int size = getMaxSize(stack);
		int radius = getMaxRadius(stack);
		int radiusSize = (int) (radius * 1.25);
		int max = 1;
		if (brush instanceof CuboidBrush) max = size;
		if (brush instanceof SphereBrush) max = radiusSize;
		if (brush instanceof CylinderBrush) max = radius;
		if (brush instanceof DynamicBrush) max = radiusSize;
		return Math.min(index, max);
	}

	public int getMaxSize(ItemStack pStack) {
		CompoundTag nbt = pStack.getOrCreateTag();
		return switch (AMPLIFIER.getLevel(nbt)) {
			case 1 -> 6;
			case 2 -> 8;
			case 3 -> 12;
			case 4 -> 16;
			default -> 4;
		};
	}
	public int getMaxRadius(ItemStack pStack) {
		CompoundTag nbt = pStack.getOrCreateTag();
		return switch (AMPLIFIER.getLevel(nbt)) {
			case 1 -> 3;
			case 2 -> 4;
			case 3 -> 5;
			case 4 -> 6;
			default -> 2;
		};
	}

	public int getHardnessSupport(ItemStack pStack) {
		CompoundTag nbt = pStack.getOrCreateTag();
		return switch (BODY.getLevel(nbt)) {
			case 1 -> 4;
			case 2 -> 8;
			case 3 -> 25;
			case 4 -> 50;
			default -> 2;
		};
	}

	public int getMaxModifierSlots(CompoundTag nbt) {
		return nbt.getInt(APPLICATOR.baseName) > 0 ? 5 : 3;
	}

	@Override
	public int getMaxDamage(ItemStack pStack) {
		CompoundTag nbt = pStack.getOrCreateTag();
		double level = REINFORCER.getLevel(nbt);
		double multiplier = level==0 ? 1 : (level * 0.5) + level;
		return (int)(1536 * multiplier);
	}

	@Override
	public int getZappingRange(ItemStack pStack) {
		CompoundTag nbt = pStack.getOrCreateTag();
		return 6 * (SCOPE.getLevel(nbt) + 1);
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
