package com.sxtkl.easycolony.core.event.client;

import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.block.TestBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class HideModelEvent {

    @SubscribeEvent
    public static void onPlayerChangeGameMode(PlayerEvent.PlayerChangeGameModeEvent event) {
        BlockPos blockPos = event.getEntity().blockPosition();
        BlockPos.betweenClosed(blockPos.offset(-8, -8, -8), blockPos.offset(8, 8, 8)).forEach(pos -> {
            BlockState state = Minecraft.getInstance().level.getBlockState(pos);
            if (state.getBlock() instanceof TestBlock) {
                // 触发模型数据更新
                Minecraft.getInstance().levelRenderer.blockChanged(Minecraft.getInstance().level, pos, state, state, Block.UPDATE_ALL);
            }
        });
    }

}
