package fuzs.leavesbegone.neoforge.client;

import fuzs.leavesbegone.common.LeavesBeGone;
import fuzs.leavesbegone.common.client.LeavesBeGoneClient;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;

@Mod(value = LeavesBeGone.MOD_ID, dist = Dist.CLIENT)
public class LeavesBeGoneNeoForgeClient {

    public LeavesBeGoneNeoForgeClient() {
        ClientModConstructor.construct(LeavesBeGone.MOD_ID, LeavesBeGoneClient::new);
    }
}
