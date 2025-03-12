package com.sxtkl.easycolony;


import com.mojang.logging.LogUtils;
import com.sxtkl.easycolony.api.creativetab.ModCreativeTabs;
import com.sxtkl.easycolony.apiimpl.initializer.ModBuildingsInitializer;
import com.sxtkl.easycolony.apiimpl.initializer.ModCraftingTypesInitializer;
import com.sxtkl.easycolony.apiimpl.initializer.ModTileEntitiesInitializer;
import com.sxtkl.easycolony.core.network.NetworkChannel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Mod(Easycolony.MODID)
public class Easycolony {

    public static final String MODID = "easycolony";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Easycolony(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        ModCreativeTabs.TAB_REG.register(context.getModEventBus());
        ModTileEntitiesInitializer.BLOCK_ENTITIES.register(context.getModEventBus());

        Mod.EventBusSubscriber.Bus.FORGE.bus().get().register(this.getClass());
        Mod.EventBusSubscriber.Bus.MOD.bus().get().register(this.getClass());
    }

    @SubscribeEvent
    public static void preInit(@NotNull final FMLCommonSetupEvent event) {
        NetworkChannel.registerMessages();
        event.enqueueWork(ModBuildingsInitializer::init);
    }
}
