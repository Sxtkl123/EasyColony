package com.sxtkl.easycolony.core.event.client;

import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.item.AbstractClipboardItem;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class TooltipEvent {

    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (event.getItemStack().getItem() instanceof AbstractClipboardItem) {
            event.getToolTip().add(Component.translatable("com.sxtkl.easycolony.tooltip.clipboard_copy").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("com.sxtkl.easycolony.tooltip.clipboard_clear").withStyle(ChatFormatting.GRAY));
            event.getToolTip().add(Component.translatable("com.sxtkl.easycolony.tooltip.clipboard_paste").withStyle(ChatFormatting.GRAY));
        }
    }

}
