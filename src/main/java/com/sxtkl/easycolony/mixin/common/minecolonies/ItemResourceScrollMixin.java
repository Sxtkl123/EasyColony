package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.core.items.ItemResourceScroll;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemResourceScroll.class)
public abstract class ItemResourceScrollMixin {

    @Shadow
    private static void openWindow(CompoundTag compound, Player player) {
    }

    @Redirect(method = "useOn", at = @At(value = "INVOKE", target = "Lcom/minecolonies/core/items/ItemResourceScroll;openWindow(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/entity/player/Player;)V"))
    private void useOn$openWindow(CompoundTag warehouseSnapshotCompound, Player currentHash) {
        if (currentHash.isCrouching()) {
            return;
        }
        openWindow(warehouseSnapshotCompound, currentHash);
    }

}
