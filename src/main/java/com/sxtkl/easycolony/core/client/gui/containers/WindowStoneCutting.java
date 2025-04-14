package com.sxtkl.easycolony.core.client.gui.containers;

import com.minecolonies.api.colony.IColonyManager;
import com.minecolonies.api.colony.requestsystem.StandardFactoryController;
import com.minecolonies.api.crafting.IRecipeStorage;
import com.minecolonies.api.crafting.ItemStorage;
import com.minecolonies.api.crafting.RecipeStorage;
import com.minecolonies.api.util.constant.TypeConstants;
import com.minecolonies.core.Network;
import com.minecolonies.core.colony.buildings.moduleviews.CraftingModuleView;
import com.minecolonies.core.colony.buildings.views.AbstractBuildingView;
import com.minecolonies.core.network.messages.server.colony.building.worker.AddRemoveRecipeMessage;
import com.sxtkl.easycolony.api.crafting.ModCraftingTypes;
import com.sxtkl.easycolony.api.inventory.container.ContainerCraftingStoneCutting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
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

    private final AbstractBuildingView building;

    private final CraftingModuleView module;

    private float scrollOffs;

    private boolean scrolling;

    private int startIndex;

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
                if (WindowStoneCutting.this.menu.getRecipes().isEmpty()) {
                    return;
                }
                WindowStoneCutting.this.addRecipe();
            }
        }
    }

    /**
     * Draws the background layer of this container (behind the items).
     */
    @Override
    protected void renderBg(@NotNull final GuiGraphics graph, final float partialTicks, final int mouseX, final int mouseY) {
        graph.blit(CRAFTING_STONE_CUTTER, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        int l = this.leftPos + RECIPES_X;
        int i1 = this.topPos + RECIPES_Y;
        int k = (int)(41.0F * this.scrollOffs);
        int j1 = this.startIndex + 12;
        graph.blit(CRAFTING_STONE_CUTTER, this.leftPos + 119, this.topPos + 15 + k, 176 + (this.isScrollBarActive() ? 0 : 12), 0, 12, 15);
        this.renderButtons(graph, mouseX, mouseY, l, i1, j1);
        this.renderRecipes(graph, l, i1, j1);
    }

    @Override
    protected void renderTooltip(@NotNull GuiGraphics graph, int pX, int pY) {
        super.renderTooltip(graph, pX, pY);
        int i = this.leftPos + RECIPES_X;
        int j = this.topPos + RECIPES_Y;
        int k = this.startIndex + 12;
        List<StonecutterRecipe> list = this.menu.getRecipes();

        for(int l = this.startIndex; l < k && l < this.menu.getRecipes().size(); ++l) {
            int i1 = l - this.startIndex;
            int j1 = i + i1 % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH;
            int k1 = j + i1 / RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_HEIGHT + 2;
            if (pX >= j1 && pX < j1 + RECIPES_IMAGE_SIZE_WIDTH && pY >= k1 && pY < k1 + RECIPES_IMAGE_SIZE_HEIGHT) {
                graph.renderTooltip(this.font, list.get(l).getResultItem(this.minecraft.level.registryAccess()), pX, pY);
            }
        }

    }

    @Override
    public void render(@NotNull final GuiGraphics stack, int x, int y, float z) {
        this.renderBackground(stack);
        super.render(stack, x, y, z);
        this.renderTooltip(stack, x, y);
    }

    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        this.scrolling = false;
        int i = this.leftPos + RECIPES_X;
        int j = this.topPos + RECIPES_Y;
        int k = this.startIndex + 12;

        for(int l = this.startIndex; l < k; ++l) {
            int i1 = l - this.startIndex;
            double d0 = pMouseX - (double)(i + i1 % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH);
            double d1 = pMouseY - (double)(j + i1 / RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_HEIGHT);
            if (d0 >= 0.0D && d1 >= 0.0D && d0 < RECIPES_IMAGE_SIZE_WIDTH && d1 < RECIPES_IMAGE_SIZE_HEIGHT && this.menu.clickMenuButton(this.minecraft.player, l)) {
                Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_STONECUTTER_SELECT_RECIPE, 1.0F));
                this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, l);
                return true;
            }
        }

        i = this.leftPos + 119;
        j = this.topPos + 9;
        if (pMouseX >= (double)i && pMouseX < (double)(i + 12) && pMouseY >= (double)j && pMouseY < (double)(j + 54)) {
            this.scrolling = true;
        }

        return super.mouseClicked(pMouseX, pMouseY, pButton);
    }

    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.scrolling && this.isScrollBarActive()) {
            int i = this.topPos + RECIPES_Y;
            int j = i + 54;
            this.scrollOffs = ((float)pMouseY - (float)i - 7.5F) / ((float)(j - i) - 15.0F);
            this.scrollOffs = Mth.clamp(this.scrollOffs, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)this.getOffscreenRows()) + 0.5D) * RECIPES_COLUMNS;
            return true;
        } else {
            return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
        }
    }

    @Override
    public boolean mouseScrolled(double pMouseX, double pMouseY, double pDelta) {
        if (this.isScrollBarActive()) {
            int i = this.getOffscreenRows();
            float f = (float)pDelta / (float)i;
            this.scrollOffs = Mth.clamp(this.scrollOffs - f, 0.0F, 1.0F);
            this.startIndex = (int)((double)(this.scrollOffs * (float)i) + 0.5D) * RECIPES_COLUMNS;
        }

        return true;
    }

    private void renderButtons(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, int pX, int pY, int pLastVisibleElementIndex) {
        for(int i = this.startIndex; i < pLastVisibleElementIndex && i < this.menu.getRecipes().size(); ++i) {
            int j = i - this.startIndex;
            int k = pX + j % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH;
            int l = j / RECIPES_COLUMNS;
            int i1 = pY + l * RECIPES_IMAGE_SIZE_HEIGHT + 2;
            int j1 = this.imageHeight;
            if (pMouseX >= k && pMouseY >= i1 && pMouseX < k + RECIPES_IMAGE_SIZE_WIDTH && pMouseY < i1 + RECIPES_IMAGE_SIZE_HEIGHT) {
                j1 += 36;
            }

            pGuiGraphics.blit(CRAFTING_STONE_CUTTER, k, i1 - 1, 0, j1, RECIPES_IMAGE_SIZE_WIDTH, RECIPES_IMAGE_SIZE_HEIGHT);
        }

    }

    private void renderRecipes(GuiGraphics pGuiGraphics, int pX, int pY, int pStartIndex) {
        List<StonecutterRecipe> list = this.menu.getRecipes();

        for(int i = this.startIndex; i < pStartIndex && i < this.menu.getRecipes().size(); ++i) {
            int j = i - this.startIndex;
            int k = pX + j % RECIPES_COLUMNS * RECIPES_IMAGE_SIZE_WIDTH;
            int l = j / RECIPES_COLUMNS;
            int i1 = pY + l * RECIPES_IMAGE_SIZE_HEIGHT + 2;
            pGuiGraphics.renderItem(list.get(i).getResultItem(this.minecraft.level.registryAccess()), k, i1);
        }
    }

    private boolean isScrollBarActive() {
        return this.menu.getRecipes().size() > 12;
    }

    private int getOffscreenRows() {
        return (this.menu.getRecipes().size() + RECIPES_COLUMNS - 1) / RECIPES_COLUMNS - RECIPES_ROWS;
    }

    private void addRecipe() {
        final List<StonecutterRecipe> list = this.container.getRecipes();

        final ItemStack inputStack = container.getInputContainer().getItem(0).copy();
        inputStack.setCount(1);
        final ItemStorage input = new ItemStorage(inputStack);

        final List<ItemStack> additionalOutput = new ArrayList<>();
        for (int i = 1; i < list.size(); i++) {
            additionalOutput.add(list.get(i).assemble(container.getInputContainer(), Minecraft.getInstance().level.registryAccess()).copy());
        }

//        final IRecipeStorage storage = StandardFactoryController.getInstance().getNewInstance(
//                TypeConstants.RECIPE,
//                StandardFactoryController.getInstance().getNewInstance(TypeConstants.ITOKEN),
//                List.of(input),
//                3,
//                list.get(0).assemble(container.getInputContainer(), Minecraft.getInstance().level.registryAccess()).copy(),
//                Blocks.AIR,
//                null,
//                com.minecolonies.api.crafting.ModRecipeTypes.MULTI_OUTPUT_ID,
//                additionalOutput,
//                new ArrayList<>()
//        );

        final IRecipeStorage storage = RecipeStorage
                .builder()
                .withInputs(List.of(input))
                .withPrimaryOutput(list.get(0).assemble(container.getInputContainer(), Minecraft.getInstance().level.registryAccess()).copy())
                .withAlternateOutputs(additionalOutput)
                .withGridSize(3)
                .withRecipeType(com.minecolonies.api.crafting.ModRecipeTypes.MULTI_OUTPUT_ID)
                .build();

        Network.getNetwork().sendToServer(new AddRemoveRecipeMessage(building, false, storage, module.getProducer().getRuntimeID()));
    }

}
