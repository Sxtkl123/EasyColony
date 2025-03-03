package com.sxtkl.easycolony.apiimpl.initializer;

import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.block.ModBlocks;
import com.sxtkl.easycolony.api.tileentity.ModTileEntities;
import com.sxtkl.easycolony.core.tileentity.TileEntityResourceScroll;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@SuppressWarnings("DataFlowIssue")
public class ModTileEntitiesInitializer {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Easycolony.MODID);

    static {
        ModTileEntities.RESOURCE_SCROLL = BLOCK_ENTITIES.register("resource_scroll", () -> BlockEntityType.Builder.of(TileEntityResourceScroll::new, ModBlocks.resourceScrollBlock).build(null));
    }
}
