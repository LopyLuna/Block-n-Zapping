package uwu.lopyluna.create_bnz.mixins;

import net.minecraft.world.entity.player.Player;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import uwu.lopyluna.create_bnz.event.BZCommonEvents;

@Mixin(value = Player.class)
public class PlayerMixin {
	@Inject(method = "tick", at = @At("TAIL"))
	public void tick(CallbackInfo ci) {
		BZCommonEvents.onPlayerTick((Player)(Object)this);
	}
}
