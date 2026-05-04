package fuzs.leavesbegone.common;

import fuzs.leavesbegone.common.config.ServerConfig;
import fuzs.leavesbegone.common.init.ModRegistry;
import fuzs.puzzleslib.common.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import fuzs.puzzleslib.common.api.event.v1.level.ServerChunkEvents;
import net.minecraft.resources.Identifier;
import net.minecraft.server.TickTask;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeavesBeGone implements ModConstructor {
    public static final String MOD_ID = "leavesbegone";
    public static final String MOD_NAME = "Leaves Be Gone";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public static final ConfigHolder CONFIG = ConfigHolder.builder(MOD_ID).server(ServerConfig.class);

    @Override
    public void onConstructMod() {
        ModRegistry.bootstrap();
        registerEventHandlers();
    }

    /**
     * Only kept for legacy versions, so that the attachment can properly be removed without needlessly logging a bunch
     * of warnings every time the chunks load.
     */
    @Deprecated
    private static void registerEventHandlers() {
        ServerChunkEvents.LOAD.register((ServerLevel serverLevel, LevelChunk levelChunk, boolean isNewlyGenerated) -> {
            serverLevel.getServer().schedule(new TickTask(0, () -> {
                if (ModRegistry.RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE.has(levelChunk)) {
                    ModRegistry.RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE.set(levelChunk, null);
                }
            }));
        });
    }

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }
}
