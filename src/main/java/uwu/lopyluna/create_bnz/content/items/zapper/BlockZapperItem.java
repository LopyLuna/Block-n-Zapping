package uwu.lopyluna.create_bnz.content.items.zapper;

import com.simibubi.create.content.equipment.zapper.PlacementPatterns;
import com.simibubi.create.content.equipment.zapper.ZapperItem;
import com.simibubi.create.foundation.gui.ScreenOpener;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import uwu.lopyluna.create_bnz.content.items.zapper.tools.Brush;
import uwu.lopyluna.create_bnz.content.items.zapper.tools.PlacementOptions;
import uwu.lopyluna.create_bnz.content.items.zapper.tools.TerrainBrushes;
import uwu.lopyluna.create_bnz.registry.BZItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static uwu.lopyluna.create_bnz.registry.ZapperModifiers.*;
import static uwu.lopyluna.create_bnz.registry.ZapperModifiers.GENERATOR;

@ParametersAreNonnullByDefault
public class BlockZapperItem extends ZapperItem {
	final String MODIFIER_SLOTS = "ModifierSlots"; //INT
	static final String BRUSH_PARAMS = "BrushParams"; //BLOCK POS
	static final String BRUSH = "Brush"; //ENUM
	static final String TOOL = "Tool"; //ENUM
	static final String PLACEMENT = "Placement"; //ENUM

	public BlockZapperItem(Properties properties) {
		super(properties);
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
	public boolean activate(Level world, Player player, ItemStack stack, BlockState stateToUse,
		BlockHitResult raytrace, CompoundTag data) {

		BlockPos targetPos = raytrace.getBlockPos();
		List<BlockPos> affectedPositions = new ArrayList<>();

		CompoundTag tag = stack.getOrCreateTag();
		Brush brush = NBTHelper.readEnum(tag, BRUSH, TerrainBrushes.class)
			.get();
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
			if (!nbt.contains(MODIFIER_SLOTS)) nbt.putInt(MODIFIER_SLOTS, 0);

			//Modifiers
			CANISTER.update(nbt);
			BODY.update(nbt);
			AMPLIFIER.update(nbt);
			ACCELERATOR.update(nbt); //
			RETRIEVER.update(nbt);
			SCOPE.update(nbt); //
			REINFORCER.update(nbt); //
			//Upgrades
			STASIS.update(nbt);
			APPLICATOR.update(nbt);
			GENERATOR.update(nbt);
		}
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
		return 24 * (SCOPE.getLevel(nbt) + 1);
	}

	@Override
	public int getCooldownDelay(ItemStack pStack) {
		CompoundTag nbt = pStack.getOrCreateTag();
		return switch (ACCELERATOR.getLevel(nbt)) {
			case 1 -> 480;
			case 2 -> 360;
			case 3 -> 240;
			case 4 -> 120;
			default -> 600;
		};
	}
}
