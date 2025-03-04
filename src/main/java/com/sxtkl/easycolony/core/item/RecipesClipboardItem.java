package com.sxtkl.easycolony.core.item;

import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.minecolonies.api.colony.requestsystem.token.IToken;
import com.minecolonies.api.util.NBTUtils;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;
import com.sxtkl.easycolony.api.item.AbstractClipboardItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.Item;

import java.util.List;

public class RecipesClipboardItem extends AbstractClipboardItem {

    public RecipesClipboardItem(final Item.Properties properties) {
        super("recipes_clipboard", properties.stacksTo(64));
    }

    @Override
    public boolean copy(final CompoundTag tag, final IBuilding building) {
        List<AbstractCraftingBuildingModule> modulesByType = building.getModulesByType(AbstractCraftingBuildingModule.class);
        if (modulesByType.isEmpty()) {
            return false;
        }

        for (AbstractCraftingBuildingModule module : modulesByType) {
            String key = module.getCustomRecipeKey();
            final ListTag recipesTagList = module.getRecipes().stream()
                    .map(token -> StandardFactoryController.getInstance().serialize(token))
                    .collect(NBTUtils.toListNBT());
            tag.put(key, recipesTagList);
        }
        return true;
    }

    @Override
    public boolean paste(final CompoundTag tag, final IBuilding building) {
        List<AbstractCraftingBuildingModule> modulesByType = building.getModulesByType(AbstractCraftingBuildingModule.class);
        if (modulesByType.isEmpty()) {
            return false;
        }
        boolean allSuccess = true;
        for (AbstractCraftingBuildingModule module : modulesByType) {
            ListTag recipesTags = new ListTag();
            String key = module.getCustomRecipeKey();
            if (tag.contains(key)) {
                recipesTags = tag.getList(key, Tag.TAG_COMPOUND);
            }

            for (int i = 0; i < recipesTags.size(); i++) {
                final IToken<?> token = StandardFactoryController.getInstance().deserialize(recipesTags.getCompound(i));
                if (!module.addRecipe(token)) {
                    allSuccess = false;
                }
            }
        }
        return allSuccess;
    }

    @Override
    public boolean clear(final CompoundTag tag) {
        tag.getAllKeys().forEach(tag::remove);
        return true;
    }
}
