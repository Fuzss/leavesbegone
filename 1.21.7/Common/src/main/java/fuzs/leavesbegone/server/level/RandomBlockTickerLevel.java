package fuzs.leavesbegone.server.level;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.ticks.LevelTicks;

public interface RandomBlockTickerLevel {

    LevelTicks<Block> leavesbegone$getRandomBlockTicks();
}
