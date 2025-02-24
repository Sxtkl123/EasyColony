package com.sxtkl.easycolony.core.colony.buildings.modules;

import com.ldtteam.blockui.views.BOWindow;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModuleView;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.Tuple;
import com.sxtkl.easycolony.api.colony.buildings.modules.IConsumeStatsModuleView;
import com.sxtkl.easycolony.core.client.gui.modules.ConsumeStatsModuleWindow;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ConsumeStatsModuleView extends AbstractBuildingModuleView implements IConsumeStatsModuleView {

    private final List<Tuple<ItemStorage, Integer>> consume = new ArrayList<>();

    @Override
    public void deserialize(@NotNull FriendlyByteBuf buf) {
        consume.clear();
        final int size = buf.readInt();
        for (int i = 0; i < size; i++) {
            consume.add(new Tuple<>(new ItemStorage(buf.readItem()), buf.readInt()));
        }
    }

    @Override
    public List<Tuple<ItemStorage, Integer>> getConsume() {
        return consume;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public BOWindow getWindow() {
        return new ConsumeStatsModuleWindow(buildingView, this);
    }

    @Override
    public String getIcon() {
        return "consume_stats";
    }

    @Override
    public String getDesc() {
        return "com.sxtkl.easycolony.gui.builder.consume_stats";
    }
}
