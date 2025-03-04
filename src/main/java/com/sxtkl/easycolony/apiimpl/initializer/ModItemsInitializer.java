package com.sxtkl.easycolony.apiimpl.initializer;

import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.item.ModItems;
import com.sxtkl.easycolony.core.item.RecipesClipboardItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModItemsInitializer {

    @SubscribeEvent
    public static void registerItems(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
            init(event.getForgeRegistry());
        }
    }

    public static void init(final IForgeRegistry<Item> registry) {
        ModItems.recipesClipboard = new RecipesClipboardItem(new Item.Properties()).registerItem(registry);
    }

}
