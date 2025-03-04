package com.sxtkl.easycolony.core.item;

import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.core.colony.buildings.modules.BuildingModules;
import com.minecolonies.core.colony.buildings.modules.MinimumStockModule;
import com.sxtkl.easycolony.api.item.AbstractClipboardItem;
import com.sxtkl.easycolony.mixin.accessor.minecolonies.MinimumStockModuleAccessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Map;


public class StorageClipboardItem extends AbstractClipboardItem {

    public StorageClipboardItem(Properties properties) {
        super("storage_clipboard", properties.stacksTo(64));
    }

    @Override
    public boolean copy(CompoundTag tag, IBuilding building) {
        if (!building.hasModule(BuildingModules.MIN_STOCK)) return false;
        MinimumStockModule module = building.getModule(BuildingModules.MIN_STOCK);
        @NotNull final ListTag minimumStockTagList = new ListTag();
        for (@NotNull final Map.Entry<ItemStorage, Integer> entry : ((MinimumStockModuleAccessor) module).getMinimumStock().entrySet()) {
            final CompoundTag compoundNBT = new CompoundTag();
            entry.getKey().getItemStack().save(compoundNBT);
            compoundNBT.putInt("quantity", entry.getValue());
            minimumStockTagList.add(compoundNBT);
        }
        tag.put("minstock", minimumStockTagList);
        return true;
    }

    @Override
    public boolean paste(CompoundTag tag, IBuilding building) {
        if (!building.hasModule(BuildingModules.MIN_STOCK)) return false;
        if (tag.isEmpty()) return false;
        MinimumStockModule module = building.getModule(BuildingModules.MIN_STOCK);
        final ListTag minimumStockTagList = tag.getList("minstock", Tag.TAG_COMPOUND);
        for (int i = 0; i < minimumStockTagList.size(); i++) {
            final CompoundTag compoundNBT = minimumStockTagList.getCompound(i);
            module.addMinimumStock(ItemStack.of(compoundNBT), compoundNBT.getInt("quantity"));
        }
        return true;
    }

    @Override
    public boolean clear(CompoundTag tag) {
        tag.getAllKeys().forEach(tag::remove);
        return false;
    }
}
