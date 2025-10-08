package fuzs.leavesbegone.init;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentRegistry;
import fuzs.puzzleslib.api.attachment.v4.DataAttachmentType;
import net.minecraft.util.Unit;
import net.minecraft.world.level.chunk.LevelChunk;

public class ModRegistry {
    /**
     * Only kept for legacy versions, so that the attachment can properly be removed without needlessly logging a bunch
     * of warnings every time the chunks load.
     */
    @Deprecated
    public static final DataAttachmentType<LevelChunk, Unit> RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE = DataAttachmentRegistry.<Unit>levelChunkBuilder()
            .persistent(Unit.CODEC.orElse(Unit.INSTANCE))
            .build(LeavesBeGone.id("random_block_ticks"));

    public static void bootstrap() {
        // NO-OP
    }
}
