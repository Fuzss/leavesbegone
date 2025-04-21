package fuzs.leavesbegone.helper;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.ticks.SavedTick;
import net.minecraft.world.ticks.TickPriority;

import java.util.List;

/**
 * Copied from Minecraft 1.21.5.
 */
public class SavedTickCodecs {
    public static final Codec<TickPriority> TICK_PRIORITY_CODEC = Codec.INT.xmap(TickPriority::byValue,
            TickPriority::getValue);
    public static final Codec<List<SavedTick<Block>>> BLOCK_TICKS_CODEC = savedTickCodec(BuiltInRegistries.BLOCK.byNameCodec()).listOf();

    public static <T> Codec<SavedTick<T>> savedTickCodec(Codec<T> codec) {
        MapCodec<BlockPos> mapCodec = RecordCodecBuilder.mapCodec(instance -> instance.group(Codec.INT.fieldOf("x")
                        .forGetter(Vec3i::getX),
                Codec.INT.fieldOf("y").forGetter(Vec3i::getY),
                Codec.INT.fieldOf("z").forGetter(Vec3i::getZ)).apply(instance, BlockPos::new));
        return RecordCodecBuilder.create(instance -> instance.group(codec.fieldOf("i").forGetter(SavedTick::type),
                mapCodec.forGetter(SavedTick::pos),
                Codec.INT.fieldOf("t").forGetter(SavedTick::delay),
                TICK_PRIORITY_CODEC.fieldOf("p").forGetter(SavedTick::priority)).apply(instance, SavedTick::new));
    }

    public static <T> List<SavedTick<T>> filterTickListForChunk(List<SavedTick<T>> list, ChunkPos chunkPos) {
        long l = chunkPos.toLong();
        return list.stream().filter(savedTick -> ChunkPos.asLong(savedTick.pos()) == l).toList();
    }
}
