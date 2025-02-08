package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.entity.ai.ITickingStateAI;
import com.minecolonies.api.entity.ai.combat.threat.IThreatTableEntity;
import com.minecolonies.api.entity.ai.statemachine.states.IAIState;
import com.minecolonies.api.entity.ai.statemachine.states.IState;
import com.minecolonies.api.entity.ai.statemachine.tickratestatemachine.ITickRateStateMachine;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.entity.citizen.citizenhandlers.ICitizenJobHandler;
import com.minecolonies.api.util.MessageUtils;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = EntityCitizen.class)
public abstract class EntityCitizenMixin extends AbstractEntityCitizen implements IThreatTableEntity {

    @Shadow(remap = false)
    private ICitizenJobHandler citizenJobHandler;

    public EntityCitizenMixin(EntityType<? extends PathfinderMob> type, Level world) {
        super(type, world);
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    public void hurt(DamageSource damageSource, float damage, CallbackInfoReturnable<Boolean> cir) {
        Entity src = damageSource.getEntity();
        // 判断是否受到的伤害来源为实体
        if (src == null) return;
        // 判断是否为卫兵
        if (citizenJobHandler.getColonyJob() != null && citizenJobHandler.getColonyJob().isGuard()) return;
        // 发出通告，说自己被锤了
        MutableComponent message = Component.translatable(
                "com.sxtkl.easycolony.colony.hurt.message",
                src.getDisplayName(),
                (int) this.getX(),
                (int) this.getY(),
                (int) this.getZ()
        );
        IColony colony = this.getCitizenColonyHandler().getColonyOrRegister();
        if (colony == null) return;
        MessageUtils.forCitizen(this, message).withPriority(MessageUtils.MessagePriority.IMPORTANT).sendTo(colony.getImportantMessageEntityPlayers());
    }
}
