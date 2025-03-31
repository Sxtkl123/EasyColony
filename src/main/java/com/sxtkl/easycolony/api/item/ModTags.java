package com.sxtkl.easycolony.api.item;

import com.sxtkl.easycolony.Easycolony;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModTags {

    public static final TagKey<Item> DELETED = ItemTags.create(new ResourceLocation(Easycolony.MODID, "deleted"));

}
