package com.sxtkl.easycolony.apiimpl.initializer;

import com.minecolonies.api.colony.buildings.ModBuildings;
import com.minecolonies.api.colony.buildings.registry.BuildingEntry;

import static com.minecolonies.core.colony.buildings.modules.BuildingModules.BUILDER_SETTINGS;
import static com.sxtkl.easycolony.core.colony.buildings.modules.BuildingModules.CONSUME_STATS;

public final class ModBuildingsInitializer {

    public static void init() {
        insertBefore(ModBuildings.builder.get(), BUILDER_SETTINGS, CONSUME_STATS);
    }

    public static void insertBefore(
            BuildingEntry entry,
            BuildingEntry.ModuleProducer<?, ?> target,
            BuildingEntry.ModuleProducer<?, ?> ele
    ) {
        int index = entry.getModuleProducers().indexOf(target);
        ModBuildings.builder.get().getModuleProducers().add(index, ele);
    }

}
