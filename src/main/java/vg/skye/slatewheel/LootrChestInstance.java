package vg.skye.slatewheel;

import com.jozufozu.flywheel.api.MaterialManager;
import com.jozufozu.flywheel.api.instance.DynamicInstance;
import com.jozufozu.flywheel.api.instance.TickableInstance;
import com.jozufozu.flywheel.backend.instancing.blockentity.BlockEntityInstance;
import com.jozufozu.flywheel.core.Materials;
import com.jozufozu.flywheel.core.hardcoded.ModelPart;
import com.jozufozu.flywheel.core.materials.model.ModelData;
import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
import com.jozufozu.flywheel.util.AnimationTickHolder;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;

import it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.zestyblaze.lootr.blocks.LootrChestBlock;
import net.zestyblaze.lootr.blocks.entities.LootrChestBlockEntity;
import net.zestyblaze.lootr.config.LootrModConfig;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

import static net.zestyblaze.lootr.client.block.LootrChestBlockRenderer.MATERIAL;
import static net.zestyblaze.lootr.client.block.LootrChestBlockRenderer.MATERIAL2;

public class LootrChestInstance extends BlockEntityInstance<LootrChestBlockEntity> implements DynamicInstance, TickableInstance {

    private OrientedData body;
    private ModelData lid;

    private final Float2FloatFunction lidProgress;
    private Material renderMaterial;
    @NotNull
    private final ChestType chestType;
    private final Quaternion baseRotation;

    private float lastProgress = Float.NaN;

    private UUID playerId = null;

    private Material getMaterial(LootrChestBlockEntity tile) {
        if (LootrModConfig.isVanillaTextures()) {
            return Sheets.CHEST_LOCATION;
        }
        if(playerId == null) {
            Player player = Minecraft.getInstance().player;
            if(player != null) {
                playerId = player.getUUID();
            } else {
                return MATERIAL;
            }
        }
        if(tile.isOpened()) {
            return MATERIAL2;
        }
        if(tile.getOpeners().contains(playerId)) {
            return MATERIAL2;
        } else {
            return MATERIAL;
        }
    }

    public LootrChestInstance(MaterialManager materialManager, LootrChestBlockEntity blockEntity) {
        super(materialManager, blockEntity);

        Block block = blockState.getBlock();

        chestType = blockState.hasProperty(ChestBlock.TYPE) ? blockState.getValue(ChestBlock.TYPE) : ChestType.SINGLE;

        if (block instanceof LootrChestBlock chestBlock) {

            float horizontalAngle = blockState.getValue(ChestBlock.FACING).toYRot();

            baseRotation = Vector3f.YP.rotationDegrees(-horizontalAngle);

            DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> wrapper = chestBlock.combine(blockState, world, getWorldPosition(), true);

            this.lidProgress = wrapper.apply(ChestBlock.opennessCombiner(blockEntity));
        } else {
            baseRotation = Quaternion.ONE;
            lidProgress = $ -> 0f;
        }

        reinitInstances();
    }

    private void reinitInstances() {
        if (body != null) {
            body.delete();
        }
        if (lid != null) {
            lid.delete();
        }

        renderMaterial = getMaterial(blockEntity);

        body = baseInstance()
                .setPosition(getInstancePosition())
                .setRotation(baseRotation);
        lid = lidInstance();
    }

    @Override
    public void tick() {
        if (renderMaterial != getMaterial(blockEntity)) {
            reinitInstances();
            updateLight();
        }
    }

    @Override
    public void beginFrame() {
        float progress = lidProgress.get(AnimationTickHolder.getPartialTicks());

        if (lastProgress == progress) return;

        lastProgress = progress;

        progress = 1.0F - progress;
        progress = 1.0F - progress * progress * progress;

        float angleX = -(progress * ((float) Math.PI / 2F));

        lid.loadIdentity()
                .translate(getInstancePosition())
                .translate(0, 9f/16f, 0)
                .centre()
                .multiply(baseRotation)
                .unCentre()
                .translate(0, 0, 1f / 16f)
                .multiply(Vector3f.XP.rotation(angleX))
                .translate(0, 0, -1f / 16f);
    }

    @Override
    public void updateLight() {
        relight(getWorldPosition(), body, lid);
    }

    @Override
    public void remove() {
        body.delete();
        lid.delete();
    }

    private OrientedData baseInstance() {

        return materialManager.solid(RenderType.entitySolid(renderMaterial.atlasLocation()))
                .material(Materials.ORIENTED)
                .model("base_" + renderMaterial.texture(), this::getBaseModel)
                .createInstance();
    }

    private ModelData lidInstance() {

        return materialManager.solid(RenderType.entitySolid(renderMaterial.atlasLocation()))
                .material(Materials.TRANSFORMED)
                .model("lid_" + renderMaterial.texture(), this::getLidModel)
                .createInstance();
    }

    private ModelPart getBaseModel() {

        return switch (chestType) {
            case LEFT -> ModelPart.builder("chest_base_left", 64, 64)
                    .sprite(renderMaterial.sprite())
                    .cuboid()
                    .textureOffset(0, 19)
                    .start(0, 0, 1)
                    .size(15, 10, 14)
                    .endCuboid()
                    .build();
            case RIGHT -> ModelPart.builder("chest_base_right", 64, 64)
                    .sprite(renderMaterial.sprite())
                    .cuboid()
                    .textureOffset(0, 19)
                    .start(1, 0, 1)
                    .size(15, 10, 14)
                    .endCuboid()
                    .build();
            default -> ModelPart.builder("chest_base", 64, 64)
                    .sprite(renderMaterial.sprite())
                    .cuboid()
                    .textureOffset(0, 19)
                    .start(1, 0, 1)
                    .end(15, 10, 15)
                    .endCuboid()
                    .build();
        };

    }

    private ModelPart getLidModel() {
        return switch (chestType) {
            case LEFT -> ModelPart.builder("chest_lid_left", 64, 64)
                    .sprite(renderMaterial.sprite())
                    .cuboid()
                    .textureOffset(0, 0)
                    .start(0, 0, 1)
                    .size(15, 5, 14)
                    .endCuboid()
                    .cuboid()
                    .start(0, -2, 15)
                    .size(1, 4, 1)
                    .endCuboid()
                    .build();
            case RIGHT -> ModelPart.builder("chest_lid_right", 64, 64)
                    .sprite(renderMaterial.sprite())
                    .cuboid()
                    .textureOffset(0, 0)
                    .start(1, 0, 1)
                    .size(15, 5, 14)
                    .endCuboid()
                    .cuboid()
                    .start(15, -2, 15)
                    .size(1, 4, 1)
                    .endCuboid()
                    .build();
            default -> ModelPart.builder("chest_lid", 64, 64)
                    .sprite(renderMaterial.sprite())
                    .cuboid()
                    .textureOffset(0, 0)
                    .start(1, 0, 1)
                    .size(14, 5, 14)
                    .endCuboid()
                    .cuboid()
                    .start(7, -2, 15)
                    .size(2, 4, 1)
                    .endCuboid()
                    .build();
        };

    }
}