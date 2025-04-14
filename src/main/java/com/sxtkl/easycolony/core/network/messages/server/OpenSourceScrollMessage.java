package com.sxtkl.easycolony.core.network.messages.server;

import com.minecolonies.api.network.IMessage;
import com.minecolonies.core.items.ItemResourceScroll;
import com.sxtkl.easycolony.mixin.accessor.minecolonies.ItemResourceScrollAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class OpenSourceScrollMessage implements IMessage {

    private ItemStack scroll;

    public OpenSourceScrollMessage() {}

    public OpenSourceScrollMessage(ItemStack scroll) {
        this.scroll = scroll;
    }

    @Override
    public void toBytes(FriendlyByteBuf buf) {
        buf.writeItem(scroll);
    }

    @Override
    public void fromBytes(FriendlyByteBuf buf) {
        this.scroll = buf.readItem();
    }

    @Override
    public void onExecute(NetworkEvent.Context ctxIn, boolean isLogicalServer) {
        if (!(scroll.getItem() instanceof ItemResourceScroll item)) return;
        Player player = Minecraft.getInstance().player;
        ((ItemResourceScrollAccessor) item).invokeOpenWindow(scroll.getOrCreateTag(), player);
    }
}
