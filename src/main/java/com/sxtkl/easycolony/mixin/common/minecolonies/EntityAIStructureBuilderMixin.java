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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = EntityAIStructureBuilder.class, remap = false)
public abstract class EntityAIStructureBuilderMixin extends AbstractEntityAIStructureWithWorkOrder<JobBuilder, BuildingBuilder> {

    public EntityAIStructureBuilderMixin(@NotNull JobBuilder job) {
        super(job);
    }

    // 注入修改土木工人寻路逻辑
    @ModifyVariable(method = "walkToConstructionSite", at = @At("HEAD"), argsOnly = true, remap = false)
    public BlockPos walkToConstructionSite$currentBlock(BlockPos currentBlock) {
        // TODO: 进行性能优化，目前该写法虽然可以解决问题，可是却会进行额外的寻路尝试，尽管和原版的性能开销相同，但是可以进行优化。
        if (!Config.easyBuilderAI) return currentBlock;
        final IWorkOrder wo = job.getWorkOrder();
        return wo.getLocation();
    }
}
