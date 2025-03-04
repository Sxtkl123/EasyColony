package com.sxtkl.easycolony.core.event.client;

import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.item.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClipboardOverrideTextureEvent {

    @SubscribeEvent
    public static void propertyOverrideRegistry(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ItemProperties.register(
                ModItems.recipesClipboard,
                new ResourceLocation(Easycolony.MODID, "written"),
                ((pStack, pLevel, pEntity, pSeed) -> pStack.getOrCreateTag().isEmpty() ? 0.0F : 1.0F)
            );
            ItemProperties.register(
                    ModItems.storageClipboard,
                    new ResourceLocation(Easycolony.MODID, "written"),
                    ((pStack, pLevel, pEntity, pSeed) -> pStack.getOrCreateTag().isEmpty() ? 0.0F : 1.0F)
            );
        });
    }
}
