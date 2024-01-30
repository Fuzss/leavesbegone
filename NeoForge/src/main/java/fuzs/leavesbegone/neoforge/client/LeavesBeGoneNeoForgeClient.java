package fuzs.leavesbegone.neoforge.client;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.client.LeavesBeGoneClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = LeavesBeGone.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class LeavesBeGoneNeoForgeClient {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ClientModConstructor.construct(LeavesBeGone.MOD_ID, LeavesBeGoneClient::new);
    }
}
