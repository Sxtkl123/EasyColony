package com.sxtkl.easycolony.core.util;

import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EasyItemStackUtils {

    public static List<ItemStack> getItemStackByTimes(final ItemStack stack, int times) {
        List<ItemStack> res = new ArrayList<>();
        int curCnt = stack.getCount() * times;
        while (curCnt > 0) {
            int cnt = Math.min(stack.getMaxStackSize(), curCnt);
            curCnt -= cnt;
            ItemStack copy = stack.copy();
            copy.setCount(cnt);
            res.add(copy);
        }
        return Collections.unmodifiableList(res);
    }

}
