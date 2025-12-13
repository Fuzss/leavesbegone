package fuzs.leavesbegone.helper;

import fuzs.leavesbegone.LeavesBeGone;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
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
        Identifier identifier = BuiltInRegistries.BLOCK.wrapAsHolder(block)
                .unwrapKey()
                .map(ResourceKey::identifier)
                .orElseThrow();
        return TagKey.create(Registries.BLOCK, LeavesBeGone.id(identifier.getNamespace() + "/" + identifier.getPath()));
    }
}
