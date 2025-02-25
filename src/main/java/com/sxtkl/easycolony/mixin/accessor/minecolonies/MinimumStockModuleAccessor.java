package com.sxtkl.easycolony.mixin.accessor.minecolonies;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.core.colony.buildings.modules.MinimumStockModule;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@Mixin(value = MinimumStockModule.class, remap = false)
public interface MinimumStockModuleAccessor {

    @Accessor(remap = false)
    Map<ItemStorage, Integer> getMinimumStock();

    @Invoker(value = "minimumStockSize", remap = false)
    int invokeMinimumStockSize();

}
