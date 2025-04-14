package com.sxtkl.easycolony.core.colony.buildings.modules;

import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.sxtkl.easycolony.Config;

public class StoneCuttingCraftingModuleView extends CraftingModuleView {

    @Override
    public void toggle(final int row) {
        if (!Config.allowStoneCutting) return;
        super.toggle(row);
    }

}
