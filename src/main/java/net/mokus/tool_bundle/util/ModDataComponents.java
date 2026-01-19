package net.mokus.tool_bundle.util;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.mokus.tool_bundle.Tool_bundle;
import net.mokus.tool_bundle.components.ToolBundleContentsComponent;

public class ModDataComponents {

    public static final ComponentType<ToolBundleContentsComponent> TOOL_BUNDLE_CONTENTS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Tool_bundle.MOD_ID,"tool_bundle_contents"),
            ComponentType.<ToolBundleContentsComponent>builder().codec(ToolBundleContentsComponent.CODEC)
                    .packetCodec(ToolBundleContentsComponent.PACKET_CODEC).cache().build()
    );

    public static final ComponentType<Integer> TOOL_BUNDLE_TIERS = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Identifier.of(Tool_bundle.MOD_ID, "tool_bundle_tiers"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static void init(){
    }
}
