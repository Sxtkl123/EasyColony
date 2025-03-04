package com.sxtkl.easycolony.core.event.client;

import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.core.item.RecipesClipboardItem;
import com.sxtkl.easycolony.core.network.NetworkChannel;
import com.sxtkl.easycolony.core.network.messages.server.TryClearClipboardMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClearOrderEvent {

    @SubscribeEvent
    public static void onClientTickEvent(@NotNull final TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;

        final Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null || mc.level == null) return;
        if (!mc.player.isCrouching()) return;
        if (!mc.options.keyPickItem.consumeClick()) return;
        if (!(mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem() instanceof RecipesClipboardItem)) return;
        NetworkChannel.INSTANCE.sendToServer(new TryClearClipboardMessage());
    }

}
