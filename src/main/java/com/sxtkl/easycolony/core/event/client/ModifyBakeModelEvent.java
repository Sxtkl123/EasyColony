package com.sxtkl.easycolony.core.event.client;

import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.model.HiddenModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModifyBakeModelEvent {

    private static final ModelResourceLocation HIDDEN_MODEL = new ModelResourceLocation("easycolony", "test", "");

    @SubscribeEvent
    public static void onModelBaked(ModelEvent.ModifyBakingResult event) {
        BakedModel ori = event.getModels().get(HIDDEN_MODEL);
        if (ori != null) {
            BakedModel bakedModel = new HiddenModel(ori);
            event.getModels().put(HIDDEN_MODEL, bakedModel);
        }
    }
}
