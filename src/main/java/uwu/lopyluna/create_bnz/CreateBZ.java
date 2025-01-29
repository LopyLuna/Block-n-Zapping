package uwu.lopyluna.create_bnz;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import uwu.lopyluna.create_bnz.infrastructure.data.BZDatagen;
import uwu.lopyluna.create_bnz.registry.*;

import static net.minecraft.world.item.CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;

@Mod(CreateBZ.MOD_ID)
public class CreateBZ {
    public static final String NAME = "Create: Block n' Zapping";
    public static final String MOD_ID = "create_bnz";

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);
    static {
        REGISTRATE.setTooltipModifierFactory(item -> new ItemDescription.Modifier(item, TooltipHelper.Palette.STANDARD_CREATE));
    }

    public CreateBZ() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);

        BZItems.register();
        BZTags.init();
        BZRecipeTypes.register(modEventBus);
        BZPackets.registerPackets();

        modEventBus.addListener(this::addCreative);
        modEventBus.addListener(EventPriority.LOWEST, BZDatagen::gatherData);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey())
            event.getEntries().putBefore(AllItems.WORLDSHAPER.asStack(), BZItems.BLOCK_ZAPPER.asStack(), PARENT_AND_SEARCH_TABS);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
