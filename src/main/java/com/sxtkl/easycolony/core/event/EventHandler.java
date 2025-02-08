package com.sxtkl.easycolony.core.event;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.IGraveData;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.tileentities.AbstractTileEntityGrave;
import com.minecolonies.api.tileentities.AbstractTileEntityNamedGrave;
import com.minecolonies.core.colony.buildings.modules.GraveyardManagementModule;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingGraveyard;
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
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;


public class EventHandler {

    @SubscribeEvent
    public static void onPlayerInteract$RightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockEntity entity = level.getBlockEntity(pos);
        ItemStack stack = event.getItemStack();

        if (entity == null) return;
        if (!event.getEntity().isShiftKeyDown()) return;
        if (stack.getItem() != Items.TOTEM_OF_UNDYING) return;
        if (!(entity instanceof AbstractTileEntityGrave) && !(entity instanceof AbstractTileEntityNamedGrave)) return;
        event.setCanceled(true);
        if (event.getSide().isClient()) return;

        if (entity instanceof AbstractTileEntityGrave grave) {
            resurrectGrave(level, pos, event.getEntity(), stack, grave);
        } else if (entity instanceof AbstractTileEntityNamedGrave grave) {
            event.setCanceled(true);
            if (event.getSide().isClient()) return;
            // TODO: 编写有名字的墓碑的相关代码。
            resurrectNamedGrave(level, pos, event.getEntity(), stack, grave);
        }
    }

    private static void resurrectGrave(Level level, BlockPos pos, Player player, ItemStack stack, AbstractTileEntityGrave grave) {
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

    private static void resurrectNamedGrave(Level level, BlockPos pos, Player player, ItemStack stack, AbstractTileEntityNamedGrave grave) {
        //TODO: 常规方法是行不通了，直接mixin吧！
    }

    @Nullable
    private static GraveyardManagementModule getGraveyardManagementModule(IColony colony, String citizenName, AbstractTileEntityNamedGrave grave) {
        if (colony == null) return null;
        Map<BlockPos, IBuilding> buildings = colony.getBuildingManager().getBuildings();
        for (IBuilding building : buildings.values()) {
            if (building instanceof BuildingGraveyard graveyard) {
                GraveyardManagementModule firstModuleOccurance = graveyard.getFirstModuleOccurance(GraveyardManagementModule.class);
                if (firstModuleOccurance.hasRestingCitizen(Collections.singleton(citizenName))) {
                    return firstModuleOccurance;
                }
            }
        }
        return null;
    }
}
