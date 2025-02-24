package com.sxtkl.easycolony.core.colony.buildings.modules;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;

public class BuildingModules {

    public static final BuildingEntry.ModuleProducer<ConsumeStatsModule, ConsumeStatsModuleView> CONSUME_STATS =
            new BuildingEntry.ModuleProducer<>("consume_stats", ConsumeStatsModule::new, () -> ConsumeStatsModuleView::new);

}
