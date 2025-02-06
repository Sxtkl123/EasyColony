package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.api.colony.workorders.IWorkOrder;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingBuilder;
import com.minecolonies.core.colony.jobs.JobBuilder;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIStructureWithWorkOrder;
import com.minecolonies.core.entity.ai.workers.builder.EntityAIStructureBuilder;
import com.sxtkl.easycolony.Config;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = EntityAIStructureBuilder.class, remap = false)
public abstract class EntityAIStructureBuilderMixin extends AbstractEntityAIStructureWithWorkOrder<JobBuilder, BuildingBuilder> {

    public EntityAIStructureBuilderMixin(@NotNull JobBuilder job) {
        super(job);
    }

    // 注入修改土木工人寻路逻辑
    @ModifyVariable(method = "walkToConstructionSite", at = @At("HEAD"), argsOnly = true, remap = false)
    public BlockPos walkTo(BlockPos value) {
        if (!Config.easyBuilderAI) {
            return value;
        }
        final IWorkOrder wo = job.getWorkOrder();
        return wo.getLocation();
    }
}
