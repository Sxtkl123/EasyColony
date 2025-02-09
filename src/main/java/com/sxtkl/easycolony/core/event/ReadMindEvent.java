package com.sxtkl.easycolony.core.event;

import com.minecolonies.api.entity.ai.ITickingStateAI;
import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import com.sxtkl.easycolony.Easycolony;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ReadMindEvent {

    @SubscribeEvent
    public static void onPlayerInteract$EntityInteract(final PlayerInteractEvent.EntityInteract event) {
        if (event.isCanceled()) return;
        if (event.getItemStack().getItem() != Items.COMPASS) return;
        if (!(event.getTarget() instanceof EntityCitizen citizen)) return;
        if (event.getEntity().isShiftKeyDown()) return;
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
        if (event.getSide().isClient()) return;
        IState stat;
        ITickingStateAI workAI = citizen.getCitizenJobHandler().getWorkAI();
        if (workAI == null) {
            stat = citizen.getCitizenAI().getState();
        } else {
            stat = workAI.getState();
        }
        Component msg = Component.translatable("com.sxtkl.easycolony.event.readmind.message")
                .append(Component.translatable("com.sxtkl.easycolony.state." + stat.toString().toLowerCase()));
        event.getEntity().sendSystemMessage(msg);
    }
}
