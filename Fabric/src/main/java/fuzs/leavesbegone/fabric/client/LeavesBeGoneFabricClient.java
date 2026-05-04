package fuzs.leavesbegone.fabric.client;

import fuzs.leavesbegone.common.LeavesBeGone;
import fuzs.leavesbegone.common.client.LeavesBeGoneClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class LeavesBeGoneFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(LeavesBeGone.MOD_ID, LeavesBeGoneClient::new);
    }
}
