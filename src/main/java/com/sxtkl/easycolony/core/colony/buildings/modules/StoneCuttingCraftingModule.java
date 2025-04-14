package com.sxtkl.easycolony.core.colony.buildings.modules;

import com.minecolonies.api.colony.jobs.registry.JobEntry;
import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.crafting.IGenericRecipe;
import com.minecolonies.api.crafting.registry.CraftingType;
import com.minecolonies.api.util.CraftingUtils;
import com.minecolonies.api.util.OptionalPredicate;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;
import com.sxtkl.easycolony.Config;
import com.sxtkl.easycolony.api.crafting.ModCraftingTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

import static com.minecolonies.api.util.constant.NbtTagConstants.TAG_RECIPES;
import static com.minecolonies.api.util.constant.TagConstants.CRAFTING_STONEMASON;

public class StoneCuttingCraftingModule extends AbstractCraftingBuildingModule.Custom {

    public StoneCuttingCraftingModule(JobEntry jobEntry) {
        super(jobEntry);
    }

    @Override
    public boolean isRecipeCompatible(@NotNull final IGenericRecipe recipe) {
        return true;
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

    @Override
    public void toggle(int recipeLocation) {
        if (!Config.allowStoneCutting) return;
        super.toggle(recipeLocation);
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        super.deserializeNBT(compound);
        ListTag recipesTags = new ListTag();
        if (compound.contains(TAG_RECIPES)) {
            recipesTags = compound.getList(TAG_RECIPES, Tag.TAG_COMPOUND);
        }
        for (int i = 0; i < recipesTags.size(); i++) {
            final IToken<?> token = StandardFactoryController.getInstance().deserialize(recipesTags.getCompound(i));
            if (!disabledRecipes.contains(token)) {
                disabledRecipes.add(token);
            }
        }
    }

}
