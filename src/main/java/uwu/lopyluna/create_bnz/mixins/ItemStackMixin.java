package uwu.lopyluna.create_bnz.mixins;

import net.minecraft.world.item.ItemStack;

import org.spongepowered.asm.mixin.Mixin;

import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperItem;

@Mixin(value = ItemStack.class)
public class ItemStackMixin {

	@Inject(method = "isDamageableItem", at = @At("HEAD"), cancellable = true)
	public void isDamageableItem(CallbackInfoReturnable<Boolean> cir) {
		ItemStack itemStackDNZ = ((ItemStack)(Object)this);
		if (!itemStackDNZ.isEmpty() && itemStackDNZ.getItem() instanceof BlockZapperItem) {
			cir.setReturnValue(true);
			cir.cancel();
		}
	}
}
