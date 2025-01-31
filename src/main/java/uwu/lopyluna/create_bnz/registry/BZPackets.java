package uwu.lopyluna.create_bnz.registry;

import com.simibubi.create.foundation.networking.SimplePacketBase;

import me.pepperbell.simplenetworking.SimpleChannel;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import uwu.lopyluna.create_bnz.content.items.zapper.ConfigureBlockZapperPacket;

import java.util.function.Function;

import static com.simibubi.create.foundation.networking.SimplePacketBase.NetworkDirection.PLAY_TO_SERVER;
import static uwu.lopyluna.create_bnz.CreateBZ.asResource;

public enum BZPackets {
    CONFIGURE_BLOCK_ZAPPER(ConfigureBlockZapperPacket.class, ConfigureBlockZapperPacket::new, PLAY_TO_SERVER);

    public static final ResourceLocation CHANNEL_NAME = asResource("main");
	private static SimpleChannel channel;

	private final BZPackets.PacketType<?> packetType;

	<T extends SimplePacketBase> BZPackets(Class<T> type, Function<FriendlyByteBuf, T> factory,
											SimplePacketBase.NetworkDirection direction) {
		packetType = new BZPackets.PacketType<>(type, factory, direction);
	}

	public static void registerPackets() {
		channel = new SimpleChannel(CHANNEL_NAME);
		for (BZPackets packet : values())
			packet.packetType.register();
	}

	public static SimpleChannel getChannel() {
		return channel;
	}

	private static class PacketType<T extends SimplePacketBase> {
		private static int index = 0;

		private final Function<FriendlyByteBuf, T> decoder;
		private final Class<T> type;
		private final SimplePacketBase.NetworkDirection direction;

		private PacketType(Class<T> type, Function<FriendlyByteBuf, T> factory, SimplePacketBase.NetworkDirection direction) {
			decoder = factory;
			this.type = type;
			this.direction = direction;
		}

		private void register() {
			switch (direction) {
				case PLAY_TO_CLIENT -> getChannel().registerS2CPacket(type, index++, decoder);
				case PLAY_TO_SERVER -> getChannel().registerC2SPacket(type, index++, decoder);
			}
		}
	}
}
