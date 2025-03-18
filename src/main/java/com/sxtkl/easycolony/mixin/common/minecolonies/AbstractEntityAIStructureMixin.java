package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.core.colony.buildings.AbstractBuildingStructureBuilder;
import com.minecolonies.core.colony.jobs.AbstractJobStructure;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIInteract;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIStructure;
import com.sxtkl.easycolony.Config;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = AbstractEntityAIStructure.class, remap = false)
public abstract class AbstractEntityAIStructureMixin<J extends AbstractJobStructure<?, J>, B extends AbstractBuildingStructureBuilder> extends AbstractEntityAIInteract<J, B> {

    public AbstractEntityAIStructureMixin(@NotNull J job) {
        super(job);
    }

    @Final
    @ModifyArg(method = "structureStep", at = @At(value = "INVOKE", target = "Lcom/minecolonies/core/entity/ai/workers/AbstractEntityAIStructure;setDelay(I)V"), index = 0, remap = false)
    public int structureStep$setDelay(int timeout) {
        if ("fixed".equals(Config.builderDelayMode)) return Config.builderFixedDelay;
        if ("magnification".equals(Config.builderDelayMode)) return (int) (timeout * Config.builderDelayMagnification);
        return timeout;
    }

}
