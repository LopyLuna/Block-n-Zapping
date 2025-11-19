package uwu.lopyluna.create_bnz.content.items.zapper;

import com.simibubi.create.content.equipment.zapper.ConfigureZapperPacket;
import com.simibubi.create.content.equipment.zapper.PlacementPatterns;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import uwu.lopyluna.create_bnz.content.items.zapper.tools.PlacementOptions;
import uwu.lopyluna.create_bnz.content.items.zapper.tools.TerrainBrushes;

public class ConfigureBlockZapperPacket extends ConfigureZapperPacket {

	protected TerrainBrushes brush;
	protected int brushParamX;
	protected int brushParamY;
	protected int brushParamZ;
	protected int offsetX;
	protected int offsetY;
	protected int offsetZ;
	protected TerrainTools tool;
	protected PlacementOptions placement;

	public ConfigureBlockZapperPacket(InteractionHand hand, PlacementPatterns pattern, TerrainBrushes brush, int brushParamX, int brushParamY, int brushParamZ, int offsetX, int offsetY, int offsetZ, TerrainTools tool, PlacementOptions placement) {
		super(hand, pattern);
		this.brush = brush;
		this.brushParamX = brushParamX;
		this.brushParamY = brushParamY;
		this.brushParamZ = brushParamZ;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
		this.tool = tool;
		this.placement = placement;
	}

	public ConfigureBlockZapperPacket(FriendlyByteBuf buffer) {
		super(buffer);
		brush = buffer.readEnum(TerrainBrushes.class);
		brushParamX = buffer.readVarInt();
		brushParamY = buffer.readVarInt();
		brushParamZ = buffer.readVarInt();
		offsetX = buffer.readVarInt();
		offsetY = buffer.readVarInt();
		offsetZ = buffer.readVarInt();
		tool = buffer.readEnum(TerrainTools.class);
		placement = buffer.readEnum(PlacementOptions.class);
	}

	@Override
	public void write(FriendlyByteBuf buffer) {
		super.write(buffer);
		buffer.writeEnum(brush);
		buffer.writeVarInt(brushParamX);
		buffer.writeVarInt(brushParamY);
		buffer.writeVarInt(brushParamZ);
		buffer.writeVarInt(offsetX);
		buffer.writeVarInt(offsetY);
		buffer.writeVarInt(offsetZ);
		buffer.writeEnum(tool);
		buffer.writeEnum(placement);
	}

	@Override
	public void configureZapper(ItemStack stack) {
		BlockZapperItem.configureSettings(stack, pattern, brush, brushParamX, brushParamY, brushParamZ, offsetX, offsetY, offsetZ, tool, placement);
	}

}
