package fuzs.leavesbegone.mixin;

import fuzs.leavesbegone.server.level.RandomBlockTickerLevel;
import fuzs.leavesbegone.world.level.chunk.RandomBlockTickerChunk;
import net.minecraft.core.Registry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.*;
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
 * aggressive null checking is done as a null values sometimes causes the game to hang without revealing anything in the log or a crash-report
 */
@Mixin(LevelChunk.class)
abstract class LevelChunkMixin extends ChunkAccess implements RandomBlockTickerChunk {
    @Unique
    private LevelChunkTicks<Block> leavesbegone$randomBlockTicks;

    public LevelChunkMixin(ChunkPos chunkPos, UpgradeData upgradeData, LevelHeightAccessor levelHeightAccessor, Registry<Biome> registry, long l, @Nullable LevelChunkSection[] levelChunkSections, @Nullable BlendingData blendingData) {
        super(chunkPos, upgradeData, levelHeightAccessor, registry, l, levelChunkSections, blendingData);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/level/ChunkPos;)V", at = @At("TAIL"))
    public void leavesbegone$init(Level level, ChunkPos chunkPos, CallbackInfo callback) {
        this.leavesbegone$randomBlockTicks = new LevelChunkTicks<>();
    }

    @Inject(method = "<init>(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ProtoChunk;Lnet/minecraft/world/level/chunk/LevelChunk$PostLoadProcessor;)V", at = @At("TAIL"))
    public void leavesbegone$init(ServerLevel serverLevel, ProtoChunk protoChunk, @Nullable LevelChunk.PostLoadProcessor postLoadProcessor, CallbackInfo callback) {
        this.leavesbegone$randomBlockTicks = new LevelChunkTicks<>();
    }

    @Override
    public LevelChunkTicks<Block> leavesbegone$getRandomBlockTicks() {
        Objects.requireNonNull(this.leavesbegone$randomBlockTicks, "Random block ticks was null");
        return this.leavesbegone$randomBlockTicks;
    }

    @Override
    public void leavesbegone$setRandomBlockTicks(LevelChunkTicks<Block> randomBlockTicks) {
        Objects.requireNonNull(randomBlockTicks, "Random block ticks was null");
        this.leavesbegone$randomBlockTicks = randomBlockTicks;
    }

    @Inject(method = "unpackTicks", at = @At("TAIL"))
    public void leavesbegone$unpackTicks(long pos, CallbackInfo callback) {
        Objects.requireNonNull(this.leavesbegone$randomBlockTicks, "Random block ticks was null");
        this.leavesbegone$randomBlockTicks.unpack(pos);
    }

    @Inject(method = "registerTickContainerInLevel", at = @At("TAIL"))
    public void leavesbegone$registerTickContainerInLevel(ServerLevel level, CallbackInfo callback) {
        if (!(level instanceof RandomBlockTickerLevel randomBlockTickerLevel)) return;
        Objects.requireNonNull(this.leavesbegone$randomBlockTicks, "Random block ticks was null");
        randomBlockTickerLevel.leavesbegone$getRandomBlockTicks().addContainer(this.chunkPos, this.leavesbegone$randomBlockTicks);
    }

    @Inject(method = "unregisterTickContainerFromLevel", at = @At("TAIL"))
    public void leavesbegone$unregisterTickContainerFromLevel$Inject$Tail(ServerLevel level, CallbackInfo callback) {
        if (!(level instanceof RandomBlockTickerLevel randomBlockTickerLevel)) return;
        randomBlockTickerLevel.leavesbegone$getRandomBlockTicks().removeContainer(this.chunkPos);
    }
}
