package net.mokus.tool_bundle.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;

import java.util.List;

public record ToolBundleTooltipData(List<ItemStack> stacks, int tier) implements TooltipData {
}
