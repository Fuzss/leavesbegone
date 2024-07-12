package fuzs.leavesbegone.mixin;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.world.level.chunk.RandomBlockTickerChunk;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.ticks.LevelChunkTicks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ChunkSerializer.class)
abstract class ChunkSerializerMixin {

    @ModifyVariable(method = "read", at = @At(value = "STORE", ordinal = 0))
    private static ChunkAccess leavesbegone$read(ChunkAccess chunkAccess, ServerLevel level, PoiManager poiManager, ChunkPos pos, CompoundTag tag) {
        if (!(chunkAccess instanceof RandomBlockTickerChunk randomBlockTickerChunk)) return chunkAccess;
        LevelChunkTicks<Block> levelchunkticks = LevelChunkTicks.load(tag.getList(LeavesBeGone.id("random_block_ticks").toString(), Tag.TAG_COMPOUND), (p_188287_) -> {
            return BuiltInRegistries.BLOCK.getOptional(ResourceLocation.tryParse(p_188287_));
        }, pos);
        randomBlockTickerChunk.leavesbegone$setRandomBlockTicks(levelchunkticks);
        return chunkAccess;
    }

    @Inject(method = "write", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/chunk/storage/ChunkSerializer;saveTicks(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/level/chunk/ChunkAccess$TicksToSave;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void leavesbegone$write(ServerLevel level, ChunkAccess chunk, CallbackInfoReturnable<CompoundTag> callback, ChunkPos chunkpos, CompoundTag compoundtag) {
        if (!(chunk instanceof RandomBlockTickerChunk randomBlockTickerChunk)) return;
        long i = level.getLevelData().getGameTime();
        compoundtag.put(LeavesBeGone.id("random_block_ticks").toString(), randomBlockTickerChunk.leavesbegone$getRandomBlockTicks().save(i, (p_196894_) -> {
            return BuiltInRegistries.BLOCK.getKey(p_196894_).toString();
        }));
    }
}
