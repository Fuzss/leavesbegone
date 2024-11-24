package fuzs.leavesbegone.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.config.ServerConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelAccessor;
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

    @WrapOperation(method = "updateShape", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/LeavesBlock;getDistanceAt(Lnet/minecraft/world/level/block/state/BlockState;)I"))
    public int updateShape(BlockState neighborState, Operation<Integer> operation) {
        int distance = operation.call(neighborState);
        if (!LeavesBeGone.CONFIG.get(ServerConfig.class).ignoreOtherLeaveTypes) return distance;
        return distance != 7 && neighborState.is(BlockTags.LEAVES) && !neighborState.is(this) ? 7 : distance;
    }

    @WrapOperation(method = "updateDistance", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/LeavesBlock;getDistanceAt(Lnet/minecraft/world/level/block/state/BlockState;)I"))
    private static int updateDistance(BlockState neighborState, Operation<Integer> operation, BlockState blockState, LevelAccessor level, BlockPos pos) {
        int distance = operation.call(neighborState);
        if (!LeavesBeGone.CONFIG.get(ServerConfig.class).ignoreOtherLeaveTypes) return distance;
        return distance != 7 && neighborState.is(BlockTags.LEAVES) && !blockState.is(neighborState.getBlock()) ? 7 : distance;
    }
}
