package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.util.Tuple;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.jobs.AbstractJob;
import com.minecolonies.core.entity.ai.workers.AbstractAISkeleton;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIBasic;
import com.sxtkl.easycolony.Config;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Predicate;

@Mixin(AbstractEntityAIBasic.class)
public abstract class AbstractEntityAIBasicMixin<J extends AbstractJob<?, J>, B extends AbstractBuilding> extends AbstractAISkeleton<J> {

    @Final
    @Shadow(remap = false)
    private static int PICKUP_ATTEMPTS;

    @Shadow(remap = false)
    protected Tuple<Predicate<ItemStack>, Integer> needsCurrently;

    @Shadow(remap = false)
    protected BlockPos walkTo;

    @Shadow(remap = false)
    private int pickUpCounter;

    @Final
    @Shadow(remap = false)
    public B building;

    protected AbstractEntityAIBasicMixin(@NotNull J job) {
        super(job);
    }

    @Shadow(remap = false)
    public abstract IAIState getStateAfterPickUp();

    @Shadow(remap = false)
    protected abstract boolean tryTransferFromPosToWorkerIfNeeded(final BlockPos pos, @NotNull final Tuple<Predicate<ItemStack>, Integer> predicate);

    @Final
    @Shadow(remap = false)
    protected abstract boolean walkToWorkPos(final BlockPos pos);

    @Inject(method = "getNeededItem", at = @At("HEAD"), remap = false, cancellable = true)
    private void getNeededItem(CallbackInfoReturnable<IAIState> cir) {
        if (Config.easyPickMaterialAI) {
            if (needsCurrently == null) {
                cir.setReturnValue(getStateAfterPickUp());
                return;
            } else {
                if (walkTo == null) {
                    final BlockPos pos = building.getTileEntity().getTilePos();
                    if (pos == null) {
                        cir.setReturnValue(getStateAfterPickUp());
                        return;
                    }
                    walkTo = pos;
                }

                if (!walkToWorkPos(walkTo) && pickUpCounter++ < PICKUP_ATTEMPTS) {
                    cir.setReturnValue(getState());
                    return;
                }

                pickUpCounter = 0;

                if (!tryTransferFromPosToWorkerIfNeeded(walkTo, needsCurrently)) {
                    walkTo = null;
                    cir.setReturnValue(getState());
                    return;
                }
            }

            walkTo = null;
            cir.setReturnValue(getStateAfterPickUp());
        }
    }
}
