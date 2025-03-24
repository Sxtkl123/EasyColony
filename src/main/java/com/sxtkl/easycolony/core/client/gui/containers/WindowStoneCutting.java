package com.sxtkl.easycolony.core.client.gui.containers;

import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.util.ItemStackUtils;
import com.minecolonies.core.Network;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.minecolonies.core.colony.buildings.views.AbstractBuildingView;
import com.minecolonies.core.network.messages.server.colony.building.worker.AddRemoveRecipeMessage;
import com.sxtkl.easycolony.api.crafting.ModCraftingTypes;
import com.sxtkl.easycolony.api.inventory.container.ContainerCraftingStoneCutting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.minecolonies.api.util.constant.TranslationConstants.WARNING_MAXIMUM_NUMBER_RECIPES;
import static com.minecolonies.api.util.constant.translation.BaseGameTranslationConstants.BASE_GUI_DONE;

@SuppressWarnings("DataFlowIssue")
@OnlyIn(Dist.CLIENT)
public class WindowStoneCutting extends AbstractContainerScreen<ContainerCraftingStoneCutting> {

    private static final ResourceLocation CRAFTING_STONE_CUTTER = new ResourceLocation("minecraft", "textures/gui/container/stonecutter.png");

    private static final int BUTTON_X_OFFSET = 1;
    private static final int BUTTON_Y_POS = 170;
    private static final int BUTTON_WIDTH = 150;
    private static final int BUTTON_HEIGHT = 20;
    private static final int RECIPES_X = 52;
    private static final int RECIPES_Y = 14;
    private static final int RECIPES_IMAGE_SIZE_WIDTH = 16;
    private static final int RECIPES_IMAGE_SIZE_HEIGHT = 18;
    private static final int RECIPES_COLUMNS = 4;
    private static final int RECIPES_ROWS = 3;

    private final ContainerCraftingStoneCutting container;

    /**
     * The building assigned to this.
     */
    private final AbstractBuildingView building;

    /**
     * The module this crafting window is for.
     */
    private final CraftingModuleView module;

    /**
     * Create a crafting gui window.
     *
     * @param container       the container.
     * @param playerInventory the player inv.
     * @param iTextComponent  the display text component.
     */
    public WindowStoneCutting(final ContainerCraftingStoneCutting container, final Inventory playerInventory, final Component iTextComponent) {
        super(container, playerInventory, iTextComponent);
        this.container = container;
        this.building = (AbstractBuildingView) IColonyManager.getInstance().getBuildingView(playerInventory.player.level().dimension(), container.getPos());
        this.module = (CraftingModuleView) building.getModuleView(container.getModuleId());
    }

    @NotNull
    public AbstractBuildingView getBuildingView() {
        return building;
    }

    @Override
    protected void init() {
        super.init();
        final Component buttonDisplay = Component.translatable(module.canLearn(ModCraftingTypes.STONECUTTING_CRAFTING.get()) ? BASE_GUI_DONE : WARNING_MAXIMUM_NUMBER_RECIPES);
        /*
         * The button to click done after finishing the recipe.
         */
        final Button doneButton = new Button.Builder(buttonDisplay, new WindowStoneCutting.OnButtonPress()).pos(leftPos + BUTTON_X_OFFSET, topPos + BUTTON_Y_POS).size(BUTTON_WIDTH, BUTTON_HEIGHT).build();
        this.addRenderableWidget(doneButton);
        if (!module.canLearn(ModCraftingTypes.STONECUTTING_CRAFTING.get())) {
            doneButton.active = false;
        }
    }

    public class OnButtonPress implements Button.OnPress {
        @Override
        public void onPress(@NotNull final Button button) {
            if (module.canLearn(ModCraftingTypes.STONECUTTING_CRAFTING.get())) {
                final List<ItemStorage> input = new ArrayList<>();
                input.add(new ItemStorage(container.slots.get(0).getItem()));
                final ItemStack primaryOutput = container.slots.get(1).getItem().copy();

                if (!ItemStackUtils.isEmpty(primaryOutput)) {
                    Network.getNetwork().sendToServer(new AddRemoveRecipeMessage(building, input, 1, primaryOutput, false, Blocks.FURNACE, module.getProducer().getRuntimeID()));
                }
            }
        }
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    @Override
    protected void renderBg(@NotNull final GuiGraphics stack, final float partialTicks, final int mouseX, final int mouseY) {
        stack.blit(CRAFTING_STONE_CUTTER, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(@NotNull final GuiGraphics stack, int x, int y, float z) {
        this.renderBackground(stack);
        super.render(stack, x, y, z);
        this.renderTooltip(stack, x, y);
    }

    private void renderRecipes(GuiGraphics pGuiGraphics, int pX, int pY, int pStartIndex) {

    }

}
