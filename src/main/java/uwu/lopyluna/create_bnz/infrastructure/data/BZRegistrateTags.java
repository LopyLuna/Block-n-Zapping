package uwu.lopyluna.create_bnz.infrastructure.data;

import com.tterrag.registrate.providers.ProviderType;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import uwu.lopyluna.create_bnz.CreateBZ;

public class BZRegistrateTags {

	public static void addGenerators() {
		CreateBZ.REGISTRATE.addDataGenerator(ProviderType.BLOCK_TAGS, BZRegistrateTags::genBlockTags);
		CreateBZ.REGISTRATE.addDataGenerator(ProviderType.ITEM_TAGS, BZRegistrateTags::genItemTags);
	}
	private static void genItemTags(RegistrateTagsProvider<Item> prov) {


	}

	private static void genBlockTags(RegistrateTagsProvider<Block> prov) {


	}
}
