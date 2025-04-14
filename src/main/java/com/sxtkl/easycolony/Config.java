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

    private static final ForgeConfigSpec.BooleanValue ALLOW_HURT_ALERT;

    private static final ForgeConfigSpec.BooleanValue ALLOW_READ_MIND;

    private static final ForgeConfigSpec.ConfigValue<String> READ_MIND_ITEM;

    private static final ForgeConfigSpec.BooleanValue USE_MAX_STOCK_AS_DEFAULT;

    private static final ForgeConfigSpec.BooleanValue ALLOW_TOGGLE_RECIPE_MODE_AS_DEFAULT;

    private static final ForgeConfigSpec.BooleanValue ALLOW_CRAFT_MULTI;

    private static final ForgeConfigSpec.BooleanValue ALLOW_BURN_PAPER;

    private static final ForgeConfigSpec.BooleanValue ALLOW_STONE_CUTTING;

    static {
        BUILDER.push("通用配置");
        EASY_PICK_MATERIAL_AI = BUILDER.comment("简易化材料寻找AI：工人在架子上找原材料时，将直接在对应的小屋方块建筑进行。")
                .define("easy_pick_material_ai", true);
        NOT_COLONY_FOOD_PENALTY_MULTIPLIER = BUILDER.comment("非殖民地食物惩罚倍率：市民食用非殖民地食物带来的饱食度惩罚倍率，原版为缩减倍率至：0.25")
                .defineInRange("not_colony_food_penalty_multiplier", 0.75, 0, Integer.MAX_VALUE);
        ALLOW_HURT_ALERT = BUILDER.comment("受击提示：是否允许非警卫市民受到攻击后发出警报。")
                .define("allow_hurt_alert", true);
        ALLOW_TOGGLE_RECIPE_MODE_AS_DEFAULT = BUILDER.comment("允许默认开启配方模式：开启后将不需要在大学研究也可以直接解锁小屋配方模式的切换。")
                .define("allow_recipe_mode_as_default", true);
        USE_MAX_STOCK_AS_DEFAULT = BUILDER.comment("默认使用库存模式：开启后将默认使用库存模式作为小屋的合成模式，对于已经放置的小屋不奏效。")
                .define("use_max_stock_as_default", true);
        BUILDER.pop();

        BUILDER.push("建筑工人配置");
        EASY_BUILDER_AI = BUILDER.comment("简易化建筑工人AI：建筑工人将固定在施工现场的工作方块附近建筑。")
                .define("easy_builder_ai", true);
        BUILDER_DELAY_MODE = BUILDER.comment("建筑工人间隔模式：可选择 fixed 或 magnification，对应下列的建筑工间隔修正模式，选择其他的则视为无效。")
                .define("builder_delay_mode", "off");
        BUILDER_DELAY_MAGNIFICATION = BUILDER.comment("建筑工人间隔倍率：扩大或缩小建筑工人建筑、破坏的间隔倍率，只有建筑工人固定间隔设置为 magnification 时，该选项生效。")
                .defineInRange("builder_delay_magnification", 1.0, 0, Integer.MAX_VALUE);
        BUILDER_FIXED_DELAY = BUILDER.comment("建筑工人间隔：建筑工人间隔的固定值，只有建筑工人固定间隔设置为 fixed 时，该选项生效。")
                .defineInRange("builder_fixed_delay", 15, 0, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.push("合成工人配置");
        ALLOW_STONE_CUTTING = BUILDER.comment("允许石匠使用切石机配方：决定是否允许石匠使用切石机配方，当设置为 false 后不会影响已经教授的配方，但会导致所有配方被禁用。")
                .define("allow_stone_cutting", true);
        BUILDER.pop();

        BUILDER.push("复活配置");
        ALLOW_RESURRECT = BUILDER.comment("是否允许玩家手动复活市民：如果允许，玩家将可以使用指定物品复活市民，成功率取决于总幸福度和墓地等级。")
                .define("allow_resurrect", true);
        RESURRECT_ITEM = BUILDER.comment("复活市民所需物品：指定玩家复活市民所需的物品。")
                .define("resurrect_item", "minecraft:totem_of_undying");
        RESURRECT_CHANCE_MULTIPLIER = BUILDER.comment("复活市民几率乘数：会在原有的复活概率基础上额外乘以一个系数。")
                .defineInRange("resurrect_chance_multiplier", 1.0, 0, Integer.MAX_VALUE);
        BUILDER.pop();

        BUILDER.push("市民思维读取器设置");
        ALLOW_READ_MIND = BUILDER.comment("是否允许玩家读取市民行动：如果允许，玩家将可以手持特定物品右键市民读取市民当前行动。")
                .define("allow_read_mind", true);
        READ_MIND_ITEM = BUILDER.comment("读取行动所需物品：指定玩家读取市民行动所需的物品，不会消耗。")
                .define("read_mind_item", "minecraft:compass");
        BUILDER.pop();


        BUILDER.push("彩蛋设置");
        ALLOW_BURN_PAPER = BUILDER.comment("是否允许清明节彩蛋：如果允许，在坟墓前烧纸可以让死亡的市民安息，市民们将不会再第二天为其哀悼，但是同样该市民将无法复活。")
                .define("allow_burn_paper", true);
        BUILDER.pop();

        BUILDER.push("测试版功能设置");
        ALLOW_CRAFT_MULTI = BUILDER.comment("是否允许合成类工人一次性合成多个物品，允许则会像玩家一样一次性填满一组合成。")
                .define("allow_craft_multi", false);

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

    public static boolean allowHurtAlert;

    public static boolean allowReadMind;

    public static Item readMindItem;

    public static boolean useMaxStockAsDefault;

    public static boolean allowToggleRecipeModeAsDefault;

    public static boolean allowCraftMulti;

    public static boolean allowBurnPaper;

    public static boolean allowStoneCutting;

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
        allowHurtAlert = ALLOW_HURT_ALERT.get();
        allowReadMind = ALLOW_READ_MIND.get();
        readMindItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(READ_MIND_ITEM.get()));
        useMaxStockAsDefault = USE_MAX_STOCK_AS_DEFAULT.get();
        allowToggleRecipeModeAsDefault = ALLOW_TOGGLE_RECIPE_MODE_AS_DEFAULT.get();
        allowCraftMulti = ALLOW_CRAFT_MULTI.get();
        allowBurnPaper = ALLOW_BURN_PAPER.get();
        allowStoneCutting = ALLOW_STONE_CUTTING.get();
    }
}
