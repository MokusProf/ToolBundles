package net.mokus.tool_bundle.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.mokus.tool_bundle.components.ToolBundleContentsComponent;
import net.mokus.tool_bundle.util.ModDataComponents;
import net.mokus.tool_bundle.util.ModTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ToolBundles extends Item {
    private static final int ITEM_BAR_COLOR = MathHelper.packRgb(0.4F, 0.4F, 1.0F);

    public ToolBundles(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canBeNested() {
        return false;
    }

    @Override
    public boolean onStackClicked(ItemStack stack, Slot slot, ClickType clickType, PlayerEntity player) {
        if (clickType != ClickType.RIGHT) {
            return false;
        } else {
            ToolBundleContentsComponent bundleContentsComponent = stack.get(ModDataComponents.TOOL_BUNDLE_CONTENTS);
            if (bundleContentsComponent == null) {
                return false;
            } else {
                ItemStack itemStack = slot.getStack();
                ToolBundleContentsComponent.Builder builder = new ToolBundleContentsComponent.Builder(bundleContentsComponent, stack);
                if (itemStack.isEmpty()) {
                    this.playRemoveOneSound(player);
                    ItemStack itemStack2 = builder.removeFirst();
                    if (itemStack2 != null) {
                        ItemStack itemStack3 = slot.insertStack(itemStack2);
                        builder.add(itemStack3);
                    }
                } else if (itemStack.getItem().canBeNested() && !itemStack.isIn(ModTags.BLOCKED_ITEM)) {
                    int i = builder.add(slot, player, stack);
                    if (i > 0) {
                        this.playInsertSound(player);
                    }
                }

                stack.set(ModDataComponents.TOOL_BUNDLE_CONTENTS, builder.build());
                return true;
            }
        }
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {
        if (clickType == ClickType.RIGHT && slot.canTakePartial(player)) {
            ToolBundleContentsComponent bundleContentsComponent = stack.get(ModDataComponents.TOOL_BUNDLE_CONTENTS);
            if (bundleContentsComponent == null) {
                return false;
            } else {
                ToolBundleContentsComponent.Builder builder = new ToolBundleContentsComponent.Builder(bundleContentsComponent, stack);
                if (otherStack.isEmpty()) {
                    ItemStack itemStack = builder.removeFirst();
                    if (itemStack != null) {
                        this.playRemoveOneSound(player);
                        cursorStackReference.set(itemStack);
                    }
                } else {
                    int i = builder.add(otherStack);
                    if (i > 0) {
                        this.playInsertSound(player);
                    }
                }

                stack.set(ModDataComponents.TOOL_BUNDLE_CONTENTS, builder.build());
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        if (dropAllBundledItems(itemStack, user)) {
            this.playDropContentsSound(user);
            user.incrementStat(Stats.USED.getOrCreateStat(this));
            return TypedActionResult.success(itemStack, world.isClient());
        } else {
            return TypedActionResult.fail(itemStack);
        }
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        ToolBundleContentsComponent contents = stack.getOrDefault(ModDataComponents.TOOL_BUNDLE_CONTENTS, ToolBundleContentsComponent.DEFAULT);
        int currentItems = (contents != null) ? contents.size() : 0;
        int maxItems = getMaxItemsForTier(stack);
        return currentItems > 0 && maxItems > 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        ToolBundleContentsComponent contents = stack.getOrDefault(ModDataComponents.TOOL_BUNDLE_CONTENTS, ToolBundleContentsComponent.DEFAULT);
        int currentItems = (contents != null) ? contents.size() : 0;
        int maxItems = getMaxItemsForTier(stack);
        float fraction = (maxItems > 0) ? ((float) currentItems / maxItems) : 0f;
        return Math.min(1 + MathHelper.floor(fraction * 12f), 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return ITEM_BAR_COLOR;
    }

    private static boolean dropAllBundledItems(ItemStack stack, PlayerEntity player) {
        ToolBundleContentsComponent bundleContentsComponent = stack.get(ModDataComponents.TOOL_BUNDLE_CONTENTS);
        if (bundleContentsComponent != null && !bundleContentsComponent.isEmpty()) {
            stack.set(ModDataComponents.TOOL_BUNDLE_CONTENTS, ToolBundleContentsComponent.DEFAULT);
            if (player instanceof ServerPlayerEntity) {
                bundleContentsComponent.iterateCopy().forEach(stackx -> player.dropItem(stackx, true));
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<TooltipData> getTooltipData(ItemStack stack) {
        if (stack.contains(DataComponentTypes.HIDE_TOOLTIP) || stack.contains(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP)) {
            return Optional.empty();
        }

        int tier = stack.getOrDefault(ModDataComponents.TOOL_BUNDLE_TIERS, 1);
        int maxItems = getMaxItemsForTier(stack);

        ToolBundleContentsComponent contents = stack.getOrDefault(ModDataComponents.TOOL_BUNDLE_CONTENTS, ToolBundleContentsComponent.DEFAULT);

        List<ItemStack> stackList = new ArrayList<>();
        for (ItemStack s : contents.stacks) {
            stackList.add(s.copy());
        }

        while (stackList.size() < maxItems) {
            stackList.add(ItemStack.EMPTY);
        }

        return Optional.of(new ToolBundleTooltipData(stackList, tier));
    }


    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        ToolBundleContentsComponent contents = stack.get(ModDataComponents.TOOL_BUNDLE_CONTENTS);
        int currentItems = (contents != null) ? contents.size() : 0;
        int maxItems = getMaxItemsForTier(stack);

        tooltip.add(Text.translatable("item.minecraft.bundle.fullness", currentItems, maxItems).formatted(Formatting.GRAY));
    }

    @Override
    public void onItemEntityDestroyed(ItemEntity entity) {
        ToolBundleContentsComponent bundleContentsComponent = entity.getStack().get(ModDataComponents.TOOL_BUNDLE_CONTENTS);
        if (bundleContentsComponent != null) {
            entity.getStack().set(ModDataComponents.TOOL_BUNDLE_CONTENTS, ToolBundleContentsComponent.DEFAULT);
            ItemUsage.spawnItemContents(entity, bundleContentsComponent.iterateCopy());
        }
    }

    private void playRemoveOneSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playInsertSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_INSERT, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
    }

    private void playDropContentsSound(Entity entity) {
        entity.playSound(SoundEvents.ITEM_BUNDLE_DROP_CONTENTS, 0.8F, 0.8F + entity.getWorld().getRandom().nextFloat() * 0.4F);
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
}
