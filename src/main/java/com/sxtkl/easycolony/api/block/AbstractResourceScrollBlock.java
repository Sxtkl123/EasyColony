package com.sxtkl.easycolony.api.block;

import com.sxtkl.easycolony.Easycolony;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.IForgeRegistry;

public abstract class AbstractResourceScrollBlock extends Block {
    private static final String NAME = "resource_scroll";

    public AbstractResourceScrollBlock() {
        super(Properties.copy(Blocks.OAK_SIGN));
    }

    public AbstractResourceScrollBlock registerBlock(final IForgeRegistry<Block> registry) {
        registry.register(new ResourceLocation(Easycolony.MODID, NAME), this);
        return this;
    }
}
