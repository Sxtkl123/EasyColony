package com.sxtkl.easycolony.core.colony.buildings.modules;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModuleView;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.core.client.gui.modules.MinimumStockModuleWindow;
import com.sxtkl.easycolony.api.colony.buildings.modules.IConsumeStatsModule;
import com.sxtkl.easycolony.api.colony.buildings.modules.IConsumeStatsModuleView;
import com.sxtkl.easycolony.core.client.gui.modules.ConsumeStatsModleWindow;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ConsumeStatsModuleView extends AbstractBuildingModuleView implements IConsumeStatsModuleView {


    @Override
    public void deserialize(@NotNull FriendlyByteBuf buf) {

    }


    @Override
    @OnlyIn(Dist.CLIENT)
    public BOWindow getWindow() {
        return new ConsumeStatsModleWindow(buildingView, this);
    }

    @Override
    public String getIcon() {
        return "stock";
    }

    @Override
    public String getDesc() {
        return "com.minecolonies.coremod.gui.warehouse.stock";
    }
}
