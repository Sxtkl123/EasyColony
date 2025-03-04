package com.sxtkl.easycolony.core.event.common;

import com.minecolonies.core.tileentities.TileEntityColonyBuilding;
import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.item.AbstractClipboardItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CopyPasteClipboardEvent {

    @SubscribeEvent
    public static void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        if (!event.getEntity().isCrouching()) return;
        if (!(event.getItemStack().getItem() instanceof AbstractClipboardItem clipboard)) return;
        final BlockEntity entity = event.getLevel().getBlockEntity(event.getPos());
        if (!(entity instanceof TileEntityColonyBuilding building)) return;
        if (event.getSide().isClient()) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            return;
        }
        if (!clipboard.paste(event.getItemStack().getOrCreateTag(), building.getBuilding())) {
            event.getEntity().sendSystemMessage(Component.translatable("com.sxtkl.easycolony.message.paste_fail"));
            event.setCancellationResult(InteractionResult.FAIL);
            return;
        }
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.getEntity().sendSystemMessage(Component.translatable("com.sxtkl.easycolony.message.paste_success"));
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLeftClickBlock(final PlayerInteractEvent.LeftClickBlock event) {
        if (!event.getEntity().isCrouching()) return;
        if (!(event.getItemStack().getItem() instanceof AbstractClipboardItem clipboard)) return;
        final BlockEntity entity = event.getLevel().getBlockEntity(event.getPos());
        if (!(entity instanceof TileEntityColonyBuilding building)) return;
        if (event.getSide().isClient()) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
            return;
        }
        if (!clipboard.copy(event.getItemStack().getOrCreateTag(), building.getBuilding())) {
            event.setCancellationResult(InteractionResult.FAIL);
            return;
        }
        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

}
