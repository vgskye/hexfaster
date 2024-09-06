package vg.skye.mixin;

import at.petrak.hexcasting.api.spell.casting.CastingContext;
import at.petrak.hexcasting.api.spell.casting.CastingHarness;
import at.petrak.hexcasting.api.spell.casting.ResolvedPatternType;
import at.petrak.hexcasting.api.spell.casting.eval.FunctionalData;
import at.petrak.hexcasting.api.spell.iota.Iota;
import com.llamalad7.mixinextras.sugar.Local;
import kotlin.Pair;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = CastingHarness.class, remap = false)
public class CastingHarnessMixin {
    @Final
    @Shadow
    private CastingContext ctx;

    @Inject(method = "handleParentheses", at = @At(
            value = "INVOKE",
            target = "Lat/petrak/hexcasting/api/spell/iota/PatternIota;getPattern()Lat/petrak/hexcasting/api/spell/math/HexPattern;",
            ordinal = 1,
            shift = At.Shift.BY,
            by = -5), cancellable = true)
    public void whyDisplayIfNotDebug(Iota iota, CallbackInfoReturnable<Pair<FunctionalData, ResolvedPatternType>> cir, @Local Pair<FunctionalData, ResolvedPatternType> out) {
        if (!ctx.getDebugPatterns()) {
            cir.setReturnValue(out);
            cir.cancel();
        }
    }
}
