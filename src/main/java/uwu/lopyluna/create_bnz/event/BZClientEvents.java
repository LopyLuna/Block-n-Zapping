package uwu.lopyluna.create_bnz.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperRenderHandler;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class BZClientEvents {

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (isGameActive() && event.phase != TickEvent.Phase.START)
            BlockZapperRenderHandler.tick();
    }

    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }
}
