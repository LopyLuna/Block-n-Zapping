package uwu.lopyluna.create_bnz.registry;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;
import uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperItem;

import static uwu.lopyluna.create_bnz.CreateBZ.REGISTRATE;

public class BZItems {
    static {
        REGISTRATE.setCreativeTab(AllCreativeModeTabs.BASE_CREATIVE_TAB);
    }

    public static final ItemEntry<BlockZapperItem> BLOCK_ZAPPER = REGISTRATE.item("handheld_block_zapper", BlockZapperItem::new)
            .lang("Handheld Block Zapper")
            .model(AssetLookup.itemModelWithPartials())
            .register();


    public static void register() {}
}
