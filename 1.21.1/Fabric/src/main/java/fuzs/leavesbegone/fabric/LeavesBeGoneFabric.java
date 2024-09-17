package fuzs.leavesbegone.fabric;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class LeavesBeGoneFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(LeavesBeGone.MOD_ID, LeavesBeGone::new);
    }
}
