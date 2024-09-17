package fuzs.leavesbegone.config;

import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;

public class ServerConfig implements ConfigCore {
    @Config(description = "Minimum ticks after which leaves will start to decay.")
    @Config.IntRange(min = 0)
    public int minimumDecayTicks = 5;
    @Config(description = "Maximum ticks it takes for leaves to decay.")
    @Config.IntRange(min = 0)
    public int maximumDecayTicks = 20;
    @Config(description = "Leaves not attached to their own kind will decay. Does not work properly with trees that generate different leave blocks like azalea.")
    public boolean ignoreOtherLeaveTypes = false;
}
