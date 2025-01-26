package uwu.lopyluna.create_bnz.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_bnz.CreateBZ;

import javax.annotation.ParametersAreNonnullByDefault;
@JeiPlugin
@SuppressWarnings("unused")
@ParametersAreNonnullByDefault
public class CreateBZJEI implements IModPlugin {
    private static final ResourceLocation ID = CreateBZ.asResource("jei_plugin");

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(RecipeTypes.CRAFTING, new ZapperRecipeMaker().createRecipes());
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ID;
    }
}
