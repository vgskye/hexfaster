package vg.skye.mixin;

import net.minecraft.client.renderer.ShaderInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import vg.skye.HexFaster;

@Mixin(value = ShaderInstance.class, priority = 1500)
public class ShaderInstanceMixin {
    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/resources/ResourceLocation;<init>(Ljava/lang/String;)V", ordinal = 0))
    private String reallyFixId(String path) {
        var parsed = path.split(":");
        if (parsed.length == 3 && parsed[0].equals(parsed[1])) {
            HexFaster.LOGGER.warn("Found Porting Lib mangling shader names! Trying to repair from {} to {}:{}", path, parsed[0], parsed[2]);
            return parsed[0] + ":" + parsed[2];
        } else {
            return path;
        }
    }
}
