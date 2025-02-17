package com.sxtkl.easycolony.api.block;

import com.sxtkl.easycolony.Easycolony;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public abstract class AbstractCitizenSensorBlock extends Block {

    private static final String NAME = "citizen_sensor";

    private boolean powered;

    public AbstractCitizenSensorBlock() {
        super(Properties.copy(Blocks.OAK_PLANKS).noOcclusion().noCollission());
        this.powered = false;
    }

    public AbstractCitizenSensorBlock registerBlock(final IForgeRegistry<Block> registry) {
        registry.register(new ResourceLocation(Easycolony.MODID, NAME), this);
        return this;
    }

    public void registerBlockItem(final IForgeRegistry<Item> registry, final Item.Properties properties) {
        registry.register(new ResourceLocation(Easycolony.MODID, NAME), new BlockItem(this, properties));
    }

    @Override
    public void entityInside(@NotNull BlockState pState, @NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull Entity pEntity) {
        if (pLevel.isClientSide) {
            return;
        }
        this.powered = true;
        pLevel.setBlock(pPos, pState, 2);
        pLevel.scheduleTick(new BlockPos(pPos), this, 100);
    }

    @Override
    public void tick(@NotNull BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        Easycolony.LOGGER.info("tick");
        this.powered = false;
    }

    private void checkPressed(Entity pEntity, Level pLevel, BlockPos pPos, BlockState pState, boolean last) {
        boolean tempPowered = powered;
        if (powered != last) {
            powered = last;
        }

        if (!tempPowered && last) {
            Easycolony.LOGGER.info("up");
        } else if (tempPowered && !last) {
            Easycolony.LOGGER.info("down");
        }

        if (tempPowered) {
            pLevel.scheduleTick(new BlockPos(pPos), this, 20);
        }
    }

    @Override
    public int getSignal(BlockState pState, BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        return this.powered ? 15 : 0;
    }

    @Override
    public boolean isSignalSource(BlockState pState) {
        return true;
    }
}
