package com.sxtkl.easycolony.mixin.common.minecolonies;

import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.crafting.RecipeStorage;
import com.minecolonies.api.entity.citizen.AbstractEntityCitizen;
import com.minecolonies.api.util.InventoryUtils;
import com.minecolonies.api.util.ItemStackUtils;
import com.sxtkl.easycolony.core.util.EasyItemStackUtils;
import com.sxtkl.easycolony.extension.IMultiCraftExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(value = RecipeStorage.class, remap = false)
public abstract class RecipeStorageMixin implements IRecipeStorage, IMultiCraftExtension {

    @Final
    @NotNull
    @Shadow
    private List<ItemStack> tools;

    @Final
    @NotNull
    @Shadow
    private List<ItemStack> secondaryOutputs;

    @Final
    @Shadow
    private ResourceLocation lootTable;

    @Shadow
    private LootTable loot;

    @Shadow
    protected abstract boolean checkForFreeSpace(final List<IItemHandler> handlers);

    @Override
    public List<ItemStack> fullfillRecipeAndCopy(final LootParams context, final List<IItemHandler> handlers, boolean doInsert, int times) {
        if (!checkForFreeSpace(handlers) || !canFullFillRecipe(1, Collections.emptyMap(), handlers.toArray(new IItemHandler[0]))) {
            return null;
        }

        final AbstractEntityCitizen citizen = (AbstractEntityCitizen) context.getParamOrNull(LootContextParams.THIS_ENTITY);

        for (final ItemStorage storage : getCleanedInput()) {
            final ItemStack stack = storage.getItemStack();
            int amountNeeded = storage.getAmount() * times;

            if (amountNeeded == 0) {
                break;
            }

            for (final IItemHandler handler : handlers) {
                boolean isTool = ItemStackUtils.compareItemStackListIgnoreStackSize(tools, stack, false, !storage.ignoreNBT());
                int slotOfStack =
                        InventoryUtils.findFirstSlotInItemHandlerNotEmptyWith(handler, itemStack ->
                                ItemStackUtils.compareItemStacksIgnoreStackSize(itemStack, stack, false, !storage.ignoreNBT()) &&
                                        (!isTool || !stack.isDamageableItem() || ItemStackUtils.getDurability(itemStack) > 0));

                while (slotOfStack != -1 && amountNeeded > 0) {
                    if (citizen != null && isTool) {
                        if (stack.isDamageableItem()) {
                            ItemStack toDamage = handler.extractItem(slotOfStack, times, false);
                            if (!ItemStackUtils.isEmpty(toDamage)) {
                                // The 4 parameter inner call from forge is for adding a callback to alter the damage caused,
                                // but unlike its description does not actually damage the item(despite the same function name). So used to just calculate the damage.
                                toDamage.hurtAndBreak(toDamage.getItem().damageItem(stack, times, citizen, item -> item.broadcastBreakEvent(InteractionHand.MAIN_HAND)), citizen, item -> item.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                            }
                            if (!ItemStackUtils.isEmpty(toDamage)) {
                                handler.insertItem(slotOfStack, toDamage, false);
                            }
                        }
                        amountNeeded -= times;
                    } else {
                        final int count = ItemStackUtils.getSize(handler.getStackInSlot(slotOfStack));
                        final ItemStack extractedStack = handler.extractItem(slotOfStack, amountNeeded, false).copy();

                        //This prevents the AI and for that matter the server from getting stuck in case of an emergency.
                        //Deletes some items, but hey.
                        if (ItemStackUtils.isEmpty(extractedStack)) {
                            handler.insertItem(slotOfStack, extractedStack, false);
                            return null;
                        }

                        amountNeeded -= count;
                        if (amountNeeded > 0) {
                            slotOfStack = InventoryUtils.findFirstSlotInItemHandlerNotEmptyWith(handler,
                                    itemStack -> !ItemStackUtils.isEmpty(itemStack) && ItemStackUtils.compareItemStacksIgnoreStackSize(itemStack, stack, false, !storage.ignoreNBT()));
                        }
                    }
                }

                // stop looping handlers if we have what we need
                if (amountNeeded <= 0) {
                    break;
                }
            }

            if (amountNeeded > 0) {
                return null;
            }
        }

        return insertCraftedItems(handlers, getPrimaryOutput(), context, doInsert, times);
    }

    @Unique
    private List<ItemStack> insertCraftedItems(final List<IItemHandler> handlers, ItemStack outputStack, LootParams context, boolean doInsert, int times) {
        final List<ItemStack> resultStacks = new ArrayList<>();
        final List<ItemStack> randomStacks = new ArrayList<>();
        final List<ItemStack> secondaryStacks = new ArrayList<>();

        if (!ItemStackUtils.isEmpty(outputStack)) {
            List<ItemStack> outputStacks = EasyItemStackUtils.getItemStackByTimes(outputStack, times);
            resultStacks.addAll(outputStacks.stream().map(ItemStack::copy).toList());
            if (doInsert) {
                for (final ItemStack stack : outputStacks) {
                    for (final IItemHandler handler : handlers) {
                        if (InventoryUtils.addItemStackToItemHandler(handler, stack.copy())) {
                            break;
                        }
                    }
                }
            }
            secondaryStacks.addAll(secondaryOutputs);
        }

        if (loot == null && lootTable != null) {
            loot = context.getLevel().getServer().getLootData().getLootTable(lootTable);
        }

        if (loot != null && context != null) {
            for (int i = 0; i < times; i++) {
                List<ItemStack> randomItems = loot.getRandomItems(context);
                randomStacks.addAll(randomItems);
            }
        }

        List<ItemStack> totalSecStacks = new ArrayList<>();
        for (final ItemStack stack : secondaryStacks) {
            totalSecStacks.addAll(EasyItemStackUtils.getItemStackByTimes(stack, times).stream().map(ItemStack::copy).toList());
        }
        resultStacks.addAll(totalSecStacks.stream().map(ItemStack::copy).toList());
        resultStacks.addAll(randomStacks.stream().map(ItemStack::copy).toList());
        if (doInsert) {
            for (final ItemStack stack : totalSecStacks) {
                for (final IItemHandler handler : handlers) {
                    if (InventoryUtils.addItemStackToItemHandler(handler, stack.copy())) {
                        break;
                    }
                }
            }
            for (final ItemStack stack : randomStacks) {
                for (final IItemHandler handler : handlers) {
                    if (InventoryUtils.addItemStackToItemHandler(handler, stack.copy())) {
                        break;
                    }
                }
            }
        }

        return Collections.unmodifiableList(resultStacks);
    }
}
