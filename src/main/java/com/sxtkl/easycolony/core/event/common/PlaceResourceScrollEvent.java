package com.sxtkl.easycolony.core.event.common;

import com.minecolonies.api.items.ModItems;
import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.block.AbstractResourceScrollBlock;
import com.sxtkl.easycolony.api.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlaceResourceScrollEvent {

    @SubscribeEvent
    public static void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        ItemStack playerItemStack = event.getItemStack();
        Player player = event.getEntity();
        if (playerItemStack.getItem() != ModItems.resourceScroll) return;
        if (!player.isCrouching()) return;
        BlockPos pos = event.getHitVec().getBlockPos().relative(event.getHitVec().getDirection());
        AbstractResourceScrollBlock scrollBlock = ModBlocks.resourceScrollBlock;
        BlockPlaceContext placeContext = new BlockPlaceContext(player.level(), player, event.getHand(), playerItemStack, event.getHitVec());
        BlockState placement = scrollBlock.getStateForPlacement(placeContext);
        if (placement == null) {
            return;
        }
        if (player.level().setBlock(pos, placement, 11)) {
            player.level().gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, placement));
            SoundType soundType = SoundType.BAMBOO;
            player.level().playSound(player, pos, soundType.getPlaceSound(), SoundSource.BLOCKS, (soundType.getVolume() + 1.0F) / 2.0F, soundType.getPitch() * 0.8F);
            if (!player.getAbilities().instabuild) {
                playerItemStack.shrink(1);
            }
            player.swing(event.getHand());
            event.setCancellationResult(InteractionResult.CONSUME);
            event.setCanceled(true);
        }
    }

}
