package fuzs.leavesbegone.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LeavesBlock.class)
abstract class LeavesBlockMixin extends Block {

    public LeavesBlockMixin(Properties properties) {
        super(properties);
    }

    @ModifyExpressionValue(
            method = "updateShape", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/LeavesBlock;getDistanceAt(Lnet/minecraft/world/level/block/state/BlockState;)I"
    )
    )
    public int updateShape(int distanceAt, BlockState blockState, LevelReader levelReader, ScheduledTickAccess scheduledTickAccess, BlockPos blockPos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource randomSource) {
        if (!LeavesBeGone.CONFIG.get(ServerConfig.class).ignoreOtherLeaveTypes) return distanceAt;
        return distanceAt != 7 && neighborState.is(BlockTags.LEAVES) && !neighborState.is(this) ? 7 : distanceAt;
    }

    @ModifyExpressionValue(
            method = "updateDistance", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/LeavesBlock;getDistanceAt(Lnet/minecraft/world/level/block/state/BlockState;)I"
    )
    )
    private static int updateDistance(int distanceAt, BlockState blockState, LevelAccessor level, BlockPos pos, @Local BlockPos.MutableBlockPos mutableBlockPos) {
        if (!LeavesBeGone.CONFIG.get(ServerConfig.class).ignoreOtherLeaveTypes) return distanceAt;
        BlockState neighborState = level.getBlockState(mutableBlockPos);
        return distanceAt != 7 && neighborState.is(BlockTags.LEAVES) && !blockState.is(neighborState.getBlock()) ? 7 :
                distanceAt;
    }
}
