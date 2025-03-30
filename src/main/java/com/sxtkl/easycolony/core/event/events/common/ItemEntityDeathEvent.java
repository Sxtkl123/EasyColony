package com.sxtkl.easycolony.core.event.events.common;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraftforge.event.entity.item.ItemEvent;

public class ItemEntityDeathEvent extends ItemEvent {
    private final DamageSource source;

    public ItemEntityDeathEvent(ItemEntity itemEntity, DamageSource source) {
        super(itemEntity);
        this.source = source;
    }

    public DamageSource getSource() {
        return source;
    }
}
