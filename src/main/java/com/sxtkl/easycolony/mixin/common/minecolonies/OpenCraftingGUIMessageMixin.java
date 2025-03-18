package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.core.colony.buildings.modules.AbstractCraftingBuildingModule;
import com.minecolonies.core.network.messages.server.colony.building.OpenCraftingGUIMessage;
import com.sxtkl.easycolony.api.crafting.ModCraftingTypes;
import com.sxtkl.easycolony.api.inventory.container.ContainerCraftingStoneCutting;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("DataFlowIssue")
@Mixin(OpenCraftingGUIMessage.class)
public abstract class OpenCraftingGUIMessageMixin {

    @Shadow(remap = false)
    private int id;

    @Inject(method = "onExecute", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/network/NetworkHooks;openScreen(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/MenuProvider;Ljava/util/function/Consumer;)V", ordinal = 2), remap = false)
    public void onExecute(NetworkEvent.Context ctxIn, boolean isLogicalServer, IColony colony, IBuilding building, CallbackInfo ci) {
        AbstractCraftingBuildingModule module = (AbstractCraftingBuildingModule) building.getModule(id);
        if (module.canLearn(ModCraftingTypes.STONECUTTING_CRAFTING.get())) {
            NetworkHooks.openScreen(ctxIn.getSender(), new MenuProvider() {
                @NotNull
                @Override
                public Component getDisplayName() {
                    return Component.literal("Stone Cutting GUI");
                }

                @NotNull
                @Override
                public AbstractContainerMenu createMenu(final int id, @NotNull final Inventory inv, @NotNull final Player player) {
                    return new ContainerCraftingStoneCutting(id, inv, building.getID(), module.getProducer().getRuntimeID());
                }
            }, buffer -> new FriendlyByteBuf(buffer.writeBlockPos(building.getID()).writeInt(module.getProducer().getRuntimeID())));
        }
    }

}
