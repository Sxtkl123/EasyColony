package com.sxtkl.easycolony.core.network.messages.server;

import com.minecolonies.api.network.IMessage;
import com.sxtkl.easycolony.Easycolony;
import net.minecraft.network.FriendlyByteBuf;
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
        Easycolony.LOGGER.info("Try clear order message.");
    }
}
