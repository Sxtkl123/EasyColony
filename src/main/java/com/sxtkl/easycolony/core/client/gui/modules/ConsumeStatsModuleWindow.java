package com.sxtkl.easycolony.core.client.gui.modules;

import com.ldtteam.blockui.Pane;
import com.ldtteam.blockui.controls.Button;
import com.ldtteam.blockui.controls.ItemIcon;
import com.ldtteam.blockui.controls.Text;
import com.ldtteam.blockui.views.ScrollingList;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.core.Network;
import com.minecolonies.core.client.gui.AbstractModuleWindow;
import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.colony.buildings.modules.IConsumeStatsModuleView;
import com.sxtkl.easycolony.core.network.messages.server.colony.building.RemoveConsumeStatsFromBuildingModuleMessage;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import static com.minecolonies.api.util.constant.WindowConstants.*;

@SuppressWarnings("DataFlowIssue")
public class ConsumeStatsModuleWindow extends AbstractModuleWindow {

    private static final String RESOURCE_STRING = ":gui/layouthuts/layoutconsumestats.xml";

    private static final String REMOVE_BUTTON = "removeConsume";

    private final ScrollingList resourceList;

    private final IConsumeStatsModuleView moduleView;

    public ConsumeStatsModuleWindow(final IBuildingView building, final IConsumeStatsModuleView moduleView) {
        super(building, Easycolony.MODID + RESOURCE_STRING);
        resourceList = this.window.findPaneOfTypeByID("resources_consumed", ScrollingList.class);
        this.moduleView = moduleView;
        registerButton(REMOVE_BUTTON, this::removeConsume);
    }

    @Override
    public void onOpened() {
        super.onOpened();
        updateConsumedList();
    }

    private void updateConsumedList() {
        resourceList.enable();
        resourceList.show();
        resourceList.setDataProvider(new ScrollingList.DataProvider() {
            @Override
            public int getElementCount() {
                return moduleView.getConsume().size();
            }

            @Override
            public void updateElement(final int index, @NotNull final Pane rowPane) {
                final ItemStack resource = moduleView.getConsume().get(index).getA().getItemStack().copy();
                resource.setCount(1);

                rowPane.findPaneOfTypeByID(RESOURCE_NAME, Text.class).setText(resource.getHoverName());
                rowPane.findPaneOfTypeByID(QUANTITY_LABEL, Text.class).setText(Component.literal(String.valueOf(moduleView.getConsume().get(index).getB())));
                rowPane.findPaneOfTypeByID(RESOURCE_ICON, ItemIcon.class).setItem(resource);
            }
        });
    }

    private void removeConsume(final Button button) {
        final int row = resourceList.getListElementIndexByPane(button);
        final Tuple<ItemStorage, Integer> tuple = moduleView.getConsume().get(row);
        moduleView.getConsume().remove(row);
        Network.getNetwork().sendToServer(new RemoveConsumeStatsFromBuildingModuleMessage(buildingView, tuple.getA().getItemStack()));
        updateConsumedList();
    }
}
