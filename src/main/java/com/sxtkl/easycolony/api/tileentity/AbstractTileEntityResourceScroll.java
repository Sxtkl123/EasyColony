package com.sxtkl.easycolony.api.tileentity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class AbstractTileEntityResourceScroll extends BlockEntity {

    protected ItemStack resourceScroll;

    public AbstractTileEntityResourceScroll(BlockPos pPos, BlockState pBlockState) {
        super(ModTileEntities.RESOURCE_SCROLL.get(), pPos, pBlockState);
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        resourceScroll = ItemStack.of(pTag.getCompound("ResourceScroll"));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("ResourceScroll", resourceScroll.save(new CompoundTag()));
    }

    public void setResourceScroll(@Nullable ItemStack scroll) {
        this.resourceScroll = scroll;
    }

    public ItemStack getResourceScroll() {
        return this.resourceScroll;
    }
}
