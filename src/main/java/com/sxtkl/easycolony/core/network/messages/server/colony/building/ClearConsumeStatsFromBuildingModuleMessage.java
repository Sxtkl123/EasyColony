package com.sxtkl.easycolony.core.network.messages.server.colony.building;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.core.network.messages.server.AbstractBuildingServerMessage;
import com.sxtkl.easycolony.core.colony.buildings.modules.BuildingModules;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class ClearConsumeStatsFromBuildingModuleMessage extends AbstractBuildingServerMessage<IBuilding> {

    public ClearConsumeStatsFromBuildingModuleMessage() {
        super();
    }

    public ClearConsumeStatsFromBuildingModuleMessage(final IBuildingView building) {
        super(building);
    }

    @Override
    protected void onExecute(NetworkEvent.Context ctxIn, boolean isLogicalServer, IColony colony, IBuilding building) {
        if (!building.hasModule(BuildingModules.CONSUME_STATS)) {
            return;
        }
        building.getModule(BuildingModules.CONSUME_STATS).clear();
    }

    @Override
    protected void toBytesOverride(FriendlyByteBuf buf) {
    }

    @Override
    protected void fromBytesOverride(FriendlyByteBuf buf) {
    }
}
