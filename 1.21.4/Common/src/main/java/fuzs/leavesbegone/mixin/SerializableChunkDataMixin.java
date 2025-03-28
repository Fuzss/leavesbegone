package fuzs.leavesbegone.mixin;

import fuzs.leavesbegone.capability.RandomBlockTicksCapability;
import fuzs.leavesbegone.init.ModRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SerializableChunkData.class)
abstract class SerializableChunkDataMixin {

    @Inject(method = "copyOf", at = @At("HEAD"))
    private static void copyOf(ServerLevel level, ChunkAccess chunk, CallbackInfoReturnable<SerializableChunkData> callback) {
        ModRegistry.RANDOM_BLOCK_TICKS_CAPABILITY.getIfProvided(chunk)
                .ifPresent((RandomBlockTicksCapability capability) -> {
                    if (capability.getRandomBlockTicks().count() == 0L) {
                        ModRegistry.RANDOM_BLOCK_TICKS_CAPABILITY.clear((LevelChunk) chunk);
                    }
                });
    }
}
