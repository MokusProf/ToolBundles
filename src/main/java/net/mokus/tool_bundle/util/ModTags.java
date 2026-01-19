package net.mokus.tool_bundle.util;

import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.mokus.tool_bundle.Tool_bundle;

public class ModTags {

    public static final TagKey<Item> BLOCKED_ITEM = createTag("blocked_item");

    private static TagKey<Item> createTag (String name){
        return TagKey.of(RegistryKeys.ITEM, Identifier.of(Tool_bundle.MOD_ID,name));
    }
}
