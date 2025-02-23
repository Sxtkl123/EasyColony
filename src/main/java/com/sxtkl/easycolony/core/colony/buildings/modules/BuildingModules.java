package com.sxtkl.easycolony.core.colony.buildings.modules;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.core.colony.buildings.modules.MinimumStockModule;
import com.minecolonies.core.colony.buildings.moduleviews.MinimumStockModuleView;

public class BuildingModules {

    public static final BuildingEntry.ModuleProducer<MinimumStockModule, MinimumStockModuleView> CONSUME_STATS =
            new BuildingEntry.ModuleProducer<>("consume_stats", ConsumeStatsModule::new, () -> ConsumeStatsModuleView::new);

}
