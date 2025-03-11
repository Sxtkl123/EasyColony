package com.sxtkl.easycolony.core.event.client;

import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.core.bake.TimberFrameBakedModel;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class BakeModelEvent {
    // @SubscribeEvent
    public static void onModelBaked(ModelEvent.ModifyBakingResult event) {
        List<ResourceLocation> ids = event.getModels().keySet().stream().filter((r) -> r.getNamespace().equals("domum_ornamentum") && r.getPath().equals("double_crossed")).toList();
        for (ResourceLocation id : ids) {
            BakedModel ori = event.getModels().get(id);
            if (ori != null) {
                BakedModel bakedModel = new TimberFrameBakedModel(ori);
                event.getModels().put(id, bakedModel);
            }
        }
    }

}
