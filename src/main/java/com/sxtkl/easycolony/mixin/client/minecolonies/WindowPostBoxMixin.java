package com.sxtkl.easycolony.mixin.client.minecolonies;

import com.minecolonies.core.client.gui.WindowPostBox;
import com.sxtkl.easycolony.core.manager.LinkageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WindowPostBox.class)
public abstract class WindowPostBoxMixin {

    @Redirect(method = "lambda$updateResources$1", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"), remap = false)
    private boolean updateResources$contain(String instance, CharSequence s) {
        return LinkageManager.match(instance, s);
    }
}
