package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.api.colony.workorders.IWorkOrder;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.util.BlockPosUtil;
import com.minecolonies.core.colony.buildings.workerbuildings.BuildingBuilder;
import com.minecolonies.core.colony.jobs.JobBuilder;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIStructureWithWorkOrder;
import com.minecolonies.core.entity.ai.workers.builder.EntityAIStructureBuilder;
import com.minecolonies.core.entity.pathfinding.navigation.MinecoloniesAdvancedPathNavigate;
import com.minecolonies.core.entity.pathfinding.pathjobs.AbstractPathJob;
import com.minecolonies.core.entity.pathfinding.pathjobs.PathJobMoveCloseToXNearY;
import com.minecolonies.core.entity.pathfinding.pathresults.PathResult;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityAIStructureBuilder.class, remap = false)
public abstract class EntityAIStructureBuilderMixin extends AbstractEntityAIStructureWithWorkOrder<JobBuilder, BuildingBuilder> {

    @Shadow(remap = false)
    PathResult<AbstractPathJob> gotoPath;

    public EntityAIStructureBuilderMixin(@NotNull JobBuilder job) {
        super(job);
    }

    @Inject(method = "walkToConstructionSite", at = @At("HEAD"), remap = false, cancellable = true)
    public void walkToConstructionSite(final BlockPos currentBlock, CallbackInfoReturnable<Boolean> cir) {
        final IWorkOrder wo = job.getWorkOrder();
        BlockPos pos = wo.getLocation();
        if (workFrom != null && workFrom.getX() == pos.getX() && workFrom.getZ() == pos.getZ() && workFrom.getY() >= pos.getY()) {
            workFrom = null;
        }

        if (workFrom == null) {
            if (gotoPath == null || gotoPath.isCancelled()) {
                final PathJobMoveCloseToXNearY pathJob = new PathJobMoveCloseToXNearY(world,
                        pos,
                        job.getWorkOrder().getLocation(),
                        4,
                        worker);
                gotoPath = ((MinecoloniesAdvancedPathNavigate) worker.getNavigation()).setPathJob(pathJob, pos, 1.0, false);
                pathJob.getPathingOptions().dropCost = 200;
                pathJob.extraNodes = 0;
            }
            else if (gotoPath.isDone()) {
                if (gotoPath.getPath() != null) {
                    workFrom = gotoPath.getPath().getTarget();
                }
                gotoPath = null;
            }
            cir.setReturnValue(false);
            return;
        }

        if (!walkToSafePos(workFrom)) {
            cir.setReturnValue(false);
            return;
        }

        if (BlockPosUtil.getDistance2D(worker.blockPosition(), pos) > 5) {
            double distToBuilding = BlockPosUtil.dist(workFrom, job.getWorkOrder().getLocation());
            workFrom = null;
            cir.setReturnValue(distToBuilding < 100);
            return;
        }

        cir.setReturnValue(true);
    }
}
