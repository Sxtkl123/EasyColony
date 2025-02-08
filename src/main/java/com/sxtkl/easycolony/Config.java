package com.sxtkl.easycolony;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static final ForgeConfigSpec SPEC;

    /**
     * 简易化建筑工人AI：建筑工人将固定在施工现场的工作方块附近建筑。
     */
    private static final ForgeConfigSpec.BooleanValue EASY_BUILDER_AI;

    /**
     * 简易化材料寻找AI：工人在架子上找原材料时，将直接在对应的小屋方块建筑进行。
     */
    private static final ForgeConfigSpec.BooleanValue EASY_PICK_MATERIAL_AI;

    /**
     * 建筑工人间隔模式：启用后建筑工人的状态转换间隔将采用固定值，即放置、破坏方块间隔将采用固定值，同时建筑工人间隔倍率选项失效。
     */
    private static final ForgeConfigSpec.ConfigValue<String> BUILDER_DELAY_MODE;

    /**
     * 建筑工人间隔倍率：扩大或缩小建筑工人建筑、破坏的间隔倍率，只有建筑工人固定间隔设置为 magnification 时，该选项生效。
     */
    private static final ForgeConfigSpec.DoubleValue BUILDER_DELAY_MAGNIFICATION;

    /**
     * 建筑工人间隔：建筑工人间隔的固定值，只有建筑工人固定间隔设置为 fixed 时，该选项生效。
     */
    private static final ForgeConfigSpec.IntValue BUILDER_FIXED_DELAY;

    /**
     * 非殖民地食物惩罚倍率：市民食用非殖民地食物带来的饱食度惩罚倍率，原版为缩减倍率至：1 + 住宅等级<br>
     * 如：对于一个5级住宅的市民，非殖民地食物将只能带来原本1/6的饱食度收益。<br>
     * 在此基础上你可以缩减惩罚倍率，其原理是对住宅等级执行一次惩罚，如惩罚倍率设为0.5，那么原本5级住宅将视为2级。
     */
    private static final ForgeConfigSpec.DoubleValue NOT_COLONY_FOOD_PENALTY_MULTIPLIER;

    private static final ForgeConfigSpec.BooleanValue ALLOW_RESURRECT;

    private static final ForgeConfigSpec.ConfigValue<String> RESURRECT_ITEM;

    private static final ForgeConfigSpec.DoubleValue RESURRECT_CHANCE_MULTIPLIER;

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
        NOT_COLONY_FOOD_PENALTY_MULTIPLIER = BUILDER.comment("非殖民地食物惩罚倍率：市民食用非殖民地食物带来的饱食度惩罚倍率，原版为缩减倍率至：1 + 住宅等级\n如：对于一个5级住宅的市民，非殖民地食物将只能带来原本1/6的饱食度收益。\n在此基础上你可以缩减惩罚倍率，其原理是对住宅等级执行一次乘法，如乘法设为0.5，那么原本5级住宅将视为2级。")
                .defineInRange("not_colony_food_penalty_multiplier", 1.0, 0, Integer.MAX_VALUE);
        ALLOW_RESURRECT = BUILDER.comment("是否允许玩家手动复活市民：如果允许，玩家将可以使用指定物品复活市民，成功率取决于总幸福度和墓地等级。")
                .define("allow_resurrect", true);
        RESURRECT_ITEM = BUILDER.comment("复活市民所需物品：指定玩家复活市民所需的物品。")
                .define("resurrect_item", "minecraft:totem_of_undying");
        RESURRECT_CHANCE_MULTIPLIER = BUILDER.comment("复活市民几率乘数：会在原有的复活概率基础上额外乘以一个系数。")
                .defineInRange("resurrect_chance_multiplier", 1.0, 0, Integer.MAX_VALUE);
        SPEC = BUILDER.build();
    }

    public static boolean easyBuilderAI;

    public static boolean easyPickMaterialAI;

    public static String builderDelayMode;

    public static double builderDelayMagnification;

    public static int builderFixedDelay;

    public static double notColonyFoodPenaltyMultiplier;

    public static boolean allowResurrect;

    public static Item resurrectItem;

    public static double resurrectChanceMultiplier;

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent event) {
        easyBuilderAI = EASY_BUILDER_AI.get();
        easyPickMaterialAI = EASY_PICK_MATERIAL_AI.get();
        builderFixedDelay = BUILDER_FIXED_DELAY.get();
        builderDelayMode = BUILDER_DELAY_MODE.get();
        builderDelayMagnification = BUILDER_DELAY_MAGNIFICATION.get();
        notColonyFoodPenaltyMultiplier = NOT_COLONY_FOOD_PENALTY_MULTIPLIER.get();
        allowResurrect = ALLOW_RESURRECT.get();
        resurrectItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(RESURRECT_ITEM.get()));
        resurrectChanceMultiplier = RESURRECT_CHANCE_MULTIPLIER.get();
    }
}
