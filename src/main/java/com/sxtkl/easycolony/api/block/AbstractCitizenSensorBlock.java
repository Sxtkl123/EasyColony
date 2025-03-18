package com.sxtkl.easycolony.api.block;

import com.ldtteam.structurize.items.ModItems;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.extension.ISilenceExtension;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@SuppressWarnings({"NullableProblems", "deprecation"})
public abstract class AbstractCitizenSensorBlock extends PressurePlateBlock implements ISilenceExtension {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    private static final String NAME = "citizen_sensor";

    public AbstractCitizenSensorBlock() {
        super(Sensitivity.MOBS,
                Properties.copy(Blocks.OAK_PLANKS).noOcclusion().noCollission()
                        .isViewBlocking((a, b, c) -> false)
                        .isSuffocating((a, b, c) -> false)
                        .isValidSpawn((a, b, c, d) -> false),
                BlockSetType.OAK);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    public AbstractCitizenSensorBlock registerBlock(final IForgeRegistry<Block> registry) {
        registry.register(new ResourceLocation(Easycolony.MODID, NAME), this);
        return this;
    }

    public void registerBlockItem(final IForgeRegistry<Item> registry, final Item.Properties properties) {
        registry.register(new ResourceLocation(Easycolony.MODID, NAME), new BlockItem(this, properties));
    }

    @Override
    protected int getSignalStrength(@NotNull Level pLevel, @NotNull BlockPos pPos) {
        net.minecraft.world.phys.AABB move = TOUCH_AABB.move(pPos);
        List<EntityCitizen> entities = pLevel.getEntitiesOfClass(EntityCitizen.class, move);
        List<Player> players = pLevel.getEntitiesOfClass(Player.class, move);
        if (entities.isEmpty() && players.isEmpty()) return 0;
        for (EntityCitizen entity : entities) {
            if (!entity.isIgnoringBlockTriggers()) {
                return 15;
            }
        }
        for (Player player : players) {
            if (!player.isIgnoringBlockTriggers()) {
                return 15;
            }
        }

        return 0;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return shouldShow() ? Shapes.block() : Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return shouldShow() ? RenderShape.MODEL : RenderShape.INVISIBLE;
    }

    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return true;
    }

    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    public boolean getSilence() {
        return true;
    }

    private static boolean shouldShow() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) {
            return true;
        }
        Item mainHandItem = mc.player.getItemInHand(InteractionHand.MAIN_HAND).getItem();
        Item offHandItem = mc.player.getItemInHand(InteractionHand.OFF_HAND).getItem();
        if (mainHandItem == ModItems.buildTool.get() || offHandItem == ModItems.buildTool.get()) return true;
        return mainHandItem == ModBlocks.citizenSensorBlock.asItem() || offHandItem == ModBlocks.citizenSensorBlock.asItem();
    }
}
