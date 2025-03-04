package com.sxtkl.easycolony.core.event.common;

import com.minecolonies.api.items.ModItems;
import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.block.AbstractResourceScrollBlock;
import com.sxtkl.easycolony.api.block.ModBlocks;
import com.sxtkl.easycolony.core.tileentity.TileEntityResourceScroll;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
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
        BlockState blockState = event.getLevel().getBlockState(event.getPos());
        if (blockState.getBlock() instanceof EntityBlock) return;
        BlockPos pos = event.getHitVec().getBlockPos().relative(event.getHitVec().getDirection());
        AbstractResourceScrollBlock scrollBlock = ModBlocks.resourceScrollBlock;
        BlockPlaceContext placeContext = new BlockPlaceContext(player.level(), player, event.getHand(), playerItemStack, event.getHitVec());
        BlockState placement = scrollBlock.getStateForPlacement(placeContext);
        if (event.getLevel().getBlockState(pos).getBlock() != Blocks.AIR) return;
        if (placement == null) {
            return;
        }
        if (player.level().setBlock(pos, placement, 11)) {
            BlockEntity blockentity = player.level().getBlockEntity(pos);
            if (blockentity instanceof TileEntityResourceScroll scroll) {
                ItemStack copy = playerItemStack.copy();
                copy.setCount(1);
                scroll.setResourceScroll(copy);
            }
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

    // TODO: 添加一个新功能，可以潜行右键直接把资源卷轴取下来，省的挖了...
}
