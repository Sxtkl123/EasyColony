package com.sxtkl.easycolony.apiimpl.initializer;

import com.minecolonies.api.crafting.RecipeCraftingType;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.api.util.constant.Constants;
import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.crafting.ModCraftingTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.DeferredRegister;

public final class ModCraftingTypesInitializer {

    public final static DeferredRegister<CraftingType>
            DEFERRED_REGISTER = DeferredRegister.create(new ResourceLocation(Constants.MOD_ID, "craftingtypes"), Easycolony.MODID);

    static {
        ModCraftingTypes.STONECUTTING_CRAFTING = DEFERRED_REGISTER.register(ModCraftingTypes.STONECUTTING_CRAFTING_ID.getPath(), () -> new RecipeCraftingType<>(ModCraftingTypes.STONECUTTING_CRAFTING_ID,
                RecipeType.STONECUTTING, null));
    }

}
