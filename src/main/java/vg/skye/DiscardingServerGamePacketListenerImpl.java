package vg.skye;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscardingServerGamePacketListenerImpl extends ServerGamePacketListenerImpl {
    private static final Connection DISCARDING_CONN = new DiscardingConnection(PacketFlow.CLIENTBOUND);

    public DiscardingServerGamePacketListenerImpl(MinecraftServer server, ServerPlayer player) {
        super(server, DISCARDING_CONN, player);
    }

    @Override
    public void send(@NotNull Packet<?> packet, @Nullable PacketSendListener listener) {
        // no-op
    }
}
