package uwu.lopyluna.create_bnz.event;


import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperRenderHandler;

public class BZClientEvents {

	public static void onTick(Minecraft client) {
        if (isGameActive()) BlockZapperRenderHandler.tick();
    }


	public static void register() {
		ClientTickEvents.END_CLIENT_TICK.register(BZClientEvents::onTick);
	}


    protected static boolean isGameActive() {
		return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }
}
