package fuzs.leavesbegone;

import fuzs.leavesbegone.config.ServerConfig;
import fuzs.puzzleslib.config.ConfigHolder;
import fuzs.puzzleslib.core.CoreServices;
import fuzs.puzzleslib.core.ModConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LeavesBeGone implements ModConstructor {
    public static final String MOD_ID = "leavesbegone";
    public static final String MOD_NAME = "Leaves Be Gone";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    @SuppressWarnings("Convert2MethodRef")
    public static final ConfigHolder CONFIG = CoreServices.FACTORIES.serverConfig(ServerConfig.class, () -> new ServerConfig());

    @Override
    public void onConstructMod() {
        CONFIG.bakeConfigs(MOD_ID);
    }
}
