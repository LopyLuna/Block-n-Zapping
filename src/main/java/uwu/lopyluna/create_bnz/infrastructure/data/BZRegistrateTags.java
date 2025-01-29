package uwu.lopyluna.create_bnz.infrastructure.data;

import com.simibubi.create.content.decoration.palettes.AllPaletteStoneTypes;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import uwu.lopyluna.create_bnz.CreateBZ;

import static uwu.lopyluna.create_bnz.registry.BZTags.AllItemTags.RENEWABLE_GENERATING;

@SuppressWarnings("deprecation")
public class BZRegistrateTags {

	public static void addGenerators() {
		CreateBZ.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, BZRegistrateTags::genBlockTags);
		CreateBZ.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, BZRegistrateTags::genItemTags);
	}
	private static void genItemTags(RegistrateTagsProvider<Item> provIn) {
		TagGen.CreateTagsProvider<Item> prov = new TagGen.CreateTagsProvider<>(provIn, Item::builtInRegistryHolder);


		prov.tag(RENEWABLE_GENERATING.tag)
				.add(Items.COBBLESTONE, Items.STONE, Items.BASALT, Items.OBSIDIAN, Items.ANDESITE, Items.GRANITE,
						Items.DIORITE, Items.MUD, Items.DIRT, Items.DIRT_PATH, Items.COARSE_DIRT, Items.ROOTED_DIRT,
						Items.MYCELIUM, Items.GRASS_BLOCK, Items.PODZOL, Items.SAND, Items.RED_SAND, Items.SOUL_SAND,
						Items.GRAVEL, Items.SNOW_BLOCK, Items.SNOW, Items.ICE, Items.NETHERRACK, Items.MOSS_BLOCK,
						Items.END_STONE, Items.MAGMA_BLOCK, AllPaletteStoneTypes.LIMESTONE.getBaseBlock().get().asItem(),
						AllPaletteStoneTypes.SCORIA.getBaseBlock().get().asItem()
				);
	}

	private static void genBlockTags(RegistrateTagsProvider<Block> prov) {


	}
}
