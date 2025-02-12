package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.core.client.gui.WindowPostBox;
import com.sxtkl.easycolony.core.manager.LinkageManager;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Locale;
import java.util.function.Predicate;

@Mixin(WindowPostBox.class)
public abstract class WindowPostBoxMixin {

    @Shadow(remap = false)
    private String filter;

    @ModifyVariable(method = "updateResources", at = @At("STORE"), ordinal = 0, remap = false)
    private Predicate<ItemStack> updateResources$filterPredicate(Predicate<ItemStack> filterPredicate) {
        if (LinkageManager.useJEAMatch) {
            return stack -> filter.isEmpty()
                    || LinkageManager.invokeMatch(stack.getDescriptionId().toLowerCase(Locale.US), filter.toLowerCase(Locale.US))
                    || LinkageManager.invokeMatch(stack.getHoverName().getString().toLowerCase(Locale.US), filter.toLowerCase(Locale.US))
                    || (stack.getItem() instanceof EnchantedBookItem && LinkageManager.invokeMatch(EnchantedBookItem.getEnchantments(stack)
                    .getCompound(0)
                    .getString("id"), filter.toLowerCase(Locale.US)));
        }
        return filterPredicate;
    }

}
