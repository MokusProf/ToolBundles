package net.mokus.tool_bundle.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.mokus.tool_bundle.util.ModItems;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.COPPER_TOOL_BUNDLE, Models.GENERATED);
        itemModelGenerator.register(ModItems.GOLD_TOOL_BUNDLE,Models.GENERATED);
        itemModelGenerator.register(ModItems.IRON_TOOL_BUNDLE,Models.GENERATED);
        itemModelGenerator.register(ModItems.DIAMOND_TOOL_BUNDLE,Models.GENERATED);
        itemModelGenerator.register(ModItems.NETHERITE_TOOL_BUNDLE,Models.GENERATED);
    }
}
