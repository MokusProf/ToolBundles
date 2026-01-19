package net.mokus.tool_bundle;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.mokus.tool_bundle.datagen.ModLanguageProvider;
import net.mokus.tool_bundle.datagen.ModModelProvider;
import net.mokus.tool_bundle.datagen.ModRecipeProvider;
import net.mokus.tool_bundle.datagen.ModTagProvider;

public class Tool_bundleDataGenerator implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModTagProvider::new);
        pack.addProvider(ModLanguageProvider::new);
        pack.addProvider(ModRecipeProvider::new);
    }
}
