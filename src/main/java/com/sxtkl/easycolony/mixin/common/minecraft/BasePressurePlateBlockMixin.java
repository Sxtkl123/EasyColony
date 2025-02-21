package com.sxtkl.easycolony.mixin.common.minecraft;

import com.sxtkl.easycolony.extension.ISilenceExtension;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BasePressurePlateBlock;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BasePressurePlateBlock.class)
public abstract class BasePressurePlateBlockMixin extends Block implements ISilenceExtension {

    public BasePressurePlateBlockMixin(Properties pProperties) {
        super(pProperties);
    }

    @Redirect(method = "checkPressed", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/core/BlockPos;Lnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;)V"))
    private void checkPressed$playSound(Level instance, Player player, BlockPos blockPos, SoundEvent soundEvent, SoundSource soundSource) {
        if (getSilence()) return;
        instance.playSound(player, blockPos, soundEvent, soundSource);
    }

    @Unique
    public boolean getSilence() {
        return false;
    }

}
