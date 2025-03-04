package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.api.colony.jobs.IJob;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIStructure;
import com.minecolonies.core.entity.ai.workers.util.BuildingStructureHandler;
import com.sxtkl.easycolony.core.colony.buildings.modules.BuildingModules;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BuildingStructureHandler.class)
public abstract class BuildingStructureHandlerMixin {

    @Final
    @Shadow(remap = false)
    private AbstractEntityAIStructure<?, ?> structureAI;

    @Shadow(remap = false)
    public abstract IItemHandler getInventory();

    @Redirect(
            method = "consume",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/minecolonies/api/util/InventoryUtils;reduceStackInItemHandler(Lnet/minecraftforge/items/IItemHandler;Lnet/minecraft/world/item/ItemStack;)V"
            ),
            remap = false)
    public void consume(IItemHandler invWrapper, ItemStack itemStack) {
        InventoryUtils.reduceStackInItemHandler(this.getInventory(), itemStack);
        IJob<?> colonyJob = structureAI.getWorker().getCitizenJobHandler().getColonyJob();
        if (colonyJob == null) {
            return;
        }
        if (!colonyJob.getWorkBuilding().hasModule(BuildingModules.CONSUME_STATS)) {
            return;
        }
        colonyJob.getWorkBuilding().getModule(BuildingModules.CONSUME_STATS).increase(itemStack);
    }

}
