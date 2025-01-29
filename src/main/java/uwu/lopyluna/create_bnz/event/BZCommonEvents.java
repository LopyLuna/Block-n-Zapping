package uwu.lopyluna.create_bnz.event;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uwu.lopyluna.create_bnz.content.items.zapper.TerrainTools;

@Mod.EventBusSubscriber
public class BZCommonEvents {


    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.START) {
            if (event.player.level().isClientSide) {
                TerrainTools.itemTickClient(event.player.level(), event.player);
            } else {
                TerrainTools.itemTransferTick(event.player.level(), event.player);
            }
        }
    }
}
