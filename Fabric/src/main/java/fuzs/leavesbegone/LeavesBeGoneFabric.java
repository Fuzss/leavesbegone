package fuzs.leavesbegone;

import fuzs.puzzleslib.core.CoreServices;
import net.fabricmc.api.ModInitializer;

public class LeavesBeGoneFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CoreServices.FACTORIES.modConstructor(LeavesBeGone.MOD_ID).accept(new LeavesBeGone());
    }
}
