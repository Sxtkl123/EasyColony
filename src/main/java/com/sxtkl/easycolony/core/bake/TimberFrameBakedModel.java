package com.sxtkl.easycolony.core.bake;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.IDynamicBakedModel;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TimberFrameBakedModel implements IDynamicBakedModel {

    public BakedModel ori;

    public TimberFrameBakedModel(final BakedModel ori) {
        this.ori = ori;
    }

    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        return ori.getQuads(state, side, rand, data, renderType);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return ori.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return ori.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return ori.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return ori.isCustomRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return ori.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return ori.getOverrides();
    }
}
