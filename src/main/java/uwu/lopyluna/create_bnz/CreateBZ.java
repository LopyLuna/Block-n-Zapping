package uwu.lopyluna.create_bnz;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipHelper;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.resources.ResourceLocation;
import uwu.lopyluna.create_bnz.registry.*;

@SuppressWarnings("unused")
public class CreateBZ implements ModInitializer {
    public static final String NAME = "Create: Block n' Zapping";
    public static final String MOD_ID = "create_bnz";

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE));
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

	@Override
	public void onInitialize() {
		BZItems.register();
		BZTags.init();
		BZRecipeTypes.register();

		REGISTRATE.register();

		ItemGroupEvents.modifyEntriesEvent(AllCreativeModeTabs.BASE_CREATIVE_TAB.key()).register(entries -> entries.addBefore(AllItems.WORLDSHAPER.asStack(), BZItems.BLOCK_ZAPPER.asStack()));

		BZPackets.registerPackets();
		BZPackets.getChannel().initServerListener();
	}
}
