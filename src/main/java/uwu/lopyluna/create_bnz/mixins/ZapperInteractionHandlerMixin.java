package uwu.lopyluna.create_bnz.mixins;

import com.simibubi.create.content.equipment.zapper.ZapperInteractionHandler;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperItem;

@Mixin(value = ZapperInteractionHandler.class, remap = false)
public class ZapperInteractionHandlerMixin {
    @Inject(method = "getRange", at = @At("HEAD"), cancellable = true)
    private static void getRange(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack.getItem() instanceof BlockZapperItem blockZapperItem) {
            cir.setReturnValue(blockZapperItem.getZappingRange(stack));
            cir.cancel();
        }
    }
}
