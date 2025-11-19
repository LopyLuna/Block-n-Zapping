package uwu.lopyluna.create_bnz;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.AllItems;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import uwu.lopyluna.create_bnz.config.BZConfig;
import uwu.lopyluna.create_bnz.infrastructure.data.BZDatagen;
import uwu.lopyluna.create_bnz.registry.BZItems;
import uwu.lopyluna.create_bnz.registry.BZPackets;
import uwu.lopyluna.create_bnz.registry.BZRecipeTypes;
import uwu.lopyluna.create_bnz.registry.BZTags;

import static net.minecraft.world.item.CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS;

@SuppressWarnings("unused")
@Mod(CreateBZ.MOD_ID)
public class CreateBZ {
    public static final String NAME = "Create: Block n' Zapping";
    public static final String MOD_ID = "create_bnz";

    public static final CreateRegistrate REG = CreateRegistrate.create(MOD_ID);

    public CreateBZ() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REG.registerEventListeners(modEventBus);

        BZConfig.register();
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

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
