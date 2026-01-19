package net.mokus.tool_bundle.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import net.mokus.tool_bundle.util.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModLanguageProvider extends FabricLanguageProvider {
    public ModLanguageProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup wrapperLookup, TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.COPPER_TOOL_BUNDLE,"Copper Tool Bundle");
        translationBuilder.add(ModItems.GOLD_TOOL_BUNDLE,"Gold Tool Bundle");
        translationBuilder.add(ModItems.IRON_TOOL_BUNDLE,"Iron Tool Bundle");
        translationBuilder.add(ModItems.DIAMOND_TOOL_BUNDLE,"Diamond Tool Bundle");
        translationBuilder.add(ModItems.NETHERITE_TOOL_BUNDLE,"Netherite Tool Bundle");
    }
}