package uwu.lopyluna.create_bnz.compat.jei;

import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.Nullable;
import uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperItem;
import uwu.lopyluna.create_bnz.content.modifiers.ModifierTier;
import uwu.lopyluna.create_bnz.content.modifiers.Modifiers;
import uwu.lopyluna.create_bnz.content.recipes.ZapperRecipe;
import uwu.lopyluna.create_bnz.registry.BZItems;
import uwu.lopyluna.create_bnz.registry.BZRecipeTypes;

import java.util.ArrayList;
import java.util.List;

public final class ZapperRecipeMaker {
    private final RecipeManager recipeManager;

    public List<CraftingRecipe> createRecipes() {
        List<CraftingRecipe> allRecipes = recipeManager.getAllRecipesFor(BZRecipeTypes.ZAPPER_RECIPE.getType());
        List<CraftingRecipe> capturedRecipes = new ArrayList<>();

        for (CraftingRecipe recipe : allRecipes) {
            if (!(recipe instanceof ZapperRecipe zapperRecipe)) continue;
            Modifiers modifier = zapperRecipe.getUpgradedComponent();
            ModifierTier tier = zapperRecipe.getTier();
            var ingredients = zapperRecipe.getIngredients();
            if (tier.require_level != 0) {
                for (var ingredient : ingredients) {
                    for (ItemStack itemStack : ingredient.getItems()) {
                        if (!(itemStack.getItem() instanceof BlockZapperItem)) continue;
                        var nbt = itemStack.getOrCreateTag();
                        nbt.putInt(modifier.baseName, tier.require_level);
                        nbt.putBoolean("jeiView", true);
                        NBTHelper.writeEnum(nbt, "AppliedModifiers0", modifier);
                    }
                }
            }
            ItemStack handgun = new ItemStack(BZItems.BLOCK_ZAPPER.get());
            assert handgun.getTag() != null;
            handgun.getTag().putBoolean("jeiView", true);
            BlockZapperItem zapperItem = ((BlockZapperItem) handgun.getItem());
            capturedRecipes.add(new ShapedRecipe(zapperRecipe.getId(), zapperRecipe.getGroup(), CraftingBookCategory.MISC, zapperRecipe.getRecipeWidth(), zapperRecipe.getRecipeHeight(),
                    ingredients, zapperItem.setModifierToTier(modifier, tier, handgun)));
        }
        return capturedRecipes;
    }

    public ZapperRecipeMaker() {
        Minecraft minecraft = Minecraft.getInstance();
        checkNotNull(minecraft, "minecraft");
        ClientLevel world = minecraft.level;
        checkNotNull(world, "minecraft world");
        this.recipeManager = world.getRecipeManager();
    }

    public <T> void checkNotNull(@Nullable T object, String name) {
        if (object == null) {
            throw new NullPointerException(name + " must not be null.");
        }
    }
}
