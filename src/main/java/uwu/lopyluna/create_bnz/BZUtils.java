package uwu.lopyluna.create_bnz;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import static net.minecraft.client.gui.screens.Screen.hasControlDown;
import static uwu.lopyluna.create_bnz.CreateBZ.MOD_ID;

public class BZUtils {
    public static String blockZapperLang = MOD_ID + ".handheld_block_zapper";

    public static MutableComponent holdCtrl() {
        return Component.translatable("create_bnz.tooltip.holdForModifiers").append("[").withStyle(ChatFormatting.DARK_GRAY)
                .append(Component.translatable("create.tooltip.keyCtrl").withStyle(hasControlDown() ? ChatFormatting.WHITE : ChatFormatting.GRAY))
                .append("]").append(Component.translatable(blockZapperLang + ".ctrl")).withStyle(ChatFormatting.DARK_GRAY);
    }

    public static Vec3 getCenterPos(Entity entity) {
        AABB boundingBox = entity.getBoundingBox();

        double centerX = (boundingBox.minX + boundingBox.maxX) / 2.0;
        double centerY = (boundingBox.minY + boundingBox.maxY) / 2.0;
        double centerZ = (boundingBox.minZ + boundingBox.maxZ) / 2.0;

        return new Vec3(centerX, centerY, centerZ);
    }
}
