package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.api.util.FoodUtils;
import com.sxtkl.easycolony.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value = FoodUtils.class, remap = false)
public abstract class FoodUtilsMixin {

    @ModifyVariable(
            method = "getFoodValue(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;ID)D",
            remap = false,
            at = @At("HEAD"),
            argsOnly = true
    )
    private static int getFoodValue$houseLevel(int houseLevel) {
        return (int) (Config.notColonyFoodPenaltyMultiplier * houseLevel);
    }

}
