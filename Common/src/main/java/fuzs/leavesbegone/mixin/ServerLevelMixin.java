package fuzs.leavesbegone.mixin;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.config.ServerConfig;
import fuzs.leavesbegone.server.level.RandomBlockTickerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraft.world.ticks.LevelTicks;
import net.minecraft.world.ticks.ScheduledTick;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
abstract class ServerLevelMixin extends Level implements RandomBlockTickerLevel {
    @Unique
    private final LevelTicks<Block> leavesbegone$randomBlockTicks = new LevelTicks<>(this::isPositionTickingWithEntitiesLoaded, this.getProfilerSupplier());
    @Unique
    private boolean leavesbegone$leavesWereNotRandomlyTicking;

    protected ServerLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, Holder<DimensionType> holder, Supplier<ProfilerFiller> supplier, boolean bl, boolean bl2, long l, int i) {
        super(writableLevelData, resourceKey, holder, supplier, bl, bl2, l, i);
    }

    @Override
    public LevelTicks<Block> leavesbegone$getRandomBlockTicks() {
        return this.leavesbegone$randomBlockTicks;
    }

    @Shadow
    private boolean isPositionTickingWithEntitiesLoaded(long l) {
        throw new IllegalStateException();
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/ticks/LevelTicks;tick(JILjava/util/function/BiConsumer;)V", shift = At.Shift.AFTER, ordinal = 0))
    public void leavesbegone$tick(BooleanSupplier pHasTimeLeft, CallbackInfo callback) {
        this.leavesbegone$randomBlockTicks.tick(this.getGameTime(), 65536, (BlockPos pos, Block block) -> {
            BlockState blockstate = this.getBlockState(pos);
            if (blockstate.is(block)) {
                blockstate.randomTick((ServerLevel) (Object) this, pos, this.random);
            }
        });
    }

    @Inject(method = "tickBlock", at = @At("HEAD"))
    private void leavesbegone$tickBlock$0(BlockPos pos, Block block, CallbackInfo callback) {
        BlockState state = this.getBlockState(pos);
        this.leavesbegone$leavesWereNotRandomlyTicking = state.is(block) && state.is(BlockTags.LEAVES) && !state.isRandomlyTicking();
    }

    @Inject(method = "tickBlock", at = @At("TAIL"))
    private void leavesbegone$tickBlock$1(BlockPos pos, Block block, CallbackInfo callback) {
        // only schedule when this has changed
        if (this.leavesbegone$leavesWereNotRandomlyTicking && this.getBlockState(pos).isRandomlyTicking()) {
            int minimumDecayTicks = LeavesBeGone.CONFIG.get(ServerConfig.class).minimumDecayTicks;
            int maximumDecayTicks = LeavesBeGone.CONFIG.get(ServerConfig.class).maximumDecayTicks;
            int delay = minimumDecayTicks + this.random.nextInt(Math.max(1, maximumDecayTicks - minimumDecayTicks));
            long gameTime = this.getLevelData().getGameTime();
            this.leavesbegone$getRandomBlockTicks().schedule(new ScheduledTick<>(block, pos, gameTime + delay, this.nextSubTickCount()));
        }
    }
}
