package fuzs.leavesbegone.mixin;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.config.ServerConfig;
import fuzs.leavesbegone.server.level.RandomBlockTickerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.ticks.ScheduledTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
abstract class LeavesBlockMixin extends Block {

    public LeavesBlockMixin(Properties properties) {
        super(properties);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void leavesbegone$tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo callback) {
        if (!(level instanceof RandomBlockTickerLevel tickerLevel)) return;
        BlockState newState = level.getBlockState(pos);
        // only schedule when this has changed
        if (newState.isRandomlyTicking() && !state.isRandomlyTicking()) {
            long gameTime = level.getLevelData().getGameTime();
            int minimumDecayTicks = LeavesBeGone.CONFIG.get(ServerConfig.class).minimumDecayTicks;
            int maximumDecayTicks = LeavesBeGone.CONFIG.get(ServerConfig.class).maximumDecayTicks;
            int delay = minimumDecayTicks + random.nextInt(Math.max(1, maximumDecayTicks - minimumDecayTicks));
            tickerLevel.leavesbegone$getRandomBlockTicks().schedule(new ScheduledTick<>(newState.getBlock(), pos, gameTime + delay, level.nextSubTickCount()));
        }
    }
}
