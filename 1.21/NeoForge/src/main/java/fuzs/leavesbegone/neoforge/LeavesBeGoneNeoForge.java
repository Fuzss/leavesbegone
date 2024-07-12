package fuzs.leavesbegone.neoforge;

import fuzs.leavesbegone.LeavesBeGone;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.fml.common.Mod;

@Mod(LeavesBeGone.MOD_ID)
public class LeavesBeGoneNeoForge {

    public LeavesBeGoneNeoForge() {
        ModConstructor.construct(LeavesBeGone.MOD_ID, LeavesBeGone::new);
    }
}
