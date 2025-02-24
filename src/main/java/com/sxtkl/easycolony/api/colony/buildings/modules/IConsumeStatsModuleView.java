package com.sxtkl.easycolony.api.colony.buildings.modules;

import com.minecolonies.api.colony.buildings.modules.IBuildingModuleView;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.Tuple;

import java.util.List;

/**
 * 为建筑物添加消耗统计的模型（消耗暂时仅包含放置消耗）
 */
public interface IConsumeStatsModuleView extends IBuildingModuleView {

    List<Tuple<ItemStorage, Integer>> getConsume();

}
