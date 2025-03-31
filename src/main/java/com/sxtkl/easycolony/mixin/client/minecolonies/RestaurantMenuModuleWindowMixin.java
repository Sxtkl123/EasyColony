package com.sxtkl.easycolony.mixin.client.minecolonies;

import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.core.client.gui.modules.RestaurantMenuModuleWindow;
import com.sxtkl.easycolony.api.item.ModTags;
import com.sxtkl.easycolony.core.manager.LinkageManager;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(RestaurantMenuModuleWindow.class)
public abstract class RestaurantMenuModuleWindowMixin {

    @Redirect(method = "lambda$updateResources$1", at = @At(value = "INVOKE", target = "Ljava/lang/String;contains(Ljava/lang/CharSequence;)Z"), remap = false)
    private boolean updateResources$filterPredicate(String instance, CharSequence s) {
        return LinkageManager.match(instance, s);
    }

    @Redirect(method = "updateResources", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z"), remap = false)
    private boolean updateResources$add(List instance, Object e) {
        if (((ItemStorage) e).getItemStack().is(ModTags.DELETED)) return false;
        return instance.add(e);
    }

}
