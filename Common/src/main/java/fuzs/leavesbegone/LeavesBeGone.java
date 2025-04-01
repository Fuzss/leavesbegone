package fuzs.leavesbegone;

import fuzs.leavesbegone.config.ServerConfig;
import fuzs.leavesbegone.init.ModRegistry;
import fuzs.puzzleslib.api.config.v3.ConfigHolder;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import fuzs.puzzleslib.api.event.v1.level.ServerChunkEvents;
import net.minecraft.resources.ResourceLocation;
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

    private static void registerEventHandlers() {
        // do this after SerializableLevelChunkTicks::loadTickContainerInLevel, so we do not write to chunk data while it is being loaded
        ServerChunkEvents.LOAD.register((ServerLevel serverLevel, LevelChunk levelChunk) -> {
            ModRegistry.RANDOM_BLOCK_TICKS_ATTACHMENT_TYPE.set(levelChunk, null);
        });
    }

    public static ResourceLocation id(String path) {
        return ResourceLocationHelper.fromNamespaceAndPath(MOD_ID, path);
    }
}
