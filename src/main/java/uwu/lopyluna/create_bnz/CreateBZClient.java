package uwu.lopyluna.create_bnz;

import net.fabricmc.api.ClientModInitializer;
import uwu.lopyluna.create_bnz.event.BZClientEvents;
import uwu.lopyluna.create_bnz.registry.BZPackets;

public class CreateBZClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {

		BZClientEvents.register();
		BZPackets.getChannel().initClientListener();
	}
}
