package com.sxtkl.easycolony.core.event;

import com.minecolonies.api.colony.IColony;
import com.minecolonies.api.util.MessageUtils;
import com.minecolonies.core.entity.citizen.EntityCitizen;
import com.sxtkl.easycolony.Config;
import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.core.manager.LinkageManager;
import me.towdium.jecharacters.JustEnoughCharacters;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Easycolony.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HurtAlertEvent {

    @SubscribeEvent
    public static void LivingHurt(final LivingHurtEvent event) {
        if (event.isCanceled()) return;
        if (!Config.allowHurtAlert) return;
        if (event.getSource().getEntity() instanceof EntityCitizen) return;
        if (!(event.getEntity() instanceof EntityCitizen citizen)) return;

        Entity src = event.getSource().getEntity();
        // 判断是否受到的伤害来源为实体
        if (src == null) return;
        // 判断是否为卫兵
        if (citizen.getCitizenJobHandler().getColonyJob() != null && citizen.getCitizenJobHandler().getColonyJob().isGuard()) return;

        // 发出通告，说自己被锤了
        MutableComponent message = Component.translatable(
                "com.sxtkl.easycolony.colony.hurt.message",
                src.getDisplayName(),
                (int) citizen.getX(),
                (int) citizen.getY(),
                (int) citizen.getZ()
        );
        // 为受到攻击的市民加入荧光效果，高亮显示其位置
        citizen.addEffect(new MobEffectInstance(MobEffects.GLOWING, 20 * 3));
        IColony colony = citizen.getCitizenColonyHandler().getColonyOrRegister();
        if (colony == null) return;
        MessageUtils.forCitizen(citizen, message).withPriority(MessageUtils.MessagePriority.IMPORTANT).sendTo(colony.getImportantMessageEntityPlayers());
        if (LinkageManager.useJEAMatch) {
            JustEnoughCharacters.logger.info("This is test message.");
        }
    }

}
