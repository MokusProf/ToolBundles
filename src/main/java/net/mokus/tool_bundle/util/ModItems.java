package net.mokus.tool_bundle.util;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.mokus.tool_bundle.Tool_bundle;
import net.mokus.tool_bundle.components.ToolBundleContentsComponent;
import net.mokus.tool_bundle.item.ToolBundles;

public class ModItems {

    public static final Item COPPER_TOOL_BUNDLE = registerItem("copper_tool_bundle",
            new ToolBundles(new Item.Settings().maxCount(1).component(ModDataComponents.TOOL_BUNDLE_CONTENTS, ToolBundleContentsComponent.DEFAULT)
                    .component(ModDataComponents.TOOL_BUNDLE_TIERS,1)), ItemGroups.TOOLS);

    public static final Item GOLD_TOOL_BUNDLE = registerItem("gold_tool_bundle",
            new ToolBundles(new Item.Settings().maxCount(1).component(ModDataComponents.TOOL_BUNDLE_CONTENTS,ToolBundleContentsComponent.DEFAULT)
                    .component(ModDataComponents.TOOL_BUNDLE_TIERS,2)),ItemGroups.TOOLS);

    public static final Item IRON_TOOL_BUNDLE = registerItem("iron_tool_bundle",
            new ToolBundles(new Item.Settings().maxCount(1).component(ModDataComponents.TOOL_BUNDLE_CONTENTS,ToolBundleContentsComponent.DEFAULT)
                    .component(ModDataComponents.TOOL_BUNDLE_TIERS,3)),ItemGroups.TOOLS);

    public static final Item DIAMOND_TOOL_BUNDLE = registerItem("diamond_tool_bundle",
            new ToolBundles(new Item.Settings().maxCount(1).component(ModDataComponents.TOOL_BUNDLE_CONTENTS,ToolBundleContentsComponent.DEFAULT)
                    .component(ModDataComponents.TOOL_BUNDLE_TIERS,4)),ItemGroups.TOOLS);

    public static final Item NETHERITE_TOOL_BUNDLE = registerItem("netherite_tool_bundle",
            new ToolBundles(new Item.Settings().maxCount(1).fireproof().component(ModDataComponents.TOOL_BUNDLE_CONTENTS,ToolBundleContentsComponent.DEFAULT)
                    .component(ModDataComponents.TOOL_BUNDLE_TIERS,5)),ItemGroups.TOOLS);


    private static Item registerItem(String name, Item item, RegistryKey<ItemGroup> group){
        Item registeredItem = Registry.register(Registries.ITEM, Identifier.of(Tool_bundle.MOD_ID, name), item);
        ItemGroupEvents.modifyEntriesEvent(group).register(entries -> entries.add(registeredItem));
        return registeredItem;
    }

    public static void init(){

    }
}
