package vg.skye.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.samsthenerd.hexgloop.screens.PatternStyle;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Style.class)
public class StyleMixin {
    @ModifyReturnValue(method = "getFont", at = @At("RETURN"))
    private ResourceLocation fakeFont(ResourceLocation original) {
        var patSty = ((PatternStyle) this);
        if (patSty.isHidden() || patSty.getPattern() != null)
            return new ResourceLocation("hexfaster", "dummy_font");
        return original;
    }
}
