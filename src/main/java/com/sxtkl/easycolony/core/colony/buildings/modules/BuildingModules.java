package com.sxtkl.easycolony.core.colony.buildings.modules;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.api.colony.jobs.ModJobs;

public class BuildingModules {

    public static final BuildingEntry.ModuleProducer<ConsumeStatsModule, ConsumeStatsModuleView> CONSUME_STATS =
            new BuildingEntry.ModuleProducer<>("consume_stats", ConsumeStatsModule::new, () -> ConsumeStatsModuleView::new);

    public static final BuildingEntry.ModuleProducer<StoneCuttingCraftingModule, StoneCuttingCraftingModuleView> STONE_CUTTING =
            new BuildingEntry.ModuleProducer<>("stone_cutting", () -> new StoneCuttingCraftingModule(ModJobs.stoneMason.get()), () -> StoneCuttingCraftingModuleView::new);

}
