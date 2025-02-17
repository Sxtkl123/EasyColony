package com.sxtkl.easycolony.api.block;

import com.minecolonies.core.entity.citizen.EntityCitizen;
import com.sxtkl.easycolony.Easycolony;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static net.minecraft.world.level.block.Blocks.OAK_PLANKS;

public abstract class AbstractCitizenPressurePlateBlock extends PressurePlateBlock {
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static String NAME = "citizen_pressure_plate";

    public AbstractCitizenPressurePlateBlock() {
        super(Sensitivity.MOBS, BlockBehaviour.Properties.of().mapColor(OAK_PLANKS.defaultMapColor()).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().strength(0.5F).ignitedByLava().pushReaction(PushReaction.DESTROY), BlockSetType.OAK);
        this.registerDefaultState(this.stateDefinition.any().setValue(POWERED, false));
    }

    @Override
    protected int getSignalStrength(@NotNull Level pLevel, @NotNull BlockPos pPos) {
        net.minecraft.world.phys.AABB move = TOUCH_AABB.move(pPos);
        List<EntityCitizen> entities = pLevel.getEntitiesOfClass(EntityCitizen.class, move);
        if(entities.isEmpty()) return 0;
        for(EntityCitizen entity : entities) {
            if(!entity.isIgnoringBlockTriggers()) {
                return 15;
            }
        }

        return 0;
    }

    public AbstractCitizenPressurePlateBlock registerBlock(final IForgeRegistry<Block> registry) {
        registry.register(new ResourceLocation(Easycolony.MODID, NAME), this);
        return this;
    }

    public void registerBlockItem(final IForgeRegistry<Item> registry, final Item.Properties properties) {
        registry.register(new ResourceLocation(Easycolony.MODID, NAME), new BlockItem(this, properties));
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
