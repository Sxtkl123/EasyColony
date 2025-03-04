package com.sxtkl.easycolony.api.creativetab;

import com.sxtkl.easycolony.Easycolony;
import com.sxtkl.easycolony.api.block.ModBlocks;
import com.sxtkl.easycolony.api.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public final class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> TAB_REG = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Easycolony.MODID);

    public static final RegistryObject<CreativeModeTab> COMMON = TAB_REG.register("easycolony", () -> new CreativeModeTab.Builder(CreativeModeTab.Row.TOP, 1)
            .icon(() -> new ItemStack(ModBlocks.citizenSensorBlock))
            .title(Component.translatable("com.sxtkl.easycolony.creativetab.common")).displayItems((config, output) -> {
                output.accept(ModBlocks.citizenSensorBlock);
                output.accept(ModItems.recipesClipboard);
                output.accept(ModItems.storageClipboard);
            }).build());

}
