package uwu.lopyluna.create_bnz.content.recipes;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.NotNull;
import uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperItem;
import uwu.lopyluna.create_bnz.content.modifiers.ModifierTier;
import uwu.lopyluna.create_bnz.content.modifiers.Modifiers;
import uwu.lopyluna.create_bnz.registry.BZItems;
import uwu.lopyluna.create_bnz.registry.BZRecipeTypes;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ZapperRecipe extends ShapedRecipe {

    private final ShapedRecipe recipe;
    private final Modifiers component;
    private final ModifierTier tier;

    @SuppressWarnings("all")
    public ZapperRecipe(ShapedRecipe recipe, Modifiers component, ModifierTier tier) {
        super(recipe.getId(), recipe.getGroup(), CraftingBookCategory.MISC, recipe.getWidth(), recipe.getHeight(), recipe.getIngredients(), recipe.getResultItem(null));
        this.recipe = recipe;
        this.component = component;
        this.tier = tier;
    }

    @Override
    public boolean matches(CraftingContainer inv, Level world) {
        return getRecipe().matches(inv, world);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return recipe.getIngredients();
    }

    @Override
    public ItemStack assemble(CraftingContainer inv, RegistryAccess pRegistryAccess) {
        for (int slot = 0; slot < inv.getContainerSize(); slot++) {
            ItemStack handgun = inv.getItem(slot).copy();
            if (!BZItems.BLOCK_ZAPPER.isIn(handgun)) continue;
            if (!(handgun.getItem() instanceof BlockZapperItem zapperItem)) continue;
            CompoundTag nbt = handgun.getOrCreateTag();
            Modifiers modifier = getUpgradedComponent();
            ModifierTier tier = getTier();
            if (modifier.getLevel(nbt) != tier.require_level) continue;
            if (zapperItem.applyModifier(modifier, handgun) != 0) return handgun;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        ItemStack handgun = new ItemStack(BZItems.BLOCK_ZAPPER.get());
        BlockZapperItem zapperItem = ((BlockZapperItem) handgun.getItem());
        CompoundTag nbt = handgun.getOrCreateTag();
        Modifiers modifier = getUpgradedComponent();
        ModifierTier tier = getTier();
        if ((modifier.getLevel(nbt) == tier.require_level) && zapperItem.applyModifier(modifier, handgun) != 0) return handgun;
        return ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return getRecipe().getId();
    }

    @Override
    public RecipeType<?> getType() {
        return BZRecipeTypes.ZAPPER_RECIPE.getType();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BZRecipeTypes.ZAPPER_RECIPE.getSerializer();
    }

    private static ZapperRecipe fromJsonShaped(ShapedRecipe recipe, JsonObject json) {
        Modifiers component = Modifiers.valueOf(GsonHelper.getAsString(json, "component"));
        ModifierTier tier = ModifierTier.valueOf(GsonHelper.getAsString(json, "tier"));
        return new ZapperRecipe(recipe, component, tier);
    }
    private static ZapperRecipe fromNetworkShaped(ShapedRecipe recipe, FriendlyByteBuf buffer) {
        Modifiers component = Modifiers.valueOf(buffer.readUtf(buffer.readInt()));
        ModifierTier tier = ModifierTier.valueOf(buffer.readUtf(buffer.readInt()));
        return new ZapperRecipe(recipe, component, tier);
    }
    @ParametersAreNonnullByDefault
    public static class Serializer extends ShapedRecipe.Serializer {

        @Override
        public @NotNull ShapedRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            return fromJsonShaped(super.fromJson(recipeId, json), json);
        }

        @Override
        public ShapedRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            return fromNetworkShaped(Objects.requireNonNull(super.fromNetwork(recipeId, buffer)), buffer);
        }

        @Override
        public void toNetwork(FriendlyByteBuf byteBuf, ShapedRecipe recipe) {
            super.toNetwork(byteBuf, recipe);
            if (recipe instanceof ZapperRecipe zapperRecipe) {
                String name = zapperRecipe.getUpgradedComponent().name();
                String name2 = zapperRecipe.getTier().name();
                byteBuf.writeInt(name.length());
                byteBuf.writeUtf(name);
                byteBuf.writeInt(name2.length());
                byteBuf.writeUtf(name2);
            }
        }

    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return getRecipe().canCraftInDimensions(width, height);
    }

    public ShapedRecipe getRecipe() {
        return recipe;
    }

    public Modifiers getUpgradedComponent() {
        return component;
    }

    public ModifierTier getTier() {
        return tier;
    }
}