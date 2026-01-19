package net.mokus.tool_bundle;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;
import net.mokus.tool_bundle.components.ToolBundleTooltipComponent;
import net.mokus.tool_bundle.item.ToolBundleTooltipData;

public class Tool_bundleClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {

        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof ToolBundleTooltipData toolData) {
                return new ToolBundleTooltipComponent(toolData);
            }
            return null;
        });
    }
}
