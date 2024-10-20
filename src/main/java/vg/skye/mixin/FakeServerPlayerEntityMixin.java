package vg.skye.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import baritone.api.fakeplayer.FakeServerPlayerEntity;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vg.skye.DiscardingServerGamePacketListenerImpl;

@Mixin(FakeServerPlayerEntity.class)
public class FakeServerPlayerEntityMixin {
    @Redirect(method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/server/level/ServerLevel;Lcom/mojang/authlib/GameProfile;)V", at = @At(value = "NEW", target = "net/minecraft/server/network/ServerGamePacketListenerImpl"))
    ServerGamePacketListenerImpl useFake(MinecraftServer server, Connection connection, ServerPlayer player) {
        return new DiscardingServerGamePacketListenerImpl(server, player);
    }
}
