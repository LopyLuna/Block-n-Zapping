package uwu.lopyluna.create_bnz.registry;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import uwu.lopyluna.create_bnz.CreateBZ;

import java.util.Collections;
import java.util.Objects;

@SuppressWarnings({"unused"})
public class BZTags {
	public static <T> TagKey<T> optionalTag(IForgeRegistry<T> registry,
		ResourceLocation id) {
		return Objects.requireNonNull(registry.tags())
			.createOptionalTagKey(id, Collections.emptySet());
	}

	public static <T> TagKey<T> forgeTag(IForgeRegistry<T> registry, String path) {
		return optionalTag(registry, new ResourceLocation("forge", path));
	}

	public static <T> TagKey<T> mcTag(IForgeRegistry<T> registry, String path) {
		return optionalTag(registry, new ResourceLocation("minecraft", path));
	}

	public static TagKey<Item> regItemTag(String pNamespace, String pPath) {
		return optionalTag(ForgeRegistries.ITEMS, new ResourceLocation(pNamespace, pPath));
	}

	public static TagKey<Block> regBlockTag(String pNamespace, String pPath) {
		return optionalTag(ForgeRegistries.BLOCKS, new ResourceLocation(pNamespace, pPath));
	}

	public static TagKey<Block> mcBlockTag(String path) {
		return mcTag(ForgeRegistries.BLOCKS, path);
	}

	public static TagKey<Item> mcItemTag(String path) {
		return mcTag(ForgeRegistries.ITEMS, path);
	}

	public static TagKey<Block> forgeBlockTag(String path) {
		return forgeTag(ForgeRegistries.BLOCKS, path);
	}

	public static TagKey<Item> forgeItemTag(String path) {
		return forgeTag(ForgeRegistries.ITEMS, path);
	}

	public static TagKey<Fluid> forgeFluidTag(String path) {
		return forgeTag(ForgeRegistries.FLUIDS, path);
	}

	public enum NameSpace {
		
		MOD(CreateBZ.MOD_ID, false, true),
		CREATE("create"),
		FORGE("forge"),
		TIC("tconstruct"),
		QUARK("quark")

		;

		public final String id;
		public final boolean optionalDefault;
		public final boolean alwaysDatagenDefault;

		NameSpace(String id) {
			this(id, true, false);
		}

		NameSpace(String id, boolean optionalDefault, boolean alwaysDatagenDefault) {
			this.id = id;
			this.optionalDefault = optionalDefault;
			this.alwaysDatagenDefault = alwaysDatagenDefault;
		}
	}

	public enum AllBlockTags {
		BLOCK_ZAPPER_REPLACEABLE,
		BLOCK_ZAPPER_BLACKLIST
		;

		public final TagKey<Block> tag;
		public final boolean alwaysDatagen;

		AllBlockTags() {
			this(NameSpace.MOD);
		}

		AllBlockTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllBlockTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllBlockTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllBlockTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(ForgeRegistries.BLOCKS, id);
			} else {
				tag = BlockTags.create(id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		@SuppressWarnings("deprecation")
		public boolean matches(Block block) {
			return block.builtInRegistryHolder()
				.is(tag);
		}

		public boolean matches(ItemStack stack) {
			return stack != null && stack.getItem() instanceof BlockItem blockItem && matches(blockItem.getBlock());
		}

		public boolean matches(BlockState state) {
			return state.is(tag);
		}

		private static void init() {}
		
	}

	public enum AllItemTags {
		RENEWABLE_GENERATING
		;

		public final TagKey<Item> tag;
		public final boolean alwaysDatagen;

		AllItemTags() {
			this(NameSpace.MOD);
		}

		AllItemTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllItemTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllItemTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllItemTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(ForgeRegistries.ITEMS, id);
			} else {
				tag = ItemTags.create(id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		@SuppressWarnings("deprecation")
		public boolean matches(Item item) {
			return item.builtInRegistryHolder()
				.is(tag);
		}

		public boolean matches(ItemStack stack) {
			return stack.is(tag);
		}

		private static void init() {}
		
	}

	public enum AllFluidTags {

		;

		public final TagKey<Fluid> tag;
		public final boolean alwaysDatagen;

		AllFluidTags() {
			this(NameSpace.MOD);
		}

		AllFluidTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllFluidTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		AllFluidTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		AllFluidTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(ForgeRegistries.FLUIDS, id);
			} else {
				tag = FluidTags.create(id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		@SuppressWarnings("deprecation")
		public boolean matches(Fluid fluid) {
			return fluid.is(tag);
		}

		public boolean matches(FluidState state) {
			return state.is(tag);
		}

		private static void init() {}
		
	}
	
	public enum BZEntityTags {

		;

		public final TagKey<EntityType<?>> tag;
		public final boolean alwaysDatagen;

		BZEntityTags() {
			this(NameSpace.MOD);
		}

		BZEntityTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		BZEntityTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		BZEntityTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		BZEntityTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(ForgeRegistries.ENTITY_TYPES, id);
			} else {
				tag = TagKey.create(Registries.ENTITY_TYPE, id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		public boolean matches(EntityType<?> type) {
			return type.is(tag);
		}

		public boolean matches(Entity entity) {
			return matches(entity.getType());
		}

		private static void init() {}
		
	}
	
	public enum BZRecipeSerializerTags {

		;

		public final TagKey<RecipeSerializer<?>> tag;
		public final boolean alwaysDatagen;

		BZRecipeSerializerTags() {
			this(NameSpace.MOD);
		}

		BZRecipeSerializerTags(NameSpace namespace) {
			this(namespace, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		BZRecipeSerializerTags(NameSpace namespace, String path) {
			this(namespace, path, namespace.optionalDefault, namespace.alwaysDatagenDefault);
		}

		BZRecipeSerializerTags(NameSpace namespace, boolean optional, boolean alwaysDatagen) {
			this(namespace, null, optional, alwaysDatagen);
		}

		BZRecipeSerializerTags(NameSpace namespace, String path, boolean optional, boolean alwaysDatagen) {
			ResourceLocation id = new ResourceLocation(namespace.id, path == null ? Lang.asId(name()) : path);
			if (optional) {
				tag = optionalTag(ForgeRegistries.RECIPE_SERIALIZERS, id);
			} else {
				tag = TagKey.create(Registries.RECIPE_SERIALIZER, id);
			}
			this.alwaysDatagen = alwaysDatagen;
		}

		public boolean matches(RecipeSerializer<?> recipeSerializer) {
			return ForgeRegistries.RECIPE_SERIALIZERS.getHolder(recipeSerializer).orElseThrow().is(tag);
		}

		private static void init() {}
	}

	public static void init() {
		AllBlockTags.init();
		AllItemTags.init();
		AllFluidTags.init();
		BZEntityTags.init();
		BZRecipeSerializerTags.init();
	}
}
