package com.sxtkl.easycolony.mixin.client.minecolonies;

import com.minecolonies.core.client.gui.WindowHutAllInventory;
import com.sxtkl.easycolony.core.manager.LinkageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WindowHutAllInventory.class)
public abstract class WindowHutAllInventoryMixin {

    @Redirect(method = "lambda$updateResources$2", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"), remap = false)
    private boolean updateResources$filterPredicate(String instance, CharSequence s) {
        return LinkageManager.match(instance, s);
    }
}
