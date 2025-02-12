package com.sxtkl.easycolony.core.manager;

import com.sxtkl.easycolony.Easycolony;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 这个类是处理所有联动模组的，这些联动模组可能并不存在，所以需要通过反射尝试获取其中的方法。
 * 我也不确定这种写法是否合适，但是他确实能运行...
 */
public class LinkageManager {

    public static boolean useJEAMatch = false;

    public static Method match;

    public static void setup() {
        useJEAMatch = true;
        try {
            Class<?> clazz = Class.forName("me.towdium.jecharacters.utils.Match");
            match = clazz.getMethod("contains", String.class, CharSequence.class);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            useJEAMatch = false;
        }
    }

    public static boolean invokeMatch(String s, CharSequence cs) {
        try {
            return (boolean) match.invoke(null, s, cs);
        } catch (IllegalAccessException | InvocationTargetException e) {
            Easycolony.LOGGER.error("Cannot invoke Match.");
        }
        return false;
    }

}
