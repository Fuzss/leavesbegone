package fuzs.leavesbegone;

import fuzs.puzzleslib.core.CoreServices;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod(LeavesBeGone.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class LeavesBeGoneForge {

    @SubscribeEvent
    public static void onConstructMod(final FMLConstructModEvent evt) {
        CoreServices.FACTORIES.modConstructor(LeavesBeGone.MOD_ID).accept(new LeavesBeGone());
    }
}
