package uwu.lopyluna.create_bnz.content.items.zapper;

import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.zapper.PlacementPatterns;
import com.simibubi.create.content.equipment.zapper.ZapperItem;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import uwu.lopyluna.create_bnz.registry.BZTags;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

import static uwu.lopyluna.create_bnz.BZUtils.getCenterPos;
import static uwu.lopyluna.create_bnz.content.modifiers.Modifiers.*;
import static uwu.lopyluna.create_bnz.registry.BZTags.AllItemTags.RENEWABLE_GENERATING;

public enum TerrainTools {

	Fill(AllIcons.I_FILL),
	Place(AllIcons.I_PLACE),
	Replace(AllIcons.I_REPLACE),
	Clear(AllIcons.I_CLEAR),
	Overlay(AllIcons.I_OVERLAY),

	;
	
	public final String translationKey;
	public final AllIcons icon;

	TerrainTools(AllIcons icon) {
		this.translationKey = Lang.asId(name());
		this.icon = icon;
	}

	public boolean requiresSelectedBlock() {
		return this != Clear;
	}

	@SuppressWarnings("all")
	public void run(Level world, List<BlockPos> targetPositions, BlockState paintedState, CompoundTag data,
				   Player player, ItemStack stack, BlockZapperItem zapperItem, InteractionHand hand, PlacementPatterns patterns) {
		int size = targetPositions.size();
		int valid = 0;
		switch (this) {
			case Clear:
				for (var p : targetPositions)
					zapperFunction(world, Blocks.AIR.defaultBlockState(), p, world.getBlockState(p), stack, player, hand, data, patterns);
				break;
			case Fill:
				for (var p : targetPositions) {
                    BlockState toReplace = world.getBlockState(p);
                    if (!isReplaceable(toReplace)) continue;
					zapperFunction(world, paintedState, p, toReplace, stack, player, hand, data, patterns);
                }
                break;
            case Overlay:
                for (var p : targetPositions) {
                    BlockState toOverlay = world.getBlockState(p);
                    if (isReplaceable(toOverlay) || (toOverlay == paintedState)) continue;
                    p = p.above();
                    BlockState toReplace = world.getBlockState(p);
                    if (!isReplaceable(toReplace)) continue;
					zapperFunction(world, paintedState, p, toReplace, stack, player, hand, data, patterns);
                }
                break;
            case Place:
                for (var p : targetPositions)
					zapperFunction(world, paintedState, p, world.getBlockState(p), stack, player, hand, data, patterns);
                break;
            case Replace:
                for (var p : targetPositions) {
                    BlockState toReplace = world.getBlockState(p);
                    if (isReplaceable(toReplace)) continue;
                    zapperFunction(world, paintedState, p, toReplace, stack, player, hand, data, patterns);
                }
                break;
            }
	}

	public int runCalculate(Level world, List<BlockPos> targetPositions, BlockState paintedState,
							ItemStack stack, BlockZapperItem zapperItem) {
		int size = 0;
        switch (this) {
			case Clear:
				return 999999;
			case Place:
				for (var p : targetPositions)
					if (meetRequirements(world, paintedState, p, world.getBlockState(p), stack, zapperItem) == 0)
						size++;
				break;
			case Fill:
				for (var p : targetPositions) {
					BlockState toReplace = world.getBlockState(p);
					if (!isReplaceable(toReplace)) continue;
					if (meetRequirements(world, paintedState, p, toReplace, stack, zapperItem) == 0)
						size++;
				}
				break;
			case Overlay:
				for (var p : targetPositions) {
					BlockState toOverlay = world.getBlockState(p);
					if (isReplaceable(toOverlay) || (toOverlay == paintedState)) continue;
					p = p.above();
					BlockState toReplace = world.getBlockState(p);
					if (!isReplaceable(toReplace)) continue;
					if (meetRequirements(world, paintedState, p, toReplace, stack, zapperItem) == 0)
						size++;
				}
				break;
            case Replace:
				for (var p : targetPositions) {
					BlockState toReplace = world.getBlockState(p);
					if (isReplaceable(toReplace)) continue;
                    if (meetRequirements(world, paintedState, p, toReplace, stack, zapperItem) == 0)
                        size++;
				}
				break;
		}
		return size;
	}

	public static boolean isReplaceable(BlockState toReplace) {
		return toReplace.canBeReplaced();
	}

	public static void zapperFunction(Level pLevel, BlockState paintState, BlockPos replacePos, BlockState replaceState,
									  ItemStack stack, Player player, InteractionHand hand, CompoundTag data, PlacementPatterns patterns) {
		Random r = new Random();
		if (switch (patterns) {
			case Chance25 -> r.nextBoolean() || r.nextBoolean();
			case Chance50 -> r.nextBoolean();
			case Chance75 -> r.nextBoolean() && r.nextBoolean();
			default -> false;
		}) return;
		if (meetRequirements(pLevel, paintState, replacePos, replaceState, stack, (BlockZapperItem) stack.getItem()) != 0) return;
		CompoundTag nbt = stack.getOrCreateTag();
		int retTier = RETRIEVER.getLevel(nbt);
		boolean stasis = STASIS.getLevel(nbt) > 0;
		Block paintBlock = paintState.getBlock();
		boolean creative = player.isCreative();
		boolean hasGenerator = GENERATOR.getLevel(nbt) > 0;
		if (!creative && !paintState.isAir() && !hasItemInInventory(paintBlock, player, hasGenerator)) return;
		if (!creative && !paintState.isAir()) calculateItemsInInventory(paintBlock, false, player, hasGenerator);

		if (retTier > 0) dropResources(replaceState, pLevel, replacePos, replaceState.hasBlockEntity() ? pLevel.getBlockEntity(replacePos) : null, player, stack, retTier);
		paintBlock.setPlacedBy(pLevel, replacePos, paintState, player, stack);
		pLevel.gameEvent(GameEvent.BLOCK_PLACE, replacePos, GameEvent.Context.of(player, paintState));
		if (stasis) pLevel.setBlock(replacePos, paintState, Block.UPDATE_CLIENTS);
		else pLevel.setBlockAndUpdate(replacePos, paintState);
		ZapperItem.setBlockEntityData(pLevel, replacePos, paintState, data, player);
		if (!creative) stack.hurtAndBreak(2, player, b -> b.broadcastBreakEvent(hand));
	}

	public static boolean hasItemInInventory(Block paintBlock, Player player, boolean generator) {
		var inv = player.getInventory();
		int size = inv.getContainerSize();
		if (RENEWABLE_GENERATING.matches(paintBlock.asItem()) && generator) return true;
		else for (int slot = 0; slot < size; slot++) {
			var item = inv.getItem(slot);
			if (item.isEmpty()) continue;
			if (item.is(paintBlock.asItem())) return true;
		}
		return false;
	}

	public static int calculateItemsInInventory(Block paintBlock, boolean calculate, Player player, boolean generator) {
		int amount = 0;
		var inv = player.getInventory();
		int size = inv.getContainerSize();
		if (RENEWABLE_GENERATING.matches(paintBlock.asItem()) && generator) amount = size * 128;
		else for (int slot = 0; slot < size; slot++) {
			var item = inv.getItem(slot);
			if (item.isEmpty())
				continue;
			if (item.is(paintBlock.asItem())) {
				if (!calculate) {	
					item.shrink(1);
					return 1;
				} else amount = amount + item.getCount();
			}
		}
		return amount;
	}

	public static boolean blacklist(BlockState toBlacklist) {
		return toBlacklist.is(BZTags.AllBlockTags.BLOCK_ZAPPER_BLACKLIST.tag) || toBlacklist.is(AllTags.AllBlockTags.NON_MOVABLE.tag) || AllTags.AllBlockTags.SAFE_NBT.matches(toBlacklist);
	}
	public static int meetRequirements(Level pLevel, BlockState paintState, BlockPos replacePos, BlockState replaceState, ItemStack stack, BlockZapperItem zapperItem) {
		if (paintState == replaceState)
			return 8;
		if (replaceState.getBlock().defaultDestroyTime() > zapperItem.getHardnessSupport(stack) && !replaceState.isAir())
			return 6;
		if (blacklist(replaceState))
			return 4;
		if (!paintState.canSurvive(pLevel, replacePos))
			return 4;
        return pLevel.getWorldBorder().isWithinBounds(replacePos) ? 0 : 4;
    }

	public static void dropResources(BlockState pState, Level pLevel, BlockPos pPos, @Nullable BlockEntity pBlockEntity, @Nullable Entity pEntity, ItemStack pTool, int retrieverLevel) {
		if (pLevel instanceof ServerLevel serverLevel) {
			BlockPos dropPos;
            if (retrieverLevel == 4 && pEntity != null) dropPos = pEntity.getOnPos().above();
            else dropPos = pPos;

            Block.getDrops(pState, serverLevel, pPos, pBlockEntity, pEntity, pTool).forEach((p_49944_) -> popResource(pLevel, dropPos, p_49944_, retrieverLevel));
			pState.spawnAfterBreak(serverLevel, pPos, pTool, false);
			int fortuneLevel = pTool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
			int silkTouchLevel = pTool.getEnchantmentLevel(Enchantments.SILK_TOUCH);

			int exp = pState.getExpDrop(serverLevel, serverLevel.random, pPos, fortuneLevel, silkTouchLevel);
			if (exp > 0) pState.getBlock().popExperience(serverLevel, dropPos, exp);
		}
	}

	static int cTick = 0;
	public static void itemTickClient(Level pLevel, Player player) {
		cTick = ++cTick % 2;
		if (cTick==1) {
			Vec3 center = getCenterPos(player);
			AABB bb = new AABB(center, center).inflate(32);

			for (ItemEntity entity : pLevel.getEntitiesOfClass(ItemEntity.class, bb)) {
				if (entity == null || entity.isRemoved() || !entity.getTags().contains("sendToNearestPlayer")) continue;
				Vec3 diff = getCenterPos(entity).subtract(center);
				double distance = diff.length();
				if (distance > 32) continue;

				var pos = entity.position();
				if (pLevel.random.nextFloat() < (Mth.clamp(entity.getItem().getCount() - 10, 5, 100) / 64f)) {
					Vec3 pPos = VecHelper.offsetRandomly(pos, pLevel.random, .5f);
					pLevel.addParticle(ParticleTypes.END_ROD, pPos.x, pos.y, pPos.z, 0, -.1f, 0);
				}
			}
		}
	}

	static int tick = 0;
	public static void itemTransferTick(Level pLevel, Player player) {
		tick = ++tick % 2;
		if (tick==1) {
			Vec3 center = getCenterPos(player);
			AABB bb = new AABB(center, center).inflate(32);

			for (ItemEntity entity : pLevel.getEntitiesOfClass(ItemEntity.class, bb)) {
				if (entity == null || entity.isRemoved() || !entity.getTags().contains("sendToNearestPlayer")) continue;
				Vec3 diff = getCenterPos(entity).subtract(center);
				double distance = diff.length();
				if (distance > 32) continue;

				Vec3 pushVec = diff.normalize().scale((32 - distance) * -1);
				float forceFactor = 1 / 128f;
				Vec3 force = pushVec.scale(forceFactor * 0.5);

				entity.push(force.x, force.y, force.z);
				entity.fallDistance = 0;
				entity.hurtMarked = true;

				Vec3 currentMovement = entity.getDeltaMovement();
				if (currentMovement.length() > 2.0) {
					Vec3 limitedMovement = currentMovement.normalize().scale(2.0);
					entity.setDeltaMovement(limitedMovement);
				}
			}
		}
	}

	public static void popResource(Level pLevel, BlockPos pPos, ItemStack pStack, int retrieverLevel) {
		double d0 = (double) EntityType.ITEM.getHeight() / 2.0D;
		double d1 = (double)pPos.getX() + 0.5D + Mth.nextDouble(pLevel.random, -0.25D, 0.25D);
		double d2 = (double)pPos.getY() + 0.5D + Mth.nextDouble(pLevel.random, -0.25D, 0.25D) - d0;
		double d3 = (double)pPos.getZ() + 0.5D + Mth.nextDouble(pLevel.random, -0.25D, 0.25D);
		ItemEntity item = new ItemEntity(pLevel, d1, d2, d3, pStack);
		if (retrieverLevel >= 2) item.addTag("sendToNearestPlayer");
		if (retrieverLevel == 3) item.setNoGravity(true);
		if (retrieverLevel >= 3) item.setNoPickUpDelay();
		popResource(pLevel, () -> item, pStack);
	}

	private static void popResource(Level pLevel, Supplier<ItemEntity> pItemEntitySupplier, ItemStack pStack) {
		if (!pLevel.isClientSide && !pStack.isEmpty() && pLevel.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !pLevel.restoringBlockSnapshots) {
			ItemEntity itementity = pItemEntitySupplier.get();
			itementity.setDefaultPickUpDelay();
			pLevel.addFreshEntity(itementity);
		}
	}


}
