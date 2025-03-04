package com.sxtkl.easycolony.core.network.messages.server;

import com.minecolonies.api.network.IMessage;
import com.sxtkl.easycolony.api.item.AbstractClipboardItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class TryClearClipboardMessage implements IMessage {

    public TryClearClipboardMessage() {
    }

    @Override
    public void toBytes(final FriendlyByteBuf buf) {
    }

    @Override
    public void fromBytes(final FriendlyByteBuf buf) {
    }

    @Override
    public void onExecute(NetworkEvent.Context ctxIn, boolean isLogicalServer) {
        Player sender = ctxIn.getSender();
        if (sender == null) return;
        ItemStack itemInHand = sender.getItemInHand(InteractionHand.MAIN_HAND);
        if (!(itemInHand.getItem() instanceof AbstractClipboardItem clipboard)) return;
        clipboard.clear(itemInHand.getOrCreateTag());
    }
}
