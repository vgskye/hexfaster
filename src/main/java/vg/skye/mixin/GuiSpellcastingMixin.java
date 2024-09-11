package vg.skye.mixin;

import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import vg.skye.HexFaster;

@Mixin(GuiSpellcasting.class)
public class GuiSpellcastingMixin {
    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/client/gui/GuiSpellcasting;calculateIotaDisplays()V"))
    public void skipUpdateBeforeInit(GuiSpellcasting instance) {
        HexFaster.LOGGER.info("Why were we updating the display before the screen even init'd again?");
    }
}
