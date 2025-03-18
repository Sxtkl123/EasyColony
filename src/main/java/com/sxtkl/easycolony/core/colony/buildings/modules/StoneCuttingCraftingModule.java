package com.sxtkl.easycolony.core.colony.buildings.modules;

import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.api.util.CraftingUtils;
import com.minecolonies.api.util.OptionalPredicate;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;
import com.sxtkl.easycolony.api.crafting.ModCraftingTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.minecolonies.api.util.constant.TagConstants.CRAFTING_STONEMASON;

public class StoneCuttingCraftingModule extends AbstractCraftingBuildingModule.Crafting {

    public StoneCuttingCraftingModule(JobEntry jobEntry) {
        super(jobEntry);
    }

    @Override
    public Set<CraftingType> getSupportedCraftingTypes() {
        return Set.of(ModCraftingTypes.STONECUTTING_CRAFTING.get());
    }

    @Override
    public boolean isRecipeCompatible(@NotNull final IGenericRecipe recipe) {
        return canLearn(ModCraftingTypes.STONECUTTING_CRAFTING.get()) &&
                recipe.getIntermediate() == Blocks.AIR;
    }

    @Override
    public @NotNull OptionalPredicate<ItemStack> getIngredientValidator() {
        return CraftingUtils.getIngredientValidatorBasedOnTags(CRAFTING_STONEMASON)
                .combine(super.getIngredientValidator());
    }

    @NotNull
    public String getId() {
        return "stone_cutting";
    }

}
