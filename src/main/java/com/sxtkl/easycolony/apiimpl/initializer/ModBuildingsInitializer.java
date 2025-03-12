package com.sxtkl.easycolony.apiimpl.initializer;

import com.minecolonies.api.colony.buildings.ModBuildings;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;

import static com.minecolonies.core.colony.buildings.modules.BuildingModules.BUILDER_SETTINGS;
import static com.minecolonies.core.colony.buildings.modules.BuildingModules.STONEMASON_DO_CRAFT;
import static com.sxtkl.easycolony.core.colony.buildings.modules.BuildingModules.CONSUME_STATS;
import static com.sxtkl.easycolony.core.colony.buildings.modules.BuildingModules.STONE_CUTTING;

public final class ModBuildingsInitializer {

    public static void init() {
        insertBefore(ModBuildings.builder.get(), BUILDER_SETTINGS, CONSUME_STATS);
        insertBefore(ModBuildings.stoneMason.get(), STONEMASON_DO_CRAFT, STONE_CUTTING);
    }

    public static void insertBefore(
            BuildingEntry entry,
            BuildingEntry.ModuleProducer<?, ?> target,
            BuildingEntry.ModuleProducer<?, ?> ele
    ) {
        int index = entry.getModuleProducers().indexOf(target);
        entry.getModuleProducers().add(index, ele);
    }

}
