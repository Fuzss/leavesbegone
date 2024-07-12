package fuzs.leavesbegone.neoforge.client;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.client.LeavesBeGoneClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = LeavesBeGone.MOD_ID, dist = Dist.CLIENT)
public class LeavesBeGoneNeoForgeClient {

    public LeavesBeGoneNeoForgeClient() {
        ClientModConstructor.construct(LeavesBeGone.MOD_ID, LeavesBeGoneClient::new);
    }
}
