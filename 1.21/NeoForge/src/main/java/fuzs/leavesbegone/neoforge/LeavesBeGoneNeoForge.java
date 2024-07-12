package fuzs.leavesbegone.neoforge;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;

@Mod(LeavesBeGone.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LeavesBeGoneNeoForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        ModConstructor.construct(LeavesBeGone.MOD_ID, LeavesBeGone::new);
    }
}
