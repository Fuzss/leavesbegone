package fuzs.leavesbegone.init;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.helper.PackedTicksHelper;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

public class ModRegistry {
    public static final DataAttachmentType<LevelChunk, ChunkAccess.PackedTicks> RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE = DataAttachmentRegistry.<ChunkAccess.PackedTicks>levelChunkBuilder()
            .persistent(PackedTicksHelper.LENIENT_CODEC)
            .build(LeavesBeGone.id("random_block_ticks"));

    public static void bootstrap() {
        // NO-OP
    }
}
