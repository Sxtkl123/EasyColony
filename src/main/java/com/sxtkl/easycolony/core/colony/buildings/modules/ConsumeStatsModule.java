package com.sxtkl.easycolony.core.colony.buildings.modules;

import com.minecolonies.api.colony.buildings.modules.AbstractBuildingModule;
import com.minecolonies.api.colony.buildings.modules.IPersistentModule;
import com.minecolonies.api.crafting.ItemStorage;
import com.sxtkl.easycolony.api.colony.buildings.modules.IConsumeStatsModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

import static com.minecolonies.api.util.constant.NbtTagConstants.TAG_QUANTITY;

public class ConsumeStatsModule extends AbstractBuildingModule implements IConsumeStatsModule, IPersistentModule {

    private static final String TAG_CONSUME_STATS = "consume_stats";

    private final Map<ItemStorage, Integer> consume = new HashMap<>();

    @Override
    public void increase(ItemStack itemStack) {
        ItemStorage key = new ItemStorage(itemStack);
        consume.put(key, consume.getOrDefault(new ItemStorage(itemStack), 0) + 1);
    }

    @Override
    public void clear() {
        consume.clear();
    }

    @Override
    public void serializeNBT(CompoundTag compound) {
        final ListTag consumeTagList = new ListTag();
        for (final Map.Entry<ItemStorage, Integer> entry : consume.entrySet()) {
            final CompoundTag compoundNBT = new CompoundTag();
            entry.getKey().getItemStack().save(compoundNBT);
            compoundNBT.putInt(TAG_QUANTITY, entry.getValue());
            consumeTagList.add(compoundNBT);
        }
        compound.put(TAG_CONSUME_STATS, consumeTagList);
    }

    @Override
    public void deserializeNBT(CompoundTag compound) {
        consume.clear();
        final ListTag minimumStockTagList = compound.getList(TAG_CONSUME_STATS, Tag.TAG_COMPOUND);
        for (int i = 0; i < minimumStockTagList.size(); i++) {
            final CompoundTag compoundNBT = minimumStockTagList.getCompound(i);
            consume.put(new ItemStorage(ItemStack.of(compoundNBT)), compoundNBT.getInt(TAG_QUANTITY));
        }
    }

    @Override
    public void serializeToView(FriendlyByteBuf buf) {
        buf.writeInt(consume.size());
        for (final Map.Entry<ItemStorage, Integer> entry : consume.entrySet()) {
            buf.writeItem(entry.getKey().getItemStack());
            buf.writeInt(entry.getValue());
        }
    }
}
