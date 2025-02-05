package com.sxtkl.easycolony;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC;

    // 简易化建筑工人AI：建筑工人将固定在施工现场的工作方块附近建筑。
    private static final ForgeConfigSpec.BooleanValue EASY_BUILDER_AI;

    // 简易化材料寻找AI：工人在架子上找原材料时，将直接在对应的小屋方块建筑进行。
    private static final ForgeConfigSpec.BooleanValue EASY_PICK_MATERIAL_AI;

    // 建筑工人间隔模式：启用后建筑工人的状态转换间隔将采用固定值，即放置、破坏方块间隔将采用固定值，同时建筑工人间隔倍率选项失效。
    private static final ForgeConfigSpec.ConfigValue<String> BUILDER_DELAY_MODE;

    // 建筑工人间隔倍率：扩大或缩小建筑工人建筑、破坏的间隔倍率，只有建筑工人固定间隔设置为 magnification 时，该选项生效。
    private static final ForgeConfigSpec.DoubleValue BUILDER_DELAY_MAGNIFICATION;

    // 建筑工人间隔：建筑工人间隔的固定值，只有建筑工人固定间隔设置为 fixed 时，该选项生效。
    private static final ForgeConfigSpec.IntValue BUILDER_FIXED_DELAY;

    static {
        EASY_BUILDER_AI = BUILDER.comment("简易化建筑工人AI：建筑工人将固定在施工现场的工作方块附近建筑。")
                .define("easy_builder_ai", true);
        EASY_PICK_MATERIAL_AI = BUILDER.comment("简易化材料寻找AI：工人在架子上找原材料时，将直接在对应的小屋方块建筑进行。")
                .define("easy_pick_material_ai", true);
        BUILDER_DELAY_MODE = BUILDER.comment("建筑工人间隔模式：可选择 fixed 或 magnification，对应下列的建筑工间隔修正模式，选择其他的则视为无效。")
                .define("builder_delay_mode", "off");
        BUILDER_DELAY_MAGNIFICATION = BUILDER.comment("建筑工人间隔倍率：扩大或缩小建筑工人建筑、破坏的间隔倍率，只有建筑工人固定间隔设置为 magnification 时，该选项生效。")
                .defineInRange("builder_delay_magnification", 1.0, 0, Integer.MAX_VALUE);
        BUILDER_FIXED_DELAY = BUILDER.comment("建筑工人间隔：建筑工人间隔的固定值，只有建筑工人固定间隔设置为 fixed 时，该选项生效。")
                .defineInRange("builder_fixed_delay", 15, 0, Integer.MAX_VALUE);
        SPEC = BUILDER.build();
    }

    public static boolean easyBuilderAI;

    public static boolean easyPickMaterialAI;

    public static String builderDelayMode;

    public static double builderDelayMagnification;

    public static int builderFixedDelay;

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        easyBuilderAI = EASY_BUILDER_AI.get();
        easyPickMaterialAI = EASY_PICK_MATERIAL_AI.get();
        builderFixedDelay = BUILDER_FIXED_DELAY.get();
        builderDelayMode = BUILDER_DELAY_MODE.get();
        builderDelayMagnification = BUILDER_DELAY_MAGNIFICATION.get();
    }
}
