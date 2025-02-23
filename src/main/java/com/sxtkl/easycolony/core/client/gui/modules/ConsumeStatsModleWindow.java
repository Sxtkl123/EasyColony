package com.sxtkl.easycolony.core.client.gui.modules;

import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.core.client.gui.AbstractModuleWindow;
import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.colony.buildings.modules.IConsumeStatsModuleView;

public class ConsumeStatsModleWindow extends AbstractModuleWindow {

    private static final String RESOURCE_STRING = ":gui/layouthuts/layoutconsumestats.xml";

    public ConsumeStatsModleWindow(final IBuildingView building, final IConsumeStatsModuleView moduleView) {
        super(building, Easycolony.MODID + RESOURCE_STRING);
    }

}
