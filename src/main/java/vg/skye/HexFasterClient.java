package vg.skye;

import at.petrak.hexcasting.api.block.circle.BlockCircleComponent;
import com.samsthenerd.hexgloop.blockentities.HexGloopBEs;
import net.fabricmc.api.ClientModInitializer;
import net.zestyblaze.lootr.registry.LootrBlockEntityInit;
import vg.skye.slatewheel.LootrChestInstance;
import vg.skye.slatewheel.SlateChestInstance;

import static com.jozufozu.flywheel.backend.instancing.InstancedRenderRegistry.configure;

public class HexFasterClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        configure(HexGloopBEs.SLATE_CHEST_BE.get())
                .skipRender(blockEntity -> {
                    var blockState = blockEntity.getBlockState();
                    return !blockState.hasProperty(BlockCircleComponent.ENERGIZED) || !blockState.getValue(BlockCircleComponent.ENERGIZED);
                })
                .factory(SlateChestInstance::new)
                .apply();

        configure(LootrBlockEntityInit.SPECIAL_LOOT_CHEST)
                .alwaysSkipRender()
                .factory(LootrChestInstance::new)
                .apply();
    }
}
