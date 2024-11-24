package vg.skye.mixin;

import io.github.ladysnake.locki.impl.PlayerInventoryKeeper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vg.skye.HexFaster;

@Mixin(PlayerInventoryKeeper.class)
public class PlayerInventoryKeeperMixin {
    @Redirect(method = "getLocks", at = @At(value = "INVOKE", target = "Lcom/google/common/base/Preconditions;checkState(ZLjava/lang/Object;)V"), remap = false)
    private void whyNot(boolean expression, Object errorMessage) {
        if (!expression)
            HexFaster.LOGGER.warn("Allowing access to locks clientside!");
    }
}
