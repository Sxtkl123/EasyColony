package com.sxtkl.easycolony.api.crafting;

import com.minecolonies.api.crafting.registry.CraftingType;
import com.sxtkl.easycolony.Easycolony;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;

public class ModCraftingTypes {

    public static final ResourceLocation STONECUTTING_CRAFTING_ID = new ResourceLocation(Easycolony.MODID, "stonecutting");

    public static RegistryObject<CraftingType> STONECUTTING_CRAFTING;

}
