package net.mokus.tool_bundle;

import net.fabricmc.api.ModInitializer;
import net.mokus.tool_bundle.util.ModDataComponents;
import net.mokus.tool_bundle.util.ModItems;

public class Tool_bundle implements ModInitializer {

    public static final String MOD_ID = "tool_bundle";

    @Override
    public void onInitialize() {
        ModItems.init();
        ModDataComponents.init();
    }
}
