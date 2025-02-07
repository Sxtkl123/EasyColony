package com.sxtkl.easycolony.core.event;

import com.minecolonies.api.colony.*;
import com.minecolonies.api.tileentities.AbstractTileEntityGrave;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


public class EventHandler {

    @SubscribeEvent
    public static void onPlayerInteract$RightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        // TODO: 测试命名的墓碑是否可以正常运行，否则编写相关代码。
        if (true) return;
        if (event.getSide().isClient()) return;
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockEntity entity = level.getBlockEntity(pos);
        ItemStack stack = event.getItemStack();
        Player player = event.getEntity();
        if (entity == null) return;
        if (!(entity instanceof AbstractTileEntityGrave grave)) return;
        if (!event.getEntity().isShiftKeyDown()) return;
        if (stack.getItem() != Items.TOTEM_OF_UNDYING) return;
        event.setCanceled(true);
        final IGraveData gData = grave.getGraveData();
        Component msg;
        if (gData == null) {
            msg = Component.translatable("com.sxtkl.easycolony.event.resurrect.no_grave_data").withStyle(ChatFormatting.GRAY);
            player.sendSystemMessage(msg);
            return;
        }
        final IColony colony = IColonyManager.getInstance().getIColony(level, pos);
        if (colony == null) {
            msg = Component.translatable("com.sxtkl.easycolony.event.resurrect.no_colony").withStyle(ChatFormatting.GRAY);
            player.sendSystemMessage(msg);
            return;
        }
        if (gData.getCitizenDataNBT() == null) return;
        if (level.random.nextDouble() <= colony.getOverallHappiness() / 10) {
            final ICitizenData citizenData = colony.getCitizenManager().resurrectCivilianData(gData.getCitizenDataNBT(), true, level, pos);
            colony.getCitizenManager().updateCitizenMourn(citizenData, false);
            level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            player.playSound(SoundEvents.TOTEM_USE, 1.0f, 1.0f);
            level.broadcastEntityEvent(player, (byte) 35);
            msg = Component.translatable("com.sxtkl.easycolony.event.resurrect.success" + level.random.nextInt(4), gData.getCitizenName()).withStyle(ChatFormatting.GREEN);
        } else {
            msg = Component.translatable("com.sxtkl.easycolony.event.resurrect.fail" + level.random.nextInt(4), gData.getCitizenName()).withStyle(ChatFormatting.GRAY);
        }
        player.sendSystemMessage(msg);
        if (player.isCreative()) return;
        stack.shrink(1);
    }
}
