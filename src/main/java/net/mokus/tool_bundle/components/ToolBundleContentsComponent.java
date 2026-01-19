package net.mokus.tool_bundle.components;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.slot.Slot;
import net.mokus.tool_bundle.util.ModDataComponents;
import net.mokus.tool_bundle.util.ModTags;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ToolBundleContentsComponent implements TooltipData {
    public static final ToolBundleContentsComponent DEFAULT = new ToolBundleContentsComponent(List.of());
    public static final Codec<ToolBundleContentsComponent> CODEC = ItemStack.CODEC.listOf().xmap(ToolBundleContentsComponent::new, component -> component.stacks);
    public static final PacketCodec<RegistryByteBuf, ToolBundleContentsComponent> PACKET_CODEC = ItemStack.PACKET_CODEC
            .collect(PacketCodecs.toList())
            .xmap(ToolBundleContentsComponent::new, component -> component.stacks);
    private static final Fraction NESTED_BUNDLE_OCCUPANCY = Fraction.getFraction(1, 16);
    private static final int ADD_TO_NEW_SLOT = -1;
    public final List<ItemStack> stacks;
    final Fraction occupancy;

    ToolBundleContentsComponent(List<ItemStack> stacks, Fraction occupancy) {
        this.stacks = stacks;
        this.occupancy = occupancy;
    }

    public ToolBundleContentsComponent(List<ItemStack> stacks) {
        this(stacks, calculateOccupancy(stacks));
    }

    private static Fraction calculateOccupancy(List<ItemStack> stacks) {
        return Fraction.getFraction(stacks.size(), 1);
    }

    static Fraction getOccupancy(ItemStack stack) {
        return Fraction.ONE;
    }

    public Iterable<ItemStack> iterateCopy() {
        return Lists.<ItemStack, ItemStack>transform(this.stacks, ItemStack::copy);
    }

    public int size() {
        return this.stacks.size();
    }

    public Fraction getOccupancy() {
        return this.occupancy;
    }

    public boolean isEmpty() {
        return this.stacks.isEmpty();
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof ToolBundleContentsComponent bundleContentsComponent)
                    ? false
                    : this.occupancy.equals(bundleContentsComponent.occupancy) && ItemStack.stacksEqual(this.stacks, bundleContentsComponent.stacks);
        }
    }

    public int hashCode() {
        return ItemStack.listHashCode(this.stacks);
    }

    public String toString() {
        return "BundleContents" + this.stacks;
    }

    public static class Builder {
        private final List<ItemStack> stacks;
        private Fraction occupancy;
        private final ItemStack bundleStack;

        public Builder(ToolBundleContentsComponent base, ItemStack bundleStack) {
            this.stacks = new ArrayList<>(base.stacks);
            this.occupancy = base.occupancy;
            this.bundleStack = bundleStack;
        }

        public Builder clear() {
            this.stacks.clear();
            this.occupancy = Fraction.ZERO;
            return this;
        }

        private int addInternal() {
            return -1;
        }

        private int getMaxAllowed(ItemStack ignoredStack) {
            int maxItems = getMaxItemsForTier(bundleStack);
            return Math.max(maxItems - stacks.size(), 0);
        }


        private int getMaxItemsForTier(ItemStack stack) {
            int tier = stack.getOrDefault(ModDataComponents.TOOL_BUNDLE_TIERS, 1);

            return switch (tier) {
                case 2 -> 4;
                case 3 -> 6;
                case 4 -> 8;
                case 5 -> 10;
                default -> 2;
            };
        }

        public int add(ItemStack stack) {
            if (!stack.isEmpty() && stack.getItem().canBeNested() && stack.getMaxCount() == 1 && !stack.isIn(ModTags.BLOCKED_ITEM)) {
                int i = Math.min(stack.getCount(), getMaxAllowed(stack));
                if (i == 0) return 0;

                this.occupancy = this.occupancy.add(ToolBundleContentsComponent.getOccupancy(stack).multiplyBy(Fraction.getFraction(i, 1)));

                int j = this.addInternal();
                if (j != -1) {
                    ItemStack itemStack = this.stacks.remove(j);
                    ItemStack itemStack2 = itemStack.copyWithCount(itemStack.getCount() + i);
                    stack.decrement(i);
                    this.stacks.add(0, itemStack2);
                } else {
                    this.stacks.add(0, stack.split(i));
                }

                return i;
            }
            return 0;
        }


        public int add(Slot slot, PlayerEntity player, ItemStack bundleStack) {
            ItemStack stack = slot.getStack();

            if (stack.isEmpty() || stack.getMaxCount() != 1 && stack.isIn(ModTags.BLOCKED_ITEM)) return 0;

            int maxAllowed = getMaxAllowed(bundleStack);
            if (maxAllowed == 0) return 0;

            int toAdd = Math.min(stack.getCount(), maxAllowed);
            this.stacks.add(0, slot.takeStack(toAdd));
            this.occupancy = this.occupancy.add(Fraction.getFraction(toAdd, 1));

            return toAdd;
        }


        @Nullable
        public ItemStack removeFirst() {
            if (this.stacks.isEmpty()) {
                return null;
            } else {
                ItemStack itemStack = this.stacks.remove(0).copy();
                this.occupancy = this.occupancy.subtract(ToolBundleContentsComponent.getOccupancy(itemStack).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)));
                return itemStack;
            }
        }


        public ToolBundleContentsComponent build() {
            return new ToolBundleContentsComponent(List.copyOf(this.stacks), this.occupancy);
        }
    }
}

