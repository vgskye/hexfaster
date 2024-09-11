package vg.skye.mixin;

import com.jozufozu.flywheel.backend.Backend;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.samsthenerd.hexgloop.blockentities.BERHexChest;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BERHexChest.class)
public class BERHexChestMixin {
    @WrapOperation(method = "render(Lnet/minecraft/world/level/block/entity/BlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V", at = @At(value = "INVOKE", target = "Lcom/samsthenerd/hexgloop/blockentities/BERHexChest;render(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/model/geom/ModelPart;Lnet/minecraft/client/model/geom/ModelPart;FII)V"))
    public void skipRenderIfFlywheel(BERHexChest<?> instance, PoseStack matrices, VertexConsumer vertices, ModelPart lid, ModelPart latch, ModelPart base, float openFactor, int light, int overlay, Operation<Void> original, @Local Level world) {
        if (!(Backend.isOn() && Backend.isFlywheelWorld(world))) {
            original.call(instance, matrices, vertices, lid, latch, base, openFactor, light, overlay);
        }
    }
}
