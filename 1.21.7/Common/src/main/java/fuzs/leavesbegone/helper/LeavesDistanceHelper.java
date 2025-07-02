package fuzs.leavesbegone.helper;

import fuzs.leavesbegone.LeavesBeGone;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public final class LeavesDistanceHelper {

    private LeavesDistanceHelper() {
        // NO-OP
    }

    public static int updateDistance(BlockState blockState, BlockState neighborState, int distanceAt) {
        return mustResetDistance(blockState, neighborState, distanceAt) ? 7 : distanceAt;
    }

    private static boolean mustResetDistance(BlockState blockState, BlockState neighborState, int distanceAt) {
        // the way this is implemented our leaves block tags are optional,
        // only trees consisting of multiple different leaves blocks do require them
        if (distanceAt == 7) {
            return false;
        } else if (!neighborState.is(BlockTags.LEAVES)) {
            return false;
        } else if (neighborState.is(blockState.getBlock())) {
            return false;
        } else if (blockState.is(createBlockTag(neighborState.getBlock()))) {
            return false;
        } else if (neighborState.is(createBlockTag(blockState.getBlock()))) {
            return false;
        } else {
            return true;
        }
    }

    public static TagKey<Block> createBlockTag(Block block) {
        ResourceLocation resourceLocation = block.builtInRegistryHolder().key().location();
        return TagKey.create(Registries.BLOCK,
                LeavesBeGone.id(resourceLocation.getNamespace() + "/" + resourceLocation.getPath()));
    }
}
