package fuzs.leavesbegone.capability;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.world.level.chunk.RandomBlockTickerChunk;
import fuzs.puzzleslib.api.capability.v3.data.CapabilityComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.ticks.LevelChunkTicks;
import org.jetbrains.annotations.Nullable;

/**
 * Chunk ticks are stored on the chunk itself, so we can freely destroy the capability to prevent it from being written
 * to chunk data when empty without losing the chunk ticks instance added to the level in
 * {@link LevelChunk#registerTickContainerInLevel(ServerLevel)}.
 */
public class RandomBlockTicksCapability extends CapabilityComponent<LevelChunk> {
    static final String KEY_CHUNK_POS = LeavesBeGone.id("chunk_pos").toString();
    static final String KEY_RANDOM_BLOCK_TICKS = LeavesBeGone.id("random_block_ticks").toString();

    @Nullable
    private LevelChunkTicks<Block> randomBlockTicks;

    public LevelChunkTicks<Block> getRandomBlockTicks() {
        LevelChunkTicks<Block> randomBlockTicks = this.randomBlockTicks;
        if (randomBlockTicks != null) {
            this.setRandomBlockTicks(randomBlockTicks);
            this.randomBlockTicks = null;
            return randomBlockTicks;
        } else {
            return ((RandomBlockTickerChunk) this.getHolder()).leavesbegone$getRandomBlockTicks();
        }
    }

    private void setRandomBlockTicks(LevelChunkTicks<Block> randomBlockTicks) {
        ((RandomBlockTickerChunk) this.getHolder()).leavesbegone$setRandomBlockTicks(randomBlockTicks);
    }

    @Override
    public void write(CompoundTag compoundTag, HolderLookup.Provider registries) {
        compoundTag.putLong(KEY_CHUNK_POS, this.getHolder().getPos().toLong());
        long gameTime = this.getHolder().getLevel().getLevelData().getGameTime();
        compoundTag.put(KEY_RANDOM_BLOCK_TICKS, this.getRandomBlockTicks().save(gameTime, (Block block) -> {
            return BuiltInRegistries.BLOCK.getKey(block).toString();
        }));
    }

    @Override
    public void read(CompoundTag compoundTag, HolderLookup.Provider registries) {
        LevelChunkTicks<Block> randomBlockTicks = LevelChunkTicks.load(compoundTag.getList(KEY_RANDOM_BLOCK_TICKS,
                Tag.TAG_COMPOUND), (String s) -> {
            return BuiltInRegistries.BLOCK.getOptional(ResourceLocation.tryParse(s));
        }, new ChunkPos(compoundTag.getLong(KEY_CHUNK_POS)));
        if (randomBlockTicks.count() != 0L) {
            this.randomBlockTicks = randomBlockTicks;
        }
    }
}
