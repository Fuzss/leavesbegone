package fuzs.leavesbegone.init;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.capability.RandomBlockTicksCapability;
import fuzs.puzzleslib.api.capability.v3.CapabilityController;
import fuzs.puzzleslib.api.capability.v3.data.CapabilityKey;
import net.minecraft.world.level.chunk.LevelChunk;

public class ModRegistry {
    static final CapabilityController CAPABILITIES = CapabilityController.from(LeavesBeGone.MOD_ID);
    public static final CapabilityKey<LevelChunk, RandomBlockTicksCapability> RANDOM_BLOCK_TICKS_CAPABILITY = CAPABILITIES.registerLevelChunkCapability(
            "random_block_ticks",
            RandomBlockTicksCapability.class,
            RandomBlockTicksCapability::new);

    public static void bootstrap() {
        // NO-OP
    }
}
