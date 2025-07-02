package fuzs.leavesbegone.fabric.client;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.client.LeavesBeGoneClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class LeavesBeGoneFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(LeavesBeGone.MOD_ID, LeavesBeGoneClient::new);
    }
}
