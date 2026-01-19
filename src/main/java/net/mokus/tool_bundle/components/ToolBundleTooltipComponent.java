package net.mokus.tool_bundle.components;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.mokus.tool_bundle.Tool_bundle;
import net.mokus.tool_bundle.item.ToolBundleTooltipData;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ToolBundleTooltipComponent implements TooltipComponent {
    private static final Identifier TIER_1_BACKGROUND = Identifier.of(Tool_bundle.MOD_ID, "container/bundle/tier_1_background");
    private static final Identifier TIER_2_BACKGROUND = Identifier.of(Tool_bundle.MOD_ID, "container/bundle/tier_2_background");
    private static final Identifier TIER_3_BACKGROUND = Identifier.of(Tool_bundle.MOD_ID, "container/bundle/tier_3_background");
    private static final Identifier TIER_4_BACKGROUND = Identifier.of(Tool_bundle.MOD_ID, "container/bundle/tier_4_background");
    private static final Identifier TIER_5_BACKGROUND = Identifier.of(Tool_bundle.MOD_ID, "container/bundle/tier_5_background");

    private final List<ItemStack> stacks;
    private final int tier;

    public ToolBundleTooltipComponent(ToolBundleTooltipData data) {
        this.stacks = data.stacks();
        this.tier = data.tier();
    }

    @Override
    public int getHeight() {
        return this.getRowsHeight() + 4;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return this.getColumnsWidth();
    }

    private int getColumnsWidth() {
        return this.getColumns() * 18 + 2;
    }

    private int getRowsHeight() {
        return this.getRows() * 20 + 2;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        int columns = this.getColumns();
        int rows = this.getRows();

        Identifier background = getBackgroundForTier(this.tier);
        context.drawGuiTexture(background, x, y, this.getColumnsWidth(), this.getRowsHeight());

        int index = 0;
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < columns; col++) {
                int slotX = x + col * 18 + 1;
                int slotY = y + row * 20 + 1;
                this.drawSlot(slotX, slotY, index++, context);
            }
        }
    }

    private void drawSlot(int x, int y, int index, DrawContext context) {
        SlotSprite sprite = getSlotSpriteForTier(this.tier);

        if (index >= this.stacks.size() || this.stacks.get(index).isEmpty()) {
            this.draw(context, x, y, sprite);
        } else {
            ItemStack itemStack = this.stacks.get(index);
            this.draw(context, x, y, sprite);
            context.drawItem(itemStack, x + 1, y + 1, index);
            if (index == 0) {
                HandledScreen.drawSlotHighlight(context, x + 1, y + 1, 0);
            }
        }
    }

    private void draw(DrawContext context, int x, int y, SlotSprite sprite) {
        context.drawGuiTexture(sprite.texture, x, y, 0, sprite.width, sprite.height);
    }

    private int getColumns() {
        int total = this.stacks.size();

        if (total <= 2) return 2;
        if (total <= 4) return 2;
        if (total <= 6) return 3;
        if (total <= 8) return 4;
        if (total <= 10) return 5;
        return 4;
    }

    private int getRows() {
        int total = this.stacks.size();
        int cols = getColumns();
        return (int) Math.ceil((double) total / cols);
    }

    private Identifier getBackgroundForTier(int tier) {
        return switch (tier) {
            case 2 -> TIER_2_BACKGROUND;
            case 3 -> TIER_3_BACKGROUND;
            case 4 -> TIER_4_BACKGROUND;
            case 5 -> TIER_5_BACKGROUND;
            default -> TIER_1_BACKGROUND;
        };
    }

private SlotSprite getSlotSpriteForTier(int tier) {
    return switch (tier) {
        case 2 -> SlotSprite.TIER_2_SLOT;
        case 3 -> SlotSprite.TIER_3_SLOT;
        case 4 -> SlotSprite.TIER_4_SLOT;
        case 5 -> SlotSprite.TIER_5_SLOT;
        default -> SlotSprite.TIER_1_SLOT;
    };
}

@Environment(EnvType.CLIENT)
public enum SlotSprite {
    TIER_1_SLOT(Identifier.of(Tool_bundle.MOD_ID, "container/bundle/tier_1_slot"), 18, 20),
    TIER_2_SLOT(Identifier.of(Tool_bundle.MOD_ID, "container/bundle/tier_2_slot"), 18, 20),
    TIER_3_SLOT(Identifier.of(Tool_bundle.MOD_ID, "container/bundle/tier_3_slot"), 18, 20),
    TIER_4_SLOT(Identifier.of(Tool_bundle.MOD_ID, "container/bundle/tier_4_slot"), 18, 20),
    TIER_5_SLOT(Identifier.of(Tool_bundle.MOD_ID, "container/bundle/tier_5_slot"), 18, 20);

    public final Identifier texture;
    public final int width;
    public final int height;

        SlotSprite(Identifier texture, int width, int height) {
            this.texture = texture;
            this.width = width;
            this.height = height;
        }
    }
}
