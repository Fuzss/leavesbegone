package fuzs.leavesbegone.neoforge;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.leavesbegone.data.ModBlockTagProvider;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.fml.common.Mod;

@Mod(LeavesBeGone.MOD_ID)
public class LeavesBeGoneNeoForge {

    public LeavesBeGoneNeoForge() {
        ModConstructor.construct(LeavesBeGone.MOD_ID, LeavesBeGone::new);
        DataProviderHelper.registerDataProviders(LeavesBeGone.MOD_ID, ModBlockTagProvider::new);
    }
}
