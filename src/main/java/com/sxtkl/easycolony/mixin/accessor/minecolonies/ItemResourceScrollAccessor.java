package com.sxtkl.easycolony.mixin.accessor.minecolonies;

import com.minecolonies.core.items.ItemResourceScroll;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ItemResourceScroll.class)
public interface ItemResourceScrollAccessor {

    @Invoker(value = "openWindow", remap = false)
    void invokeOpenWindow(final CompoundTag compound, final Player player);
}
