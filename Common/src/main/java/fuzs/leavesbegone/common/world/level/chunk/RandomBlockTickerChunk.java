package fuzs.leavesbegone.common.world.level.chunk;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.ticks.LevelChunkTicks;

public interface RandomBlockTickerChunk {

    LevelChunkTicks<Block> leavesbegone$getRandomBlockTicks();

    void leavesbegone$setRandomBlockTicks(LevelChunkTicks<Block> randomBlockTicks);
}
