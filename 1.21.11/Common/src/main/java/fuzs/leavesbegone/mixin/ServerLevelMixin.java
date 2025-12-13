package fuzs.leavesbegone.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.config.ServerConfig;
import fuzs.leavesbegone.server.level.RandomBlockTickerLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
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

@Mixin(ServerLevel.class)
abstract class ServerLevelMixin extends Level implements RandomBlockTickerLevel {
    @Unique
    private final LevelTicks<Block> leavesbegone$randomBlockTicks = new LevelTicks<>(this::isPositionTickingWithEntitiesLoaded);

    protected ServerLevelMixin(WritableLevelData writableLevelData, ResourceKey<Level> resourceKey, RegistryAccess registryAccess, Holder<DimensionType> holder, boolean bl, boolean bl2, long l, int i) {
        super(writableLevelData, resourceKey, registryAccess, holder, bl, bl2, l, i);
    }

    @Override
    public LevelTicks<Block> leavesbegone$getRandomBlockTicks() {
        return this.leavesbegone$randomBlockTicks;
    }

    @Shadow
    private boolean isPositionTickingWithEntitiesLoaded(long l) {
        throw new RuntimeException();
    }

    @Inject(
            method = "tick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/ticks/LevelTicks;tick(JILjava/util/function/BiConsumer;)V",
            shift = At.Shift.AFTER,
            ordinal = 0
    )
    )
    public void tick(BooleanSupplier hasTimeLeft, CallbackInfo callback) {
        this.leavesbegone$getRandomBlockTicks().tick(this.getGameTime(), 65536, (BlockPos blockPos, Block block) -> {
            BlockState blockState = this.getBlockState(blockPos);
            if (blockState.is(block)) {
                blockState.randomTick(ServerLevel.class.cast(this), blockPos, this.random);
            }
        });
    }

    @Inject(method = "tickBlock", at = @At("HEAD"))
    private void tickBlock$0(BlockPos pos, Block block, CallbackInfo callback, @Share("leavesWereNotRandomlyTicking") LocalBooleanRef leavesWereNotRandomlyTickingRef) {
        BlockState blockState = this.getBlockState(pos);
        leavesWereNotRandomlyTickingRef.set(
                blockState.is(block) && blockState.is(BlockTags.LEAVES) && !blockState.isRandomlyTicking());
    }

    @Inject(method = "tickBlock", at = @At("TAIL"))
    private void tickBlock$1(BlockPos pos, Block block, CallbackInfo callback, @Share("leavesWereNotRandomlyTicking") LocalBooleanRef leavesWereNotRandomlyTickingRef) {
        // only schedule when this has changed
        if (leavesWereNotRandomlyTickingRef.get() && this.getBlockState(pos).isRandomlyTicking()) {
            int minimumDecayTicks = LeavesBeGone.CONFIG.get(ServerConfig.class).minimumDecayTicks;
            int maximumDecayTicks = LeavesBeGone.CONFIG.get(ServerConfig.class).maximumDecayTicks;
            int delay = minimumDecayTicks + this.random.nextInt(Math.max(1, maximumDecayTicks - minimumDecayTicks));
            long gameTime = this.getLevelData().getGameTime();
            this.leavesbegone$getRandomBlockTicks()
                    .schedule(new ScheduledTick<>(block, pos, gameTime + delay, this.nextSubTickCount()));
        }
    }
}
