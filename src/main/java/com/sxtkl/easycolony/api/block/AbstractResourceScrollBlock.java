package com.sxtkl.easycolony.api.block;

import com.minecolonies.core.items.ItemResourceScroll;
import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.core.tileentity.TileEntityResourceScroll;
import com.sxtkl.easycolony.mixin.accessor.minecolonies.ItemResourceScrollAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SuppressWarnings({"deprecation", "NullableProblems"})
public abstract class AbstractResourceScrollBlock extends Block implements EntityBlock {
    private static final String NAME = "resource_scroll";

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape EAST_AABB = Block.box(0.0D, 0.0D, 0.0D, 3.0D, 16.0D, 16.0D);
    protected static final VoxelShape WEST_AABB = Block.box(13.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    protected static final VoxelShape SOUTH_AABB = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 3.0D);
    protected static final VoxelShape NORTH_AABB = Block.box(0.0D, 0.0D, 13.0D, 16.0D, 16.0D, 16.0D);

    public AbstractResourceScrollBlock() {
        super(Properties.copy(Blocks.LADDER).noCollission());
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)) {
            case NORTH -> NORTH_AABB;
            case SOUTH -> SOUTH_AABB;
            case WEST -> WEST_AABB;
            default -> EAST_AABB;
        };
    }

    private boolean canAttachTo(BlockGetter pBlockReader, BlockPos pPos, Direction pDirection) {
        BlockState blockstate = pBlockReader.getBlockState(pPos);
        return blockstate.isFaceSturdy(pBlockReader, pPos, pDirection);
    }

    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        Direction direction = pState.getValue(FACING);
        return this.canAttachTo(pLevel, pPos.relative(direction.getOpposite()), direction);
    }

    public AbstractResourceScrollBlock registerBlock(final IForgeRegistry<Block> registry) {
        registry.register(new ResourceLocation(Easycolony.MODID, NAME), this);
        return this;
    }

    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if (pFacing.getOpposite() == pState.getValue(FACING) && !pState.canSurvive(pLevel, pCurrentPos)) {
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext pContext) {
        if (!pContext.replacingClickedOnBlock()) {
            BlockState blockstate = pContext.getLevel().getBlockState(pContext.getClickedPos().relative(pContext.getClickedFace().getOpposite()));
            if (blockstate.is(this) && blockstate.getValue(FACING) == pContext.getClickedFace()) {
                return null;
            }
        }

        BlockState defaultState = this.defaultBlockState();
        LevelReader levelreader = pContext.getLevel();
        BlockPos blockpos = pContext.getClickedPos();

        for(Direction direction : pContext.getNearestLookingDirections()) {
            if (direction.getAxis().isHorizontal()) {
                defaultState = defaultState.setValue(FACING, direction.getOpposite());
                if (defaultState.canSurvive(levelreader, blockpos)) {
                    return defaultState;
                }
            }
        }

        return null;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TileEntityResourceScroll(pPos, pState);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        BlockEntity tile = pLevel.getBlockEntity(pPos);
        if (!(tile instanceof TileEntityResourceScroll scroll)) return InteractionResult.PASS;
        if (scroll.getResourceScroll() == null) return InteractionResult.PASS;
        if (!(scroll.getResourceScroll().getItem() instanceof ItemResourceScroll item)) return InteractionResult.PASS;
        if (!pLevel.isClientSide()) return InteractionResult.SUCCESS;
        ((ItemResourceScrollAccessor) item).invokeOpenWindow(scroll.getResourceScroll().getOrCreateTag(), pPlayer);
        return InteractionResult.SUCCESS;
    }

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        BlockEntity entity = pParams.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (!(entity instanceof TileEntityResourceScroll scroll)) {
            return super.getDrops(pState, pParams);
        }
        return List.of(scroll.getResourceScroll());
    }
}
