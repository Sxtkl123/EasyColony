package com.sxtkl.easycolony;

import com.mojang.logging.LogUtils;
import com.sxtkl.easycolony.core.event.EventHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Easycolony.MODID)
public class Easycolony {

    public static final String MODID = "easycolony";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Easycolony(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        MinecraftForge.EVENT_BUS.register(EventHandler.class);
    }
}
