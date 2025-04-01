package fuzs.leavesbegone.helper;

import com.mojang.serialization.Codec;
import fuzs.leavesbegone.init.ModRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.ticks.LevelChunkTicks;
import net.minecraft.world.ticks.SavedTick;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class PackedTicksHelper {
    /**
     * Copied from {@link net.minecraft.world.level.chunk.storage.SerializableChunkData#BLOCK_TICKS_CODEC}.
     */
    private static final Codec<List<SavedTick<Block>>> BLOCK_TICKS_CODEC = SavedTick.codec(BuiltInRegistries.BLOCK.byNameCodec())
            .listOf();
    public static final Codec<ChunkAccess.PackedTicks> CODEC = BLOCK_TICKS_CODEC.xmap((List<SavedTick<Block>> blocks) -> new ChunkAccess.PackedTicks(
            blocks,
            Collections.emptyList()), ChunkAccess.PackedTicks::blocks);
    /**
     * Backwards compatible with old capability format, so that remaining data can properly be read and removed.
     */
    @Deprecated
    public static final Codec<ChunkAccess.PackedTicks> LENIENT_CODEC = CODEC.orElse(new ChunkAccess.PackedTicks(
            Collections.emptyList(),
            Collections.emptyList()));

    private PackedTicksHelper() {
        // NO-OP
    }

    public static ChunkAccess.PackedTicks getTicksForSerialization(long gametime, LevelChunkTicks<Block> levelChunkTicks) {
        Objects.requireNonNull(levelChunkTicks, "level chunk ticks is null");
        return new ChunkAccess.PackedTicks(levelChunkTicks.pack(gametime), Collections.emptyList());
    }

    public static LevelChunkTicks<Block> instantiate(ChunkAccess.PackedTicks packedTicks) {
        Objects.requireNonNull(packedTicks, "packed ticks is null");
        return new LevelChunkTicks<>(packedTicks.blocks());
    }

    public static void saveTickContainerFromLevel(LevelChunk levelChunk, LevelChunkTicks<Block> randomBlockTicks) {
        if (randomBlockTicks.count() != 0L) {
            ChunkAccess.PackedTicks packedTicks = getTicksForSerialization(levelChunk.getLevel().getGameTime(),
                    randomBlockTicks);
            ModRegistry.RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE.set(levelChunk, packedTicks);
        } else {
            ModRegistry.RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE.set(levelChunk, null);
        }
    }

    @Nullable
    public static LevelChunkTicks<Block> loadTickContainerInLevel(LevelChunk levelChunk) {
        if (ModRegistry.RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE.has(levelChunk)) {
            LevelChunkTicks<Block> randomBlockTicks = instantiate(ModRegistry.RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE.get(
                    levelChunk));
            ModRegistry.RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE.set(levelChunk, null);
            return randomBlockTicks;
        } else {
            return null;
        }
    }
}
