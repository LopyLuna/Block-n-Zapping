package uwu.lopyluna.create_bnz.event;

import net.minecraft.world.entity.player.Player;
import uwu.lopyluna.create_bnz.content.items.zapper.TerrainTools;

public class BZCommonEvents {

    public static void onPlayerTick(Player player) {
		if (!player.level().isClientSide)  {
			TerrainTools.itemTransferTick(player.level(), player);
		}
    }
}
