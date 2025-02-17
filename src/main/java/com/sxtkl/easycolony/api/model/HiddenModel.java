package com.sxtkl.easycolony.api.model;


import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.ChunkRenderTypeSet;
import net.minecraftforge.client.extensions.IForgeBakedModel;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HiddenModel implements IDynamicBakedModel {

    private final BakedModel oriModel;

    private BakedModel nowModel;

    public static ModelProperty<Boolean> HIDDEN = new ModelProperty<>();

    public HiddenModel(BakedModel oriModel) {
        this.oriModel = oriModel;
        this.nowModel = oriModel;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return nowModel.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return nowModel.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return nowModel.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return nowModel.getParticleIcon();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return nowModel.getOverrides();
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        Boolean hidden = data.get(HIDDEN);
        if (hidden == null) {
            nowModel = oriModel;
            return nowModel.getQuads(state, side, rand, data, renderType);
        }
        if (!hidden) {
            nowModel = oriModel;
            return nowModel.getQuads(state, side, rand, data, renderType);
        }
        nowModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(Blocks.AIR.defaultBlockState());
        return nowModel.getQuads(state, side, rand, data, renderType);
    }

    @Override
    public @NotNull ModelData getModelData(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull ModelData modelData) {
        Minecraft mc = Minecraft.getInstance();
        ModelData.Builder builder = ModelData.builder();
        if (mc.player != null && mc.player.isCreative()) {
            return builder.with(HIDDEN, false).build();
        }
        return builder.with(HIDDEN, true).build();
    }
}
