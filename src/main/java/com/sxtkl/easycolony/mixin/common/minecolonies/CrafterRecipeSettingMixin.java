package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.core.colony.buildings.modules.settings.CrafterRecipeSetting;
import com.minecolonies.core.colony.buildings.modules.settings.StringSettingWithDesc;
import com.sxtkl.easycolony.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = CrafterRecipeSetting.class, remap = false)
public abstract class CrafterRecipeSettingMixin extends StringSettingWithDesc {

    @Inject(method = "<init>()V", at = @At("TAIL"), remap = false)
    private void init(CallbackInfo ci) {
        if (!Config.useMaxStockAsDefault) {
            return;
        }
        this.currentIndex = 1;
    }

    @Inject(method = "isActive*", at = @At("HEAD"), remap = false, cancellable = true)
    private void isActive(CallbackInfoReturnable<Boolean> cir) {
        if (!Config.allowToggleRecipeModeAsDefault) {
            return;
        }
        cir.setReturnValue(true);
        cir.cancel();
    }

}
