package fuzs.leavesbegone.init;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.attachment.SerializableLevelChunkTicks;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import net.minecraft.world.level.chunk.LevelChunk;

public class ModRegistry {
    public static final DataAttachmentType<LevelChunk, SerializableLevelChunkTicks> RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE = DataAttachmentRegistry.<SerializableLevelChunkTicks>levelChunkBuilder()
            .persistent(SerializableLevelChunkTicks.LENIENT_CODEC)
            .build(LeavesBeGone.id("random_block_ticks"));

    public static void bootstrap() {
        // NO-OP
    }
}
