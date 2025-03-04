package com.sxtkl.easycolony.core.item;

import com.sxtkl.easycolony.api.item.AbstractRegisterItem;
import net.minecraft.world.item.Item;

public class RecipesClipboardItem extends AbstractRegisterItem {

    public RecipesClipboardItem(final Item.Properties properties) {
        super("recipes_clipboard", properties.stacksTo(64));
    }

}
