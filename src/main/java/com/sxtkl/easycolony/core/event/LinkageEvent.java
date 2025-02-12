package com.sxtkl.easycolony.core.event;

import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.core.manager.LinkageManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class LinkageEvent {

    @SubscribeEvent
    public static void fmlCommonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
           if (ModList.get().isLoaded("jecharacters")) {
               LinkageManager.setup();
           }
        });
    }

}
