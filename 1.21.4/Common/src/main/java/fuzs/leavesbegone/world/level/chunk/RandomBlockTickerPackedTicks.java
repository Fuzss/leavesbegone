package fuzs.leavesbegone.world.level.chunk;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.ticks.SavedTick;

import java.util.List;

public interface RandomBlockTickerPackedTicks {

    List<SavedTick<Block>> leavesbegone$getRandomBlocks();

    void leavesbegone$setRandomBlocks(List<SavedTick<Block>> randomBlocks);
}
