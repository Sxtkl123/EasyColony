package com.sxtkl.easycolony.mixin.accsessor.minecolonies;

import com.minecolonies.core.colony.buildings.modules.GraveyardManagementModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(GraveyardManagementModule.class)
public interface GraveyardManagementModuleAccessor {

    @Accessor
    List<String> getRestingCitizen();

}
