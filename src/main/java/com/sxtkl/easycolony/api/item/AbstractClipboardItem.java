package com.sxtkl.easycolony.api.item;

import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.core.items.AbstractItemMinecolonies;
import com.sxtkl.easycolony.Easycolony;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class AbstractClipboardItem extends AbstractItemMinecolonies {

    protected String name;

    public AbstractClipboardItem(String name, Properties properties) {
        super(name, properties);
        this.name = name;
    }


    public AbstractClipboardItem registerItem(final IForgeRegistry<Item> registry) {
        registry.register(new ResourceLocation(Easycolony.MODID, name), this);
        return this;
    }

    public abstract boolean copy(final CompoundTag tag, final IBuilding building);

    public abstract boolean paste(final CompoundTag tag, final IBuilding building);

    public abstract boolean clear(final CompoundTag tag);

}
