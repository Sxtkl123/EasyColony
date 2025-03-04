package com.sxtkl.easycolony.core.network;

import com.minecolonies.api.network.IMessage;
import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.core.network.messages.server.TryClearClipboardMessage;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class NetworkChannel {

    public static SimpleChannel INSTANCE;

    public static final String VERSION = "1.0";
    private static int id = 0;

    public static void registerMessages() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(Easycolony.MODID, "net_channel"),
                () -> VERSION,
                (v) -> v.equals(VERSION),
                (v) -> v.equals(VERSION)
        );
        INSTANCE.registerMessage(id++, TryClearClipboardMessage.class, IMessage::toBytes, (msg) -> new TryClearClipboardMessage(), (msg, ctxIn) -> {
            final NetworkEvent.Context ctx = ctxIn.get();
            final LogicalSide packetOrigin = ctx.getDirection().getOriginationSide();
            ctx.setPacketHandled(true);
            ctx.enqueueWork(() -> msg.onExecute(ctx, packetOrigin.equals(LogicalSide.CLIENT)));
        });
    }

}
