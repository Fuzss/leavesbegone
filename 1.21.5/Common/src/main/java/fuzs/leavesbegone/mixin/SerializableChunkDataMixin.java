package fuzs.leavesbegone.mixin;

import fuzs.leavesbegone.attachment.SerializableLevelChunkTicks;
import fuzs.leavesbegone.world.level.chunk.RandomBlockTickerChunk;
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
    private static void copyOf(ServerLevel level, ChunkAccess chunkAccess, CallbackInfoReturnable<SerializableChunkData> callback) {
        if (!(chunkAccess instanceof RandomBlockTickerChunk chunk)) return;
        // random block ticks are only saved right before serialization, so that we can choose to only stored them when not empty
        SerializableLevelChunkTicks.saveTickContainerFromLevel((LevelChunk) chunkAccess,
                chunk.leavesbegone$getRandomBlockTicks());
    }
}
