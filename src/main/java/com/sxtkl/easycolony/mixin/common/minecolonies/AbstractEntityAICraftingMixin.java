package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.api.colony.requestsystem.request.IRequest;
import com.minecolonies.api.colony.requestsystem.requestable.crafting.PublicCrafting;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.core.colony.buildings.AbstractBuilding;
import com.minecolonies.core.colony.jobs.AbstractJobCrafter;
import com.minecolonies.core.entity.ai.workers.AbstractEntityAIInteract;
import com.minecolonies.core.entity.ai.workers.crafting.AbstractEntityAICrafting;
import com.minecolonies.core.util.citizenutils.CitizenItemUtils;
import com.sxtkl.easycolony.Config;
import com.sxtkl.easycolony.core.util.EasyItemStackUtils;
import com.sxtkl.easycolony.extension.IMultiCraftExtension;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.List;

@Mixin(value = AbstractEntityAICrafting.class, remap = false)
public abstract class AbstractEntityAICraftingMixin<J extends AbstractJobCrafter<?, J>, B extends AbstractBuilding> extends AbstractEntityAIInteract<J, B> {

    /**
     * Creates the abstract part of the AI. Always use this constructor!
     *
     * @param job the job to fulfill
     */
    public AbstractEntityAICraftingMixin(@NotNull J job) {
        super(job);
    }

    @Shadow
    protected abstract int getExtendedCount(final ItemStack stack);

    @Shadow
    protected IRecipeStorage currentRecipeStorage;

    @Shadow
    public IRequest<? extends PublicCrafting> currentRequest;

    @Unique
    private int currentMaxCraftCount;


    @Redirect(method = "craft", at = @At(value = "INVOKE", target = "Lcom/minecolonies/api/colony/requestsystem/request/IRequest;addDelivery(Lnet/minecraft/world/item/ItemStack;)V"))
    public void craft$addDelivery(IRequest<? extends PublicCrafting> instance, ItemStack stack) {
        if (!Config.allowCraftMulti) {
            instance.addDelivery(stack);
            return;
        }
        List<ItemStack> outputs = EasyItemStackUtils.getItemStackByTimes(stack, this.currentMaxCraftCount);
        for (ItemStack output : outputs) {
            instance.addDelivery(output.copy());
        }
    }

    @Redirect(method = "craft", at = @At(value = "INVOKE", target = "Lcom/minecolonies/core/colony/jobs/AbstractJobCrafter;setCraftCounter(I)V"))
    public void craft$setCraftCounter(AbstractJobCrafter<?, ?> instance, int craftCounter) {
        if (!Config.allowCraftMulti) {
            instance.setCraftCounter(instance.getCraftCounter() + 1);
            return;
        }
        instance.setCraftCounter(instance.getCraftCounter() + this.currentMaxCraftCount);
    }

    @Redirect(method = "craft", at = @At(value = "INVOKE", target = "Lcom/minecolonies/api/crafting/IRecipeStorage;fullfillRecipe(Lnet/minecraft/world/level/storage/loot/LootParams;Ljava/util/List;)Z"))
    public boolean craft$fullfillRecipe(IRecipeStorage instance, LootParams context, List<IItemHandler> handlers) {
        if (!Config.allowCraftMulti) {
            return instance.fullfillRecipe(context, handlers);
        }
        if (!(instance instanceof IMultiCraftExtension multiCraft)) {
            return instance.fullfillRecipe(context, handlers);
        }
        this.currentMaxCraftCount = getMaxCraftCount();
        return multiCraft.fullfillRecipeAndCopy(context, handlers, true, this.currentMaxCraftCount) != null;
    }

    @Redirect(method = "craft", at = @At(value = "INVOKE", target = "Lcom/minecolonies/core/util/citizenutils/CitizenItemUtils;damageItemInHand(Lcom/minecolonies/api/entity/citizen/AbstractEntityCitizen;Lnet/minecraft/world/InteractionHand;I)V"))
    public void craft$damageItemInHand(AbstractEntityCitizen citizen, InteractionHand hand, int damage) {
        if (!Config.allowCraftMulti) {
            CitizenItemUtils.damageItemInHand(worker, InteractionHand.MAIN_HAND, 1);
            return;
        }
        CitizenItemUtils.damageItemInHand(worker, InteractionHand.MAIN_HAND, this.currentMaxCraftCount);
    }

    @Unique
    private int getMaxCraftCount() {
        final int currentCount = InventoryUtils.getItemCountInItemHandler(worker.getInventoryCitizen(),
                stack -> ItemStackUtils.compareItemStacksIgnoreStackSize(stack, currentRecipeStorage.getPrimaryOutput()));
        final int inProgressCount = getExtendedCount(currentRecipeStorage.getPrimaryOutput());

        final int countPerIteration = currentRecipeStorage.getPrimaryOutput().getCount();
        final int doneOpsCount = currentCount / countPerIteration;
        final int progressOpsCount = inProgressCount / countPerIteration;
        final int remainingOpsCount = currentRequest.getRequest().getCount() - doneOpsCount - progressOpsCount;
        return Math.min(currentRecipeStorage.getPrimaryOutput().getMaxStackSize(), remainingOpsCount);
    }

}
