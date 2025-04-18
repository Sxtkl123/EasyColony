package com.sxtkl.easycolony.mixin.common.domumornamentum;

import com.ldtteam.domumornamentum.block.decorative.TimberFrameBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TimberFrameBlock.class)
public abstract class TimberFrameBlockMixin {

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;noOcclusion()Lnet/minecraft/world/level/block/state/BlockBehaviour$Properties;"))
    private static BlockBehaviour.Properties init$noOcclusion(BlockBehaviour.Properties instance) {
        return instance;
    }

}
