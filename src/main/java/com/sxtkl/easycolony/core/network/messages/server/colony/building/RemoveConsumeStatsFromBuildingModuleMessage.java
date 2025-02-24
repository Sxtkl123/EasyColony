package com.sxtkl.easycolony.core.network.messages.server.colony.building;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.IBuilding;
import com.minecolonies.api.colony.buildings.views.IBuildingView;
import com.minecolonies.core.network.messages.server.AbstractBuildingServerMessage;
import com.sxtkl.easycolony.core.colony.buildings.modules.BuildingModules;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class RemoveConsumeStatsFromBuildingModuleMessage extends AbstractBuildingServerMessage<IBuilding> {

    private ItemStack stack;

    public RemoveConsumeStatsFromBuildingModuleMessage() {
        super();
    }

    public RemoveConsumeStatsFromBuildingModuleMessage(final IBuildingView building, final ItemStack stack) {
        super(building);
        this.stack = stack;
    }

    @Override
    protected void onExecute(NetworkEvent.Context ctxIn, boolean isLogicalServer, IColony colony, IBuilding building) {
        if (!building.hasModule(BuildingModules.CONSUME_STATS)) {
            return;
        }
        building.getModule(BuildingModules.CONSUME_STATS).remove(stack);
    }

    @Override
    protected void toBytesOverride(FriendlyByteBuf buf) {
        buf.writeItem(stack);
    }

    @Override
    protected void fromBytesOverride(FriendlyByteBuf buf) {
        stack = buf.readItem();
    }
}
