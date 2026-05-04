package fuzs.leavesbegone.common.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import fuzs.leavesbegone.common.LeavesBeGone;
import fuzs.leavesbegone.common.config.ServerConfig;
import fuzs.leavesbegone.common.helper.LeavesDistanceHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

    @ModifyExpressionValue(method = "updateShape",
                           at = @At(value = "INVOKE",
                                    target = "Lnet/minecraft/world/level/block/LeavesBlock;getDistanceAt(Lnet/minecraft/world/level/block/state/BlockState;)I"))
    public int updateShape(int distanceAt, BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (!LeavesBeGone.CONFIG.get(ServerConfig.class).ignoreOtherLeaveTypes) {
            return distanceAt;
        }

        return LeavesDistanceHelper.updateDistance(state, neighbourState, distanceAt);
    }

    @ModifyExpressionValue(method = "updateDistance",
                           at = @At(value = "INVOKE",
                                    target = "Lnet/minecraft/world/level/block/LeavesBlock;getDistanceAt(Lnet/minecraft/world/level/block/state/BlockState;)I"))
    private static int updateDistance(int distanceAt, BlockState state, LevelAccessor level, BlockPos pos, @Local BlockPos.MutableBlockPos neighborPos) {
        if (!LeavesBeGone.CONFIG.get(ServerConfig.class).ignoreOtherLeaveTypes) {
            return distanceAt;
        }

        return LeavesDistanceHelper.updateDistance(state, level.getBlockState(neighborPos), distanceAt);
    }
}
