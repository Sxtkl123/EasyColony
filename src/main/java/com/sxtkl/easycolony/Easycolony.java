package com.sxtkl.easycolony;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Easycolony.MODID)
public class Easycolony {

    public static final String MODID = "easycolony";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Easycolony(FMLJavaModLoadingContext context) {
    }
}
