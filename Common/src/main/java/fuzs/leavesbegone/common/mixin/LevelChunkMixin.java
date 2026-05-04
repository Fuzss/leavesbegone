package fuzs.leavesbegone.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import fuzs.leavesbegone.common.server.level.RandomBlockTickerLevel;
import fuzs.leavesbegone.common.world.level.chunk.RandomBlockTickerChunk;
import fuzs.leavesbegone.common.world.level.chunk.RandomBlockTickerPackedTicks;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.*;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.ticks.LevelChunkTicks;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

/**
 * Aggressive null checking is done as {@code null} values sometimes causes the game to hang without revealing anything
 * in the log or producing a proper crash-report.
 */
@Mixin(LevelChunk.class)
abstract class LevelChunkMixin extends ChunkAccess implements RandomBlockTickerChunk {
    @Unique
    private LevelChunkTicks<Block> leavesbegone$randomBlockTicks = new LevelChunkTicks<>();

    public LevelChunkMixin(ChunkPos chunkPos, UpgradeData upgradeData, LevelHeightAccessor levelHeightAccessor, PalettedContainerFactory containerFactory, long inhabitedTime, LevelChunkSection @Nullable [] sections, @Nullable BlendingData blendingData) {
        super(chunkPos, upgradeData, levelHeightAccessor, containerFactory, inhabitedTime, sections, blendingData);
    }

    @Override
    public LevelChunkTicks<Block> leavesbegone$getRandomBlockTicks() {
        Objects.requireNonNull(this.leavesbegone$randomBlockTicks, "random block ticks is null");
        return this.leavesbegone$randomBlockTicks;
    }

    @Override
    public void leavesbegone$setRandomBlockTicks(LevelChunkTicks<Block> randomBlockTicks) {
        Objects.requireNonNull(randomBlockTicks, "random block ticks is null");
        this.leavesbegone$randomBlockTicks = randomBlockTicks;
    }

    @Inject(method = "unpackTicks", at = @At("TAIL"))
    public void unpackTicks(long currentTick, CallbackInfo callback) {
        this.leavesbegone$getRandomBlockTicks().unpack(currentTick);
    }

    @Inject(method = "registerTickContainerInLevel", at = @At("TAIL"))
    public void registerTickContainerInLevel(ServerLevel level, CallbackInfo callback) {
        if (!(level instanceof RandomBlockTickerLevel tickerLevel)) {
            return;
        }

        tickerLevel.leavesbegone$getRandomBlockTicks()
                .addContainer(this.chunkPos, this.leavesbegone$getRandomBlockTicks());
    }

    @Inject(method = "unregisterTickContainerFromLevel", at = @At("TAIL"))
    public void unregisterTickContainerFromLevel(ServerLevel level, CallbackInfo callback) {
        if (!(level instanceof RandomBlockTickerLevel tickerLevel)) {
            return;
        }

        tickerLevel.leavesbegone$getRandomBlockTicks().removeContainer(this.chunkPos);
    }

    @ModifyReturnValue(method = "getTicksForSerialization", at = @At("TAIL"))
    public ChunkAccess.PackedTicks getTicksForSerialization(ChunkAccess.PackedTicks packedTicks, long currentTick) {
        RandomBlockTickerPackedTicks.class.cast(packedTicks)
                .leavesbegone$setRandomBlocks(this.leavesbegone$getRandomBlockTicks().pack(currentTick));
        return packedTicks;
    }
}
