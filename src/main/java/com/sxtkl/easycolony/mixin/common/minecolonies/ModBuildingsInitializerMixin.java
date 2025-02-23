package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.api.colony.buildings.registry.BuildingEntry;
import com.minecolonies.apiimp.initializer.ModBuildingsInitializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.sxtkl.easycolony.core.colony.buildings.modules.BuildingModules.CONSUME_STATS;

@Mixin(ModBuildingsInitializer.class)
public abstract class ModBuildingsInitializerMixin {

    @Redirect(method = "lambda$static$11", at = @At(value = "INVOKE", target = "Lcom/minecolonies/api/colony/buildings/registry/BuildingEntry$Builder;addBuildingModuleProducer(Lcom/minecolonies/api/colony/buildings/registry/BuildingEntry$ModuleProducer;)Lcom/minecolonies/api/colony/buildings/registry/BuildingEntry$Builder;", ordinal = 2), remap = false)
    private static BuildingEntry.Builder test(BuildingEntry.Builder instance, BuildingEntry.ModuleProducer<?, ?> moduleSet) {
        return instance.addBuildingModuleProducer(moduleSet).addBuildingModuleProducer(CONSUME_STATS);
    }

}
