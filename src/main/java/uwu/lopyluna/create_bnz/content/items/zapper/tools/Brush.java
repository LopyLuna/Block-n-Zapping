package uwu.lopyluna.create_bnz.content.items.zapper.tools;

import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.Vec3;
import uwu.lopyluna.create_bnz.content.items.zapper.TerrainTools;

import java.util.Collection;

public abstract class Brush {

	public int param0;
	public int param1;
	public int param2;
	public int amtParams;

	public Brush(int amtParams) {
		this.amtParams = amtParams;
	}

	public void set(int param0, int param1, int param2) {
		this.param0 = param0;
		this.param1 = param1;
		this.param2 = param2;
	}

	public TerrainTools[] getSupportedTools() {
		return TerrainTools.values();
	}
	
	public TerrainTools redirectTool(TerrainTools tool) {
		return tool;
	}

	public boolean hasPlacementOptions() {
		return true;
	}

	public boolean hasConnectivityOptions() {
		return false;
	}

	public int getMax(int paramIndex) {
		return Integer.MAX_VALUE;
	}

	public int getMin(int paramIndex) {
		return 0;
	}

	public Component getParamLabel(int paramIndex) {
		return Lang
			.translateDirect(paramIndex == 0 ? "generic.width" : paramIndex == 1 ? "generic.height" : "generic.length");
	}

	public int get(int paramIndex) {
		return paramIndex == 0 ? param0 : paramIndex == 1 ? param1 : param2;
	}

	public BlockPos getOffset(Vec3 ray, Direction face, PlacementOptions option) {
		return BlockPos.ZERO;
	}

	public abstract Collection<BlockPos> addToGlobalPositions(LevelAccessor world, BlockPos targetPos, Direction targetFace,
		Collection<BlockPos> affectedPositions, TerrainTools usedTool);

}
