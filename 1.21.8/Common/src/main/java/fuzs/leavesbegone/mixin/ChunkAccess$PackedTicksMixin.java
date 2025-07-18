package fuzs.leavesbegone.mixin;

import fuzs.leavesbegone.world.level.chunk.RandomBlockTickerPackedTicks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.ticks.SavedTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Mixin(ChunkAccess.PackedTicks.class)
abstract class ChunkAccess$PackedTicksMixin implements RandomBlockTickerPackedTicks {
    @Unique
    private List<SavedTick<Block>> leavesbegone$randomBlocks;

    @Override
    public List<SavedTick<Block>> leavesbegone$getRandomBlocks() {
        return this.leavesbegone$randomBlocks;
    }

    @Override
    public void leavesbegone$setRandomBlocks(List<SavedTick<Block>> randomBlocks) {
        this.leavesbegone$randomBlocks = randomBlocks;
    }
}
