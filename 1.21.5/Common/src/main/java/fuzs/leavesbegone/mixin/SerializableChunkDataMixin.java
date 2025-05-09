package fuzs.leavesbegone.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.serialization.Codec;
import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.world.level.chunk.RandomBlockTickerChunk;
import fuzs.leavesbegone.world.level.chunk.RandomBlockTickerPackedTicks;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.storage.RegionStorageInfo;
import net.minecraft.world.level.chunk.storage.SerializableChunkData;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.SavedTick;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(SerializableChunkData.class)
abstract class SerializableChunkDataMixin {
    @Shadow
    @Final
    private static Codec<List<SavedTick<Block>>> BLOCK_TICKS_CODEC;
    @Shadow
    @Final
    private ChunkAccess.PackedTicks packedTicks;

    @ModifyVariable(method = "parse", at = @At("STORE"))
    private static ChunkAccess.PackedTicks parse(ChunkAccess.PackedTicks ticks, LevelHeightAccessor levelHeightAccessor, RegistryAccess registries, CompoundTag tag, @Local ChunkPos chunkPos) {
        List<SavedTick<Block>> list = SavedTick.filterTickListForChunk(tag.read(LeavesBeGone.id("random_block_ticks")
                .toString(), BLOCK_TICKS_CODEC).orElse(List.of()), chunkPos);
        RandomBlockTickerPackedTicks.class.cast(ticks).leavesbegone$setRandomBlocks(list);
        return ticks;
    }

    @ModifyVariable(method = "read", at = @At("STORE"))
    public ChunkAccess read(ChunkAccess chunkAccess, ServerLevel level, PoiManager poiManager, RegionStorageInfo regionStorageInfo, ChunkPos pos) {
        if (chunkAccess instanceof RandomBlockTickerChunk chunk) {
            chunk.leavesbegone$setRandomBlockTicks(new LevelChunkTicks<>(RandomBlockTickerPackedTicks.class.cast(this.packedTicks)
                    .leavesbegone$getRandomBlocks()));
            return chunkAccess;
        } else {
            return chunkAccess;
        }
    }

    @Inject(method = "saveTicks", at = @At("TAIL"))
    private static void saveTicks(CompoundTag tag, ChunkAccess.PackedTicks ticks, CallbackInfo callback) {
        tag.storeNullable(LeavesBeGone.id("random_block_ticks").toString(),
                BLOCK_TICKS_CODEC,
                RandomBlockTickerPackedTicks.class.cast(ticks).leavesbegone$getRandomBlocks());
    }
}
