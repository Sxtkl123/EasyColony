package com.sxtkl.easycolony.apiimpl.initializer;


import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.inventory.ModContainers;
import com.sxtkl.easycolony.api.inventory.container.ContainerCraftingStoneCutting;
import com.sxtkl.easycolony.core.client.gui.containers.WindowStoneCutting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModContainerInitializers {

    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Easycolony.MODID);

    static {
        ModContainers.craftingStoneCutting = CONTAINERS.register("crafting_stone_cutting", () -> IForgeMenuType.create(ContainerCraftingStoneCutting::fromFriendlyByteBuf));
    }

    @SubscribeEvent
    public static void doClientStuff(final FMLClientSetupEvent event) {
        MenuScreens.register(ModContainers.craftingStoneCutting.get(), WindowStoneCutting::new);
    }
}
