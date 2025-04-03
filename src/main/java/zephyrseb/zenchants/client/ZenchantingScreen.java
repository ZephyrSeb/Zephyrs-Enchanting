package zephyrseb.zenchants.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.CyclingSlotIcon;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import zephyrseb.zenchants.ZenchantingScreenHandler;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public class ZenchantingScreen extends ForgingScreen<ZenchantingScreenHandler> {
    private static final Identifier ERROR_TEXTURE = Identifier.ofVanilla("container/smithing/error");
    private static final Identifier EMPTY_SLOT_SWORD = Identifier.ofVanilla("container/slot/sword");
    private static final Identifier EMPTY_SLOT_AXE = Identifier.ofVanilla("container/slot/axe");
    private static final Identifier EMPTY_SLOT_PICKAXE = Identifier.ofVanilla("container/slot/pickaxe");
    private static final Identifier EMPTY_SLOT_SHOVEL = Identifier.ofVanilla("container/slot/shovel");
    private static final Identifier EMPTY_SLOT_HOE = Identifier.ofVanilla("container/slot/hoe");
    private static final Identifier EMPTY_SLOT_HELMET = Identifier.ofVanilla("container/slot/helmet");
    private static final Identifier EMPTY_SLOT_CHESTPLATE = Identifier.ofVanilla("container/slot/chestplate");
    private static final Identifier EMPTY_SLOT_LEGGINGS = Identifier.ofVanilla("container/slot/leggings");
    private static final Identifier EMPTY_SLOT_BOOTS = Identifier.ofVanilla("container/slot/boots");
    private static final Identifier EMPTY_SLOT_LAPIS = Identifier.ofVanilla("container/slot/lapis_lazuli");
    private static final Text ERROR_TOOLTIP = Text.translatable("container.zenchants.tooltip_error");
    private static final Text LEVEL_TOOLTIP = Text.translatable("container.zenchants.tooltip_level");
    private static final Text EXCLUSIVE_TOOLTIP = Text.translatable("container.zenchants.tooltip_exclusive");
    private static final Text XP_TOOLTIP = Text.translatable("container.zenchants.tooltip_xp");
    private static final List<Identifier> EMPTY_SLOT_TEXTURES = List.of(
            EMPTY_SLOT_SWORD, EMPTY_SLOT_AXE, EMPTY_SLOT_PICKAXE, EMPTY_SLOT_SHOVEL, EMPTY_SLOT_HOE, EMPTY_SLOT_HELMET, EMPTY_SLOT_CHESTPLATE, EMPTY_SLOT_LEGGINGS, EMPTY_SLOT_BOOTS
    );
    private static final List<Identifier> EMPTY_SLOT_LAPIS_LAZULI = List.of(
            EMPTY_SLOT_LAPIS
    );
    private final CyclingSlotIcon baseSlotIcon = new CyclingSlotIcon(0);
    private final CyclingSlotIcon additionSlotIcon = new CyclingSlotIcon(1);
    private final CyclingSlotIcon lapisSlotIcon = new CyclingSlotIcon(2);

    public ZenchantingScreen(ZenchantingScreenHandler handler, PlayerInventory playerInventory, Text ignoredTitle) {
        super(handler, playerInventory, Text.translatable("container.zenchants.enchant"), Identifier.of("zenchants", "textures/gui/zenchanting.png"));
        this.titleX = 44;
        this.titleY = 15;
    }

    @Override
    public void handledScreenTick() {
        super.handledScreenTick();
        this.baseSlotIcon.updateTexture(EMPTY_SLOT_TEXTURES);
        this.lapisSlotIcon.updateTexture(EMPTY_SLOT_LAPIS_LAZULI);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.renderSlotTooltip(context, mouseX, mouseY);
    }

    //Draws a GUI, including slot icons to show what goes into which slot
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        super.drawBackground(context, delta, mouseX, mouseY);
        this.baseSlotIcon.render(this.handler, context, delta, this.x, this.y);
        this.additionSlotIcon.render(this.handler, context, delta, this.x, this.y);
        this.lapisSlotIcon.render(this.handler, context, delta, this.x, this.y);
    }

    //Adds an arrow when an incorrect recipe is loaded into the table
    @Override
    protected void drawInvalidRecipeArrow(DrawContext context, int x, int y) {
        if (this.hasInvalidRecipe()) {
            context.drawGuiTexture(RenderLayer::getGuiTextured, ERROR_TEXTURE, x + 65 + 24, y + 46, 28, 21);
        }
    }

    //Tooltip that appears when you hover over the invalid recipe arrow
    private void renderSlotTooltip(DrawContext context, int mouseX, int mouseY) {
        Optional<Text> optional = Optional.empty();
        if (this.hasInvalidRecipe() && this.isPointWithinBounds(65 + 24, 46, 28, 21, mouseX, mouseY)) {
            optional = switch (this.handler.getErrorCode()) {
                case 2 -> Optional.of(LEVEL_TOOLTIP);
                case 3 -> Optional.of(XP_TOOLTIP);
                case 4 -> Optional.of(EXCLUSIVE_TOOLTIP);
                default -> Optional.of(ERROR_TOOLTIP);
            };

        }

        optional.ifPresent(text -> context.drawOrderedTooltip(this.textRenderer, this.textRenderer.wrapLines(text, 115), mouseX, mouseY));
    }

    //Draws the text displaying how many levels an operation costs
    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        super.drawForeground(context, mouseX, mouseY);
        int levelCost = ((ZenchantingScreenHandler) this.handler).getLevelCost();
        if (levelCost >= 0) {
            int j = 8453920;
            Text text;
            if (!this.handler.getSlot(3).hasStack()) {
                text = null;
            } else {
                text = Text.translatable("container.repair.cost", levelCost);
                if (!this.handler.getSlot(2).canTakeItems(this.handler.getPlayer())) {
                    j = 16736352;
                }
            }

            if (text != null) {
                int k = this.backgroundWidth - 8 - this.textRenderer.getWidth(text) - 2;
                context.fill(k - 2, 67, this.backgroundWidth - 8, 79, 1325400064);
                context.drawTextWithShadow(this.textRenderer, text, k, 69, j);
            }
        }
    }

    private boolean hasInvalidRecipe() {
        return this.handler.getSlot(0).hasStack()
                && this.handler.getSlot(1).hasStack()
                && this.handler.getSlot(2).hasStack()
                && !this.handler.getSlot(this.handler.getResultSlotIndex()).hasStack();
    }
}
