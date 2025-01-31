package uwu.lopyluna.create_bnz.mixins.accessor;

import net.minecraft.util.valueproviders.IntProvider;

import net.minecraft.world.level.block.DropExperienceBlock;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = DropExperienceBlock.class)
public interface DropExperienceBlockAccessor {
	@Accessor("xpRange")
	IntProvider xpRange$BnZ();
}
