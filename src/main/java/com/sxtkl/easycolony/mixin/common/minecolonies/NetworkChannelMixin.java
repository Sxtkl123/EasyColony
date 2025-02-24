package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.api.network.IMessage;
import com.minecolonies.core.network.NetworkChannel;
import com.sxtkl.easycolony.core.network.messages.server.colony.building.RemoveConsumeStatsFromBuildingModuleMessage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.function.Supplier;

@Mixin(NetworkChannel.class)
public abstract class NetworkChannelMixin {

    @Shadow(remap = false)
    protected abstract <MSG extends IMessage> void registerMessage(final int id, final Class<MSG> msgClazz, final Supplier<MSG> msgCreator);

    @Inject(method = "registerCommonMessages", at = @At("RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private void registerCommonMessages(CallbackInfo ci, int idx) {
        registerMessage(++idx, RemoveConsumeStatsFromBuildingModuleMessage.class, RemoveConsumeStatsFromBuildingModuleMessage::new);
    }

}
