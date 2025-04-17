package fuzs.leavesbegone.mixin;

import fuzs.leavesbegone.attachment.SerializableLevelChunkTicks;
import fuzs.leavesbegone.world.level.chunk.RandomBlockTickerChunk;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkMap.class)
abstract class ChunkMapMixin {

    @Inject(method = "save", at = @At("HEAD"))
    private void save(ChunkAccess chunkAccess, CallbackInfoReturnable<Boolean> callback) {
        if (!(chunkAccess instanceof RandomBlockTickerChunk chunk)) return;
        // random block ticks are only saved right before serialization, so that we can choose to only stored them when not empty
        SerializableLevelChunkTicks.saveTickContainerFromLevel((LevelChunk) chunkAccess,
                chunk.leavesbegone$getRandomBlockTicks());
    }
}
