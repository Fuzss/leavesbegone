package fuzs.leavesbegone.mixin;

import fuzs.leavesbegone.capability.RandomBlockTicksCapability;
import fuzs.leavesbegone.init.ModRegistry;
import fuzs.leavesbegone.server.level.RandomBlockTickerLevel;
import fuzs.leavesbegone.world.level.chunk.RandomBlockTickerChunk;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.UpgradeData;
import net.minecraft.world.level.levelgen.blending.BlendingData;
import net.minecraft.world.ticks.LevelChunkTicks;
import org.jetbrains.annotations.Nullable;
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

    public LevelChunkMixin(ChunkPos chunkPos, UpgradeData upgradeData, LevelHeightAccessor levelHeightAccessor, Registry<Biome> registry, long l, @Nullable LevelChunkSection[] levelChunkSections, @Nullable BlendingData blendingData) {
        super(chunkPos, upgradeData, levelHeightAccessor, registry, l, levelChunkSections, blendingData);
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
    public void unpackTicks(long pos, CallbackInfo callback) {
        ModRegistry.RANDOM_BLOCK_TICKS_CAPABILITY.getIfProvided(this)
                .map(RandomBlockTicksCapability::getRandomBlockTicks)
                .ifPresent((LevelChunkTicks<Block> ticks) -> {
                    ticks.unpack(pos);
                });
    }

    @Inject(method = "registerTickContainerInLevel", at = @At("TAIL"))
    public void registerTickContainerInLevel(ServerLevel serverLevel, CallbackInfo callback) {
        if (!(serverLevel instanceof RandomBlockTickerLevel level)) return;
        ModRegistry.RANDOM_BLOCK_TICKS_CAPABILITY.getIfProvided(this)
                .map(RandomBlockTicksCapability::getRandomBlockTicks)
                .ifPresent((LevelChunkTicks<Block> ticks) -> {
                    level.leavesbegone$getRandomBlockTicks().addContainer(this.chunkPos, ticks);
                });
    }

    @Inject(method = "unregisterTickContainerFromLevel", at = @At("TAIL"))
    public void unregisterTickContainerFromLevel(ServerLevel serverLevel, CallbackInfo callback) {
        if (!(serverLevel instanceof RandomBlockTickerLevel level)) return;
        ModRegistry.RANDOM_BLOCK_TICKS_CAPABILITY.getIfProvided(this)
                .map(RandomBlockTicksCapability::getRandomBlockTicks)
                .ifPresent((LevelChunkTicks<Block> ticks) -> {
                    level.leavesbegone$getRandomBlockTicks().removeContainer(this.chunkPos);
                });
    }
}
