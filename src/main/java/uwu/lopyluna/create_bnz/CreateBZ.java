package uwu.lopyluna.create_bnz;

import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import uwu.lopyluna.create_bnz.infrastructure.data.BZDatagen;
import uwu.lopyluna.create_bnz.registry.*;


@Mod(CreateBZ.MOD_ID)
public class CreateBZ {
    public static final String NAME = "Create: Block n' Zapping";
    public static final String MOD_ID = "create_bnz";

    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MOD_ID);

    public CreateBZ() {
        ModLoadingContext modLoadingContext = ModLoadingContext.get();

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

        REGISTRATE.registerEventListeners(modEventBus);

        ZapperModifiers.register();
        BZItems.register();
        BZTags.init();
        BZPackets.registerPackets();

        modEventBus.addListener(CreateBZ::init);
        modEventBus.addListener(EventPriority.LOWEST, BZDatagen::gatherData);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static void init(final FMLCommonSetupEvent event) {
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
