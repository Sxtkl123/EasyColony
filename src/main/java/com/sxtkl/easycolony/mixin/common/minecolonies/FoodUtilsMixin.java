package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.api.util.FoodUtils;
import com.sxtkl.easycolony.Config;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(value = FoodUtils.class, remap = false)
public abstract class FoodUtilsMixin {

    @ModifyConstant(method = "getFoodValue(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;D)D", constant = @Constant(doubleValue = 0.25D))
    private static double getFoodValue$houseLevel(double constant) {
        return Config.notColonyFoodPenaltyMultiplier;
    }

}
