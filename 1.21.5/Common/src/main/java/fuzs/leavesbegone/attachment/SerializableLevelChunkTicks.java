package fuzs.leavesbegone.attachment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.init.ModRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.ticks.LevelChunkTicks;
import org.jetbrains.annotations.Nullable;

public record SerializableLevelChunkTicks(ListTag listTag, ChunkPos chunkPos) {
    /**
     * Adapted from {@link net.minecraft.nbt.CompoundTag#CODEC}.
     */
    public static final Codec<ListTag> LIST_TAG_CODEC = Codec.PASSTHROUGH.comapFlatMap((Dynamic<?> dynamic) -> {
        Tag tag = dynamic.convert(NbtOps.INSTANCE).getValue();
        return tag instanceof ListTag listTag ?
                DataResult.success(listTag == dynamic.getValue() ? listTag.copy() : listTag) :
                DataResult.error(() -> "Not a list tag: " + tag);
    }, (ListTag listTag) -> new Dynamic<>(NbtOps.INSTANCE, listTag.copy()));
    public static final Codec<SerializableLevelChunkTicks> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group(LIST_TAG_CODEC.fieldOf("random_block_ticks")
                                .forGetter(SerializableLevelChunkTicks::listTag),
                        ChunkPos.CODEC.fieldOf("chunk_pos").forGetter(SerializableLevelChunkTicks::chunkPos))
                .apply(instance, SerializableLevelChunkTicks::new);
    });
    @Deprecated
    public static final Codec<SerializableLevelChunkTicks> LEGACY_CODEC = decodeOnly(TagParser.AS_CODEC.map((CompoundTag compoundTag) -> {
        return new SerializableLevelChunkTicks(compoundTag.getList(LeavesBeGone.id("random_block_ticks").toString(),
                Tag.TAG_COMPOUND), new ChunkPos(compoundTag.getLong(LeavesBeGone.id("chunk_pos").toString())));
    }));
    @Deprecated
    public static final Codec<SerializableLevelChunkTicks> LENIENT_CODEC = Codec.withAlternative(CODEC, LEGACY_CODEC);

    /**
     * Creates a codec from a decoder. The returned codec can only decode, and will throw on any attempt to encode.
     * <p>
     * Copied from {@code net.neoforged.neoforge.common.util.NeoForgeExtraCodecs#decodeOnly}.
     */
    @Deprecated
    public static <A> Codec<A> decodeOnly(Decoder<A> decoder) {
        return Codec.of(Codec.unit(() -> {
            throw new UnsupportedOperationException("Cannot encode with decode-only codec! Decoder:" + decoder);
        }), decoder, "DecodeOnly[" + decoder + "]");
    }

    public static <T> SerializableLevelChunkTicks save(LevelChunk levelChunk, LevelChunkTicks<T> levelChunkTicks, Registry<T> registry) {
        long gameTime = levelChunk.getLevel().getLevelData().getGameTime();
        return new SerializableLevelChunkTicks(levelChunkTicks.save(gameTime, (T t) -> {
            return registry.getKey(t).toString();
        }), levelChunk.getPos());
    }

    public <T> LevelChunkTicks<T> load(Registry<T> registry) {
        return LevelChunkTicks.load(this.listTag, (String s) -> {
            return registry.getOptional(ResourceLocation.tryParse(s));
        }, this.chunkPos);
    }

    public static void saveTickContainerFromLevel(LevelChunk levelChunk, LevelChunkTicks<Block> randomBlockTicks) {
        if (randomBlockTicks.count() != 0L) {
            SerializableLevelChunkTicks serializableLevelChunkTicks = save(levelChunk,
                    randomBlockTicks,
                    BuiltInRegistries.BLOCK);
            ModRegistry.RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE.set(levelChunk, serializableLevelChunkTicks);
        } else {
            ModRegistry.RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE.set(levelChunk, null);
        }
    }

    @Nullable
    public static LevelChunkTicks<Block> loadTickContainerInLevel(LevelChunk levelChunk) {
        if (ModRegistry.RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE.has(levelChunk)) {
            return ModRegistry.RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE.get(levelChunk).load(BuiltInRegistries.BLOCK);
        } else {
            return null;
        }
    }
}
