package uwu.lopyluna.create_bnz;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import static net.minecraft.client.gui.screens.Screen.hasControlDown;
import static uwu.lopyluna.create_bnz.CreateBZ.MOD_ID;

public class BZUtils {
    public static String blockZapperLang = MOD_ID + ".handheld_block_zapper";

    public static MutableComponent holdCtrl() {
        return Component.translatable("create_bnz.tooltip.holdForModifiers").append("[").withStyle(ChatFormatting.DARK_GRAY)
                .append(Component.translatable("create.tooltip.keyCtrl").withStyle(hasControlDown() ? ChatFormatting.WHITE : ChatFormatting.GRAY))
                .append("]").append(Component.translatable(blockZapperLang + ".ctrl")).withStyle(ChatFormatting.DARK_GRAY);
    }
}
