package com.sxtkl.easycolony.core.colony.buildings.modules;

import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.api.util.CraftingUtils;
import com.minecolonies.api.util.OptionalPredicate;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;
import com.sxtkl.easycolony.api.crafting.ModCraftingTypes;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.minecolonies.api.util.constant.TagConstants.CRAFTING_STONEMASON;

public class StoneCuttingCraftingModule extends AbstractCraftingBuildingModule.Custom {

    public StoneCuttingCraftingModule(JobEntry jobEntry) {
        super(jobEntry);
    }

    @Override
    public Set<CraftingType> getSupportedCraftingTypes() {
        return Set.of(ModCraftingTypes.STONECUTTING_CRAFTING.get());
    }

    public @NotNull
    static OptionalPredicate<ItemStack> getStaticIngredientValidator() {
        return CraftingUtils.getIngredientValidatorBasedOnTags(CRAFTING_STONEMASON, true);
    }

    @Override
    public @NotNull OptionalPredicate<ItemStack> getIngredientValidator() {
        return getStaticIngredientValidator();
    }

    @NotNull
    public String getId() {
        return "stone_cutting";
    }

}
