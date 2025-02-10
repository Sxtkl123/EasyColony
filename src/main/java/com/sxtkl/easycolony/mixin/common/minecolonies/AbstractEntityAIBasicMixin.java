package com.sxtkl.easycolony.mixin.common.minecolonies;

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
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Predicate;

@Mixin(AbstractEntityAIBasic.class)
@SuppressWarnings({"DataFlowIssue"})
public abstract class AbstractEntityAIBasicMixin<J extends AbstractJob<?, J>, B extends AbstractBuilding> extends AbstractAISkeleton<J> {

    @Final
    @Shadow(remap = false)
    public B building;

    @Shadow(remap = false)
    protected Tuple<Predicate<ItemStack>, Integer> needsCurrently;

    protected AbstractEntityAIBasicMixin(@NotNull J job) {
        super(job);
    }

    // 修改工人取货逻辑，使其在工作方块处取货
    @ModifyVariable(method = "getNeededItem", at = @At("STORE"), ordinal = 0, remap = false)
    private BlockPos getNeedItem$pos(BlockPos pos) {
        if (!Config.easyPickMaterialAI) return pos;
        BlockPos ori = building.getTileEntity().getPositionOfChestWithItemStack(needsCurrently.getA());
        return ori == null ? null : building.getTileEntity().getTilePos();
    }
}
