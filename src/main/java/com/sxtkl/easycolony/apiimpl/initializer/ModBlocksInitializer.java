package com.sxtkl.easycolony.apiimpl.initializer;

import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.block.ModBlocks;
import com.sxtkl.easycolony.core.block.CitizenSensorBlock;
import com.sxtkl.easycolony.core.block.ResourceScrollBlock;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ModBlocksInitializer {
    @SubscribeEvent
    public static void registerBlocks(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCKS)) {
            init(event.getForgeRegistry());
        }
    }

    @SubscribeEvent
    public static void registerItems(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS)) {
            registerBlockItem(event.getForgeRegistry());
        }
    }

    public static void init(final IForgeRegistry<Block> registry) {
        ModBlocks.citizenSensorBlock = new CitizenSensorBlock().registerBlock(registry);
        ModBlocks.resourceScrollBlock = new ResourceScrollBlock().registerBlock(registry);
    }

    public static void registerBlockItem(final IForgeRegistry<Item> registry) {
        ModBlocks.citizenSensorBlock.registerBlockItem(registry, new Item.Properties());
    }

}
