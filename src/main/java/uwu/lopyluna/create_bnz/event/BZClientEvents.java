package uwu.lopyluna.create_bnz.event;

import net.createmod.catnip.gui.ScreenOpener;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperItem;
import uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperRenderHandler;
import uwu.lopyluna.create_bnz.content.items.zapper.BlockZapperScreen;
import uwu.lopyluna.create_bnz.registry.BZKeys;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class BZClientEvents {

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent event) {
        if (isGameActive() && event.phase != TickEvent.Phase.START) {
            BlockZapperRenderHandler.tick();
            handleKeybinds();
        }
    }

    private static void handleKeybinds() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        while (BZKeys.OPEN_BLOCK_ZAPPER_GUI.consumeClick()) {
            ItemStack mainHand = mc.player.getMainHandItem();
            if (mainHand.getItem() instanceof BlockZapperItem) {
                ScreenOpener.open(new BlockZapperScreen(mainHand, InteractionHand.MAIN_HAND));
                return;
            }

            // Check offhand
            ItemStack offHand = mc.player.getOffhandItem();
            if (offHand.getItem() instanceof BlockZapperItem) {
                ScreenOpener.open(new BlockZapperScreen(offHand, InteractionHand.OFF_HAND));
                return;
            }
        }
    }

    protected static boolean isGameActive() {
        return !(Minecraft.getInstance().level == null || Minecraft.getInstance().player == null);
    }
}
