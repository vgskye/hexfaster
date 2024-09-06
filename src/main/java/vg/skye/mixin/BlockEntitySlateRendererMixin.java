package vg.skye.mixin;

import at.petrak.hexcasting.client.be.BlockEntitySlateRenderer;
import at.petrak.hexcasting.common.blocks.circles.BlockEntitySlate;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vg.skye.renderer.WorldlyPatternRenderHelpers;

@Mixin(BlockEntitySlateRenderer.class)
public class BlockEntitySlateRendererMixin {
    /**
     * @author Skye
     * @reason The alternative is inject head unconditional cancel
     */
    @Overwrite
    public void render(BlockEntitySlate tile, float pPartialTick, PoseStack ps,
                       MultiBufferSource buffer, int light, int overlay) {
        if (tile.pattern == null)
            return;

        var bs = tile.getBlockState();

        WorldlyPatternRenderHelpers.renderPatternForSlate(tile, tile.pattern, ps, buffer, light, bs);
    }
}
