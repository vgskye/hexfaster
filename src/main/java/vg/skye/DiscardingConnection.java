package vg.skye;

import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DiscardingConnection extends Connection {
    public DiscardingConnection(PacketFlow receiving) {
        super(receiving);
    }

    @Override
    public void send(@NotNull Packet<?> packet, @Nullable PacketSendListener sendListener) {
        // no-op
    }
}
