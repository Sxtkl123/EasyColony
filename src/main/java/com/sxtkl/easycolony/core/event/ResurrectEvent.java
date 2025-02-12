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
import com.sxtkl.easycolony.Config;
import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.extension.IGraveDataExtension;
import com.sxtkl.easycolony.mixin.accessor.minecolonies.GraveyardManagementModuleAccessor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.Map;


@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ResurrectEvent {

    @SubscribeEvent
    public static void onPlayerInteract$RightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        if (event.isCanceled()) return;
        if (!Config.allowResurrect) return;

        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockEntity entity = level.getBlockEntity(pos);
        ItemStack stack = event.getItemStack();

        if (entity == null) return;
        if (!event.getEntity().isShiftKeyDown()) return;
        if (stack.getItem() != Config.resurrectItem) return;
        if (!(entity instanceof AbstractTileEntityGrave) && !(entity instanceof AbstractTileEntityNamedGrave)) return;
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.CONSUME);
        if (event.getSide().isClient()) return;

        if (entity instanceof AbstractTileEntityGrave grave) {
            IColony colony = IColonyManager.getInstance().getIColony(level, pos);
            resurrect(colony, level, pos, event.getEntity(), grave.getGraveData());
        } else if (entity instanceof AbstractTileEntityNamedGrave grave) {
            IColony colony = IColonyManager.getInstance().getIColony(level, pos);
            IGraveData graveData = ((IGraveDataExtension) grave).getGraveData();
            resurrect(colony, level, pos, event.getEntity(), graveData);
            GraveyardManagementModule module = getGraveyardManagementModule(colony, graveData.getCitizenName());
            if (module != null) {
                ((GraveyardManagementModuleAccessor) module).getRestingCitizen().remove(graveData.getCitizenName());
                module.markDirty();
            }
        }

        if (event.getEntity().isCreative()) return;
        stack.shrink(1);
    }

    private static void resurrect(IColony colony, Level level, BlockPos pos, Player player, IGraveData gData) {
        Component msg;
        if (gData == null) {
            msg = Component.translatable("com.sxtkl.easycolony.event.resurrect.no_grave_data").withStyle(ChatFormatting.GRAY);
            player.sendSystemMessage(msg);
            return;
        }
        if (colony == null) {
            msg = Component.translatable("com.sxtkl.easycolony.event.resurrect.no_colony").withStyle(ChatFormatting.GRAY);
            player.sendSystemMessage(msg);
            return;
        }
        if (gData.getCitizenDataNBT() == null) return;
        if (level.random.nextDouble() <= getResurrectChance(colony)) {
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
    }

    private static double getResurrectChance(IColony colony) {
        Map<BlockPos, IBuilding> buildings = colony.getBuildingManager().getBuildings();
        int max = 0;
        for (IBuilding building : buildings.values()) {
            if (building instanceof BuildingGraveyard graveyard) {
                max = Math.max(max, graveyard.getBuildingLevel());
            }
        }

        double happiness = colony.getOverallHappiness();
        return happiness / 10 * (max + 1) / 5 * Config.resurrectChanceMultiplier;
    }

    @Nullable
    private static GraveyardManagementModule getGraveyardManagementModule(IColony colony, String citizenName) {
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
