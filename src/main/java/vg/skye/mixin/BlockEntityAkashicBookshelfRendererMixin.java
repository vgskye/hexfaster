package vg.skye.mixin;

import at.petrak.hexcasting.api.spell.math.HexPattern;
import at.petrak.hexcasting.client.be.BlockEntityAkashicBookshelfRenderer;
import at.petrak.hexcasting.common.blocks.akashic.BlockEntityAkashicBookshelf;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import vg.skye.renderer.WorldlyPatternRenderHelpers;

@Mixin(BlockEntityAkashicBookshelfRenderer.class)
public class BlockEntityAkashicBookshelfRendererMixin {
    /**
     * @author Skye
     * @reason Do you want inject head unconditional cancel instead?
     */
    @Overwrite
    public void render(BlockEntityAkashicBookshelf tile, float pPartialTick, PoseStack ps,
                       MultiBufferSource buffer, int light, int overlay) {
        HexPattern pattern = tile.getPattern();
        if (pattern == null) {
            return;
        }

        var bs = tile.getBlockState();

        WorldlyPatternRenderHelpers.renderPatternForAkashicBookshelf(tile, pattern, ps, buffer, light, bs);
    }
}
