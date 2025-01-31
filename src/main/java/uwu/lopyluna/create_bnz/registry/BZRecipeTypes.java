package uwu.lopyluna.create_bnz.registry;

import com.simibubi.create.foundation.recipe.IRecipeTypeInfo;
import com.simibubi.create.foundation.utility.Lang;

import io.github.fabricators_of_create.porting_lib.util.ShapedRecipeUtil;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import uwu.lopyluna.create_bnz.CreateBZ;
import uwu.lopyluna.create_bnz.content.recipes.ZapperRecipe;

import java.util.function.Supplier;

public enum BZRecipeTypes implements IRecipeTypeInfo {
    ZAPPER_RECIPE(ZapperRecipe.Serializer::new, () -> RecipeType.CRAFTING)
    ;

    private final ResourceLocation id;
	private final RecipeSerializer<?> serializerObject;
	private final Supplier<RecipeType<?>> type;

    BZRecipeTypes(Supplier<RecipeSerializer<?>> serializerSupplier, Supplier<RecipeType<?>> typeSupplier) {
		String name = Lang.asId(name());
		id = CreateBZ.asResource(name);
		serializerObject = Registry.register(BuiltInRegistries.RECIPE_SERIALIZER, id, serializerSupplier.get());
		type = typeSupplier;
	}

    public static void register() {
		ShapedRecipeUtil.setCraftingSize(9, 9);
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeSerializer<?>> T getSerializer() {
		return (T) serializerObject;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T extends RecipeType<?>> T getType() {
        return (T) type.get();
    }
}
