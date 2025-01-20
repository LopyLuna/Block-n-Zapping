package uwu.lopyluna.create_bnz;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class CreateClientBZ {
    public static void onCtorClient(IEventBus modEventBus, IEventBus forgeEventBus) {
        modEventBus.addListener(CreateClientBZ::clientInit);
    }

    public static void clientInit(final FMLClientSetupEvent event) {
    }
}
