package fuzs.leavesbegone.common.data;

import fuzs.leavesbegone.common.helper.LeavesDistanceHelper;
import fuzs.puzzleslib.common.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.common.api.data.v2.tags.AbstractTagAppender;
import fuzs.puzzleslib.common.api.data.v2.tags.AbstractTagProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;

public class ModBlockTagProvider extends AbstractTagProvider<Block> {

    public ModBlockTagProvider(DataProviderContext context) {
        super(Registries.BLOCK, context);
    }

    @Override
    public void addTags(HolderLookup.Provider provider) {
        provider.lookupOrThrow(Registries.BLOCK).listElements().map(Holder.Reference::value).forEach((Block block) -> {
            if (block instanceof LeavesBlock) {
                AbstractTagAppender<Block> tagAppender = this.tag(LeavesDistanceHelper.createBlockTag(block))
                        .add(block);
                if (block == Blocks.AZALEA_LEAVES) {
                    tagAppender.add(Blocks.FLOWERING_AZALEA_LEAVES);
                } else if (block == Blocks.FLOWERING_AZALEA_LEAVES) {
                    tagAppender.add(Blocks.AZALEA_LEAVES);
                }
            }
        });
    }
}
