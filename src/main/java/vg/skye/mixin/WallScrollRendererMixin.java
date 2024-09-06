package vg.skye.mixin;

import at.petrak.hexcasting.client.entity.WallScrollRenderer;
import at.petrak.hexcasting.common.entities.EntityWallScroll;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vg.skye.renderer.WorldlyPatternRenderHelpers;

@Mixin(WallScrollRenderer.class)
public abstract class WallScrollRendererMixin extends EntityRenderer<EntityWallScroll> {
    protected WallScrollRendererMixin(EntityRendererProvider.Context context) {
        super(context);
        throw new RuntimeException("this should never be called ever");
    }

    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", ordinal = 0, shift = At.Shift.AFTER), method = "render(Lat/petrak/hexcasting/common/entities/EntityWallScroll;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", cancellable = true)
    private void renderMixin(EntityWallScroll wallScroll, float yaw, float partialTicks, PoseStack ps, MultiBufferSource bufSource, int packedLight, CallbackInfo ci, @Local(name = "light") int light) {
        if(wallScroll.pattern != null)
            WorldlyPatternRenderHelpers.renderPatternForScroll(wallScroll.pattern, wallScroll, ps, bufSource, light, wallScroll.blockSize, wallScroll.getShowsStrokeOrder());
        ps.popPose();
        super.render(wallScroll, yaw, partialTicks, ps, bufSource, packedLight);
        ci.cancel();
    }
}
