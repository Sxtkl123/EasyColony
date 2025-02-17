package com.sxtkl.easycolony.api.block;

import com.sxtkl.easycolony.Easycolony;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.IForgeRegistry;

import static net.minecraft.world.level.block.Blocks.GLASS;

public class TestBlock extends HalfTransparentBlock {
    public TestBlock() {
        super(Properties.copy(GLASS)
                .isRedstoneConductor((a, b, c) -> false)
                .isViewBlocking((a, b, c) -> false)
                .isSuffocating((a, b, c) -> false)
                .isValidSpawn((a, b, c, d) -> false)
        );
    }

    public boolean propagatesSkylightDown(BlockState pState, BlockGetter pReader, BlockPos pPos) {
        return true;
    }

    public float getShadeBrightness(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        return 1.0F;
    }

    public TestBlock registerBlock(final IForgeRegistry<Block> registry) {
        registry.register(new ResourceLocation(Easycolony.MODID, "test"), this);
        return this;
    }

    public void registerBlockItem(final IForgeRegistry<Item> registry, final Item.Properties properties) {
        registry.register(new ResourceLocation(Easycolony.MODID, "test"), new BlockItem(this, properties));
    }

}
