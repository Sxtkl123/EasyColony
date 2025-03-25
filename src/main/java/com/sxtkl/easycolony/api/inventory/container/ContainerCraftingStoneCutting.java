package com.sxtkl.easycolony.api.inventory.container;

import com.minecolonies.api.util.ItemStackUtils;
import com.sxtkl.easycolony.api.inventory.ModContainers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.minecolonies.api.util.constant.InventoryConstants.*;

public class ContainerCraftingStoneCutting extends AbstractContainerMenu {

    private List<StonecutterRecipe> recipes = List.of();

    private final Container inputContainer = new SimpleContainer(1);

    private final Inventory playerInventory;

    private final Level level;

    public final BlockPos buildingPos;

    /**
     * The module id.
     */
    private int moduleId;

    public static ContainerCraftingStoneCutting fromFriendlyByteBuf(final int windowId, final Inventory inv, final FriendlyByteBuf packetBuffer) {
        final BlockPos tePos = packetBuffer.readBlockPos();
        final int moduleId = packetBuffer.readInt();
        return new ContainerCraftingStoneCutting(windowId, inv, tePos, moduleId);
    }

    public ContainerCraftingStoneCutting(final int windowId, final Inventory inv, final BlockPos pos, final int moduleId) {
        super(ModContainers.craftingStoneCutting.get(), windowId);
        this.level = inv.player.level();
        this.moduleId = moduleId;
        this.playerInventory = inv;
        this.buildingPos = pos;
        this.addSlot(new Slot(inputContainer, 0, 20, 33) {
            @Override
            public int getMaxStackSize() {
                return 1;
            }

            @NotNull
            @Override
            public ItemStack remove(final int par1) {
                return ItemStack.EMPTY;
            }

            @Override
            public boolean mayPlace(final @NotNull ItemStack par1ItemStack) {
                return true;
            }

            @Override
            public boolean mayPickup(final @NotNull Player player) {
                return false;
            }
        });
        for (int i = 0; i < INVENTORY_ROWS; i++) {
            for (int j = 0; j < INVENTORY_COLUMNS; j++) {
                addSlot(new Slot(
                        playerInventory,
                        j + i * INVENTORY_COLUMNS + INVENTORY_COLUMNS,
                        PLAYER_INVENTORY_INITIAL_X_OFFSET + j * PLAYER_INVENTORY_OFFSET_EACH,
                        PLAYER_INVENTORY_INITIAL_Y_OFFSET_CRAFTING + i * PLAYER_INVENTORY_OFFSET_EACH
                ));
            }
        }
        for (int i = 0; i < INVENTORY_COLUMNS; i++) {
            addSlot(new Slot(
                    playerInventory, i,
                    PLAYER_INVENTORY_INITIAL_X_OFFSET + i * PLAYER_INVENTORY_OFFSET_EACH,
                    PLAYER_INVENTORY_HOTBAR_OFFSET_CRAFTING
            ));
        }
    }

    @Override
    public boolean stillValid(@NotNull final Player playerIn) {
        return true;
    }

    @Override
    public void clicked(final int slotId, final int clickedButton, final @NotNull ClickType mode, final @NotNull Player playerIn) {
        if (slotId == 0) {
            if (mode == ClickType.PICKUP || mode == ClickType.PICKUP_ALL || mode == ClickType.SWAP) {
                final Slot slot = this.slots.get(slotId);
                handleSlotClick(slot, this.getCarried());
            }
        } else {
            super.clicked(slotId, clickedButton, mode, playerInventory.player);
        }
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(final Player playerIn, final int index) {
        if (index <= 1) {
            return ItemStack.EMPTY;
        }

        ItemStack itemstack = ItemStackUtils.EMPTY;
        final Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            final ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < HOTBAR_START) {
                if (!this.moveItemStackTo(itemstack1, HOTBAR_START, TOTAL_SLOTS_FURNACE, false)) {
                    return ItemStackUtils.EMPTY;
                }
            } else if ((index < TOTAL_SLOTS_FURNACE
                    && !this.moveItemStackTo(itemstack1, 1, HOTBAR_START, false))
                    || !this.moveItemStackTo(itemstack1, 1, TOTAL_SLOTS_FURNACE, false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.getCount() == 0) {
                slot.set(ItemStackUtils.EMPTY);
            } else {
                slot.setChanged();
            }
            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStackUtils.EMPTY;
            }
        }
        return itemstack;
    }

    public Player getPlayer() {
        return playerInventory.player;
    }

    /**
     * Get the position of the container.
     *
     * @return the position.
     */
    public BlockPos getPos() {
        return buildingPos;
    }

    /**
     * Get the module if of the container.
     *
     * @return the module id.
     */
    public int getModuleId() {
        return this.moduleId;
    }

    private void handleSlotClick(final Slot slot, final ItemStack stack) {
        if (stack.getCount() > 0) {
            final ItemStack copy = stack.copy();
            copy.setCount(1);
            slot.set(copy);
            this.recipes = this.level.getRecipeManager().getRecipesFor(RecipeType.STONECUTTING, inputContainer, this.level);
        } else if (slot.getItem().getCount() > 0) {
            slot.set(ItemStack.EMPTY);
            this.recipes = List.of();
        }
    }

    public List<StonecutterRecipe> getRecipes() {
        return recipes;
    }

    public Container getInputContainer() {
        return inputContainer;
    }
}
