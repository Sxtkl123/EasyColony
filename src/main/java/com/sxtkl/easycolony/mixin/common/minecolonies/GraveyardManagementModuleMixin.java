package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.api.colony.GraveData;
import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingEventsModule;
import com.minecolonies.api.colony.buildings.modules.IBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IPersistentModule;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.core.colony.buildings.modules.GraveyardManagementModule;
import com.sxtkl.easycolony.extension.IGraveDataExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;

@Mixin(value = GraveyardManagementModule.class, remap = false)
public abstract class GraveyardManagementModuleMixin extends AbstractBuildingModule implements IBuildingModule, IPersistentModule, IBuildingEventsModule {

    @Nullable
    @Shadow(remap = false)
    private GraveData lastGraveData;

    @Inject(method = "buryCitizenHere", at = @At(value = "INVOKE", target = "Lcom/minecolonies/core/tileentities/TileEntityNamedGrave;setTextLines(Ljava/util/ArrayList;)V"
    , shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    public void buryCitizenHere(Tuple<BlockPos, Direction> positionAndDirection, AbstractEntityCitizen worker, CallbackInfo ci, IColony colony, Direction facing, BlockEntity tileEntity, String firstName, String lastName, ArrayList<String> lines) {
        IGraveDataExtension graveDataExtension = (IGraveDataExtension) tileEntity;
        graveDataExtension.setGraveData(lastGraveData);
    }

}
