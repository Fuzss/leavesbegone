package fuzs.leavesbegone.capability;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.puzzleslib.api.capability.v3.data.CapabilityComponent;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.ticks.LevelChunkTicks;

import java.util.Objects;

/**
 * Aggressive null checking is done as {@code null} values sometimes causes the game to hang without revealing anything
 * in the log or producing a proper crash-report.
 */
public class RandomBlockTicksCapability extends CapabilityComponent<LevelChunk> {
    static final String KEY_CHUNK_POS = LeavesBeGone.id("chunk_pos").toString();
    static final String KEY_RANDOM_BLOCK_TICKS = LeavesBeGone.id("random_block_ticks").toString();

    private LevelChunkTicks<Block> randomBlockTicks = new LevelChunkTicks<>();

    public LevelChunkTicks<Block> getRandomBlockTicks() {
        Objects.requireNonNull(this.randomBlockTicks, "random block ticks is null");
        return this.randomBlockTicks;
    }

    public void setRandomBlockTicks(LevelChunkTicks<Block> randomBlockTicks) {
        Objects.requireNonNull(randomBlockTicks, "random block ticks is null");
        this.randomBlockTicks = randomBlockTicks;
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
        LevelChunkTicks<Block> ticks = LevelChunkTicks.load(compoundTag.getList(KEY_RANDOM_BLOCK_TICKS,
                Tag.TAG_COMPOUND), (String s) -> {
            return BuiltInRegistries.BLOCK.getOptional(ResourceLocation.tryParse(s));
        }, new ChunkPos(compoundTag.getLong(KEY_CHUNK_POS)));
        this.setRandomBlockTicks(ticks);
    }
}
