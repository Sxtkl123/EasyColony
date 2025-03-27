package com.sxtkl.easycolony.extension;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraftforge.items.IItemHandler;

import java.util.List;

public interface IMultiCraftExtension {

    List<ItemStack> fullfillRecipeAndCopy(final LootParams context, final List<IItemHandler> handlers, boolean doInsert, int times);

}
