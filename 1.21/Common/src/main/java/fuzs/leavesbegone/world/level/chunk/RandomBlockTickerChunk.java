package fuzs.leavesbegone.world.level.chunk;

import fuzs.leavesbegone.LeavesBeGone;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.ticks.LevelChunkTicks;

public interface RandomBlockTickerChunk {
    String KEY_RANDOM_BLOCK_TICKS = LeavesBeGone.id("random_block_ticks").toString();

    LevelChunkTicks<Block> leavesbegone$getRandomBlockTicks();

    void leavesbegone$setRandomBlockTicks(LevelChunkTicks<Block> randomBlockTicks);
}
