package com.sxtkl.easycolony.core.event.common;

import com.minecolonies.api.colony.ICitizenData;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.IGraveData;
import com.minecolonies.api.tileentities.AbstractTileEntityGrave;
import com.minecolonies.api.tileentities.AbstractTileEntityNamedGrave;
import com.minecolonies.core.colony.CitizenData;
import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.core.event.events.common.ItemEntityDeathEvent;
import com.sxtkl.easycolony.extension.IGraveDataExtension;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BurnPaperEvent {

    @SubscribeEvent
    public static void onEntityDie(final ItemEntityDeathEvent event) {
        ItemEntity entity = event.getEntity();
        Entity owner = entity.getOwner();
        if (entity.getItem().getItem() != Items.PAPER) return;
        if (!event.getSource().type().msgId().equals("inFire")) return;
        if (!(owner instanceof Player player)) return;
        Level level = entity.level();
        BlockPos blockPos = entity.blockPosition();
        IColony colony = IColonyManager.getInstance().getIColony(level, blockPos);
        for (Direction direction : Direction.values()) {
            if (direction == Direction.DOWN || direction == Direction.UP) continue;
            if (tryRest(blockPos.relative(direction), level, colony, player)) {
                if (level.getBlockState(blockPos).getBlock().equals(Blocks.FIRE) || level.getBlockState(blockPos).getBlock().equals(Blocks.SOUL_FIRE)) {
                    level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState());
                }
                return;
            }
        }
    }

    private static boolean tryRest(BlockPos pos, Level level, IColony colony, Player player) {
        Component msg;
        if (colony == null) return false;
        BlockEntity entity = level.getBlockEntity(pos);
        if (!(entity instanceof AbstractTileEntityGrave) && !(entity instanceof AbstractTileEntityNamedGrave)) return false;
        IGraveData gData = null;
        if (entity instanceof AbstractTileEntityGrave grave) {
            gData = grave.getGraveData();
            grave.setGraveData(null);
        } else if (entity instanceof AbstractTileEntityNamedGrave grave) {
            gData = ((IGraveDataExtension) grave).getGraveData();
            ((IGraveDataExtension) grave).setGraveData(null);
        }
        if (gData == null || gData.getCitizenDataNBT() == null) {
            msg = Component.translatable("com.sxtkl.easycolony.event.resurrect.no_grave_data").withStyle(ChatFormatting.GRAY);
            player.sendSystemMessage(msg);
            return false;
        }
        ICitizenData citizenData = new CitizenData(gData.getCitizenDataNBT().getInt("id"), colony);
        citizenData.setName(gData.getCitizenName());
        colony.getCitizenManager().updateCitizenMourn(citizenData, false);
        if (level instanceof ServerLevel serverLevel) {
            drawParticle(pos, serverLevel);
            msg = Component.translatable("com.sxtkl.easycolony.event.rest_grave.success", gData.getCitizenName()).withStyle(ChatFormatting.GREEN);
            player.sendSystemMessage(msg);
        }
        return true;
    }

    private static void drawParticle(BlockPos pos, ServerLevel level) {
        double centerX = pos.getCenter().x;
        double centerZ = pos.getCenter().z;
        final double r = 1.25;
        double h = Math.cos(Math.toRadians(60)) * r;
        for (int i = 0; i < 360; i += 10) {
            double vX = Math.cos(Math.toRadians(i)) * r;
            double vZ = Math.sin(Math.toRadians(i)) * r;
            level.sendParticles(
                    ParticleTypes.SOUL_FIRE_FLAME,
                    centerX + vX,
                    pos.getY() + 0.5,
                    centerZ + vZ,
                    0,
                    -vX,
                    h,
                    -vZ,
                    0.1
            );
        }
        level.sendParticles(
                ParticleTypes.SOUL,
                centerX,
                pos.getY() + 0.2,
                centerZ,
                0,
                0,
                h + 0.3,
                0,
                0.1
        );
    }

}
