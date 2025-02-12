package com.sxtkl.easycolony.mixin.client.minecolonies;

import com.minecolonies.core.client.gui.WindowSelectRes;
import com.sxtkl.easycolony.core.manager.LinkageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WindowSelectRes.class)
public abstract class WindowSelectResMixin {

    @Redirect(method = "updateResources", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"), remap = false)
    private boolean updateResources$contains(String instance, CharSequence s) {
        return LinkageManager.match(instance, s);
    }

}
