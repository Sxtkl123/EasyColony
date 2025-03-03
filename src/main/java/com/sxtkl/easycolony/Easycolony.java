package com.sxtkl.easycolony;


import com.mojang.logging.LogUtils;
import com.sxtkl.easycolony.api.creativetab.ModCreativeTabs;
import com.sxtkl.easycolony.apiimpl.initializer.ModTileEntitiesInitializer;
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
        ModCreativeTabs.TAB_REG.register(context.getModEventBus());
        ModTileEntitiesInitializer.BLOCK_ENTITIES.register(context.getModEventBus());
    }
}
