package com.sxtkl.easycolony.api.colony.buildings.modules;

import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import net.minecraft.world.item.ItemStack;

/**
 * 为建筑物添加消耗统计的模型（消耗暂时仅包含放置消耗）
 */
public interface IConsumeStatsModule extends IBuildingModule {

    void increase(final ItemStack itemStack);

    void clear();

    void remove(final ItemStack itemStack);

}
