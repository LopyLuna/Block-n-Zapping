package uwu.lopyluna.create_bnz.mixins;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.equipment.zapper.ZapperInteractionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperItem;

import static com.simibubi.create.content.equipment.zapper.ZapperInteractionHandler.getRange;

@Mixin(value = ZapperInteractionHandler.class, remap = false)
public class ZapperInteractionHandlerSelectMixin {

    @Inject(method = "trySelect", at = @At("HEAD"), cancellable = true)
    private static void trySelect(ItemStack stack, Player player, CallbackInfoReturnable<Boolean> cir) {
        if (stack.getItem() instanceof BlockZapperItem item)
            if (bnz$trySelect(stack, player, item)) {
                cir.setReturnValue(false);
                cir.cancel();
            }
    }

    @Unique
    private static boolean bnz$trySelect(ItemStack stack, Player player, BlockZapperItem item) {
        Vec3 start = player.position().add(0, player.getEyeHeight(), 0);
        Vec3 range = player.getLookAngle().scale(getRange(stack));
        BlockHitResult raytrace = player.level().clip(new ClipContext(start, start.add(range), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, player));
        BlockPos pos = raytrace.getBlockPos();
        BlockState newState = player.level().getBlockState(pos);
        if (newState.getBlock().defaultDestroyTime() > item.getHardnessSupport(stack)) {
            AllSoundEvents.DENY.playOnServer(player.level(), player.blockPosition());
            return true;
        }
        return false;
    }
}
