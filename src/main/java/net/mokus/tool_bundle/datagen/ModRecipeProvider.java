package net.mokus.tool_bundle.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryWrapper;
import net.mokus.tool_bundle.util.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.COPPER_TOOL_BUNDLE)
                .input('X', Items.COPPER_INGOT)
                .input('Y', Items.STRING)
                .pattern(" Y ")
                .pattern("X X")
                .pattern("XXX")
                .criterion("has_copper_ingot", conditionsFromItem(Items.COPPER_INGOT))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.GOLD_TOOL_BUNDLE)
                .input('X', Items.GOLD_INGOT)
                .input('Y', ModItems.COPPER_TOOL_BUNDLE)
                .pattern("XXX")
                .pattern("XYX")
                .pattern("XXX")
                .criterion("has_copper_bundle", conditionsFromItem(ModItems.COPPER_TOOL_BUNDLE))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.IRON_TOOL_BUNDLE)
                .input('X', Items.IRON_INGOT)
                .input('Y', ModItems.GOLD_TOOL_BUNDLE)
                .pattern("XXX")
                .pattern("XYX")
                .pattern("XXX")
                .criterion("has_copper_bundle", conditionsFromItem(ModItems.COPPER_TOOL_BUNDLE))
                .offerTo(exporter);
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.DIAMOND_TOOL_BUNDLE)
                .input('X', Items.DIAMOND)
                .input('Y', ModItems.IRON_TOOL_BUNDLE)
                .pattern("XXX")
                .pattern("XYX")
                .pattern("XXX")
                .criterion("has_copper_bundle", conditionsFromItem(ModItems.COPPER_TOOL_BUNDLE))
                .offerTo(exporter);
        offerNetheriteUpgradeRecipe(exporter,ModItems.DIAMOND_TOOL_BUNDLE,RecipeCategory.MISC,ModItems.NETHERITE_TOOL_BUNDLE);
    }
}
