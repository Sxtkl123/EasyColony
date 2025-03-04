package com.sxtkl.easycolony.api.item;

import com.minecolonies.core.items.AbstractItemMinecolonies;
import com.sxtkl.easycolony.Easycolony;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class AbstractRegisterItem extends AbstractItemMinecolonies {

    protected String id;

    public AbstractRegisterItem(String name, Properties properties) {
        super(name, properties);
        this.id = name;
    }

    public AbstractRegisterItem registerItem(final IForgeRegistry<Item> registry) {
        registry.register(new ResourceLocation(Easycolony.MODID, id), this);
        return this;
    }
}
