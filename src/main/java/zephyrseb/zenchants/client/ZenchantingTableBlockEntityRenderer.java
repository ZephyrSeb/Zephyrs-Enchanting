package zephyrseb.zenchants.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.state.EnchantingTableBlockEntityRenderState;
import net.minecraft.client.render.command.ModelCommandRenderer;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.state.CameraRenderState;
import net.minecraft.client.texture.SpriteHolder;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import zephyrseb.zenchants.ZenchantingTableBlockEntity;

@Environment(EnvType.CLIENT)
public class ZenchantingTableBlockEntityRenderer implements BlockEntityRenderer<ZenchantingTableBlockEntity, EnchantingTableBlockEntityRenderState> {
    public static final SpriteIdentifier BOOK_TEXTURE;
    private final SpriteHolder spriteHolder;
    private final BookModel book;

    public ZenchantingTableBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.spriteHolder = ctx.spriteHolder();
        this.book = new BookModel(ctx.getLayerModelPart(EntityModelLayers.BOOK));
    }

    @Override
    public EnchantingTableBlockEntityRenderState createRenderState() {
        return new EnchantingTableBlockEntityRenderState();
    }

    @Override
    public void render(EnchantingTableBlockEntityRenderState state, MatrixStack matrixStack, OrderedRenderCommandQueue queue, CameraRenderState cameraRenderState) {
        matrixStack.push();
        matrixStack.translate(0.5F, 0.75F, 0.5F);
        matrixStack.translate(0.0F, 0.1F + MathHelper.sin(state.ticks * 0.1F) * 0.01F, 0.0F);
        float f = state.bookRotationDegrees;
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation(-f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(80.0F));
        float g = MathHelper.fractionalPart(state.pageAngle + 0.25F) * 1.6F - 0.3F;
        float h = MathHelper.fractionalPart(state.pageAngle + 0.75F) * 1.6F - 0.3F;
        BookModel.BookModelState bookModelState = new BookModel.BookModelState(state.ticks, MathHelper.clamp(g, 0.0F, 1.0F), MathHelper.clamp(h, 0.0F, 1.0F), state.pageTurningSpeed);
        queue.submitModel(this.book, bookModelState, matrixStack, BOOK_TEXTURE.getRenderLayer(RenderLayer::getEntitySolid), state.lightmapCoordinates, OverlayTexture.DEFAULT_UV, -1, this.spriteHolder.getSprite(BOOK_TEXTURE), 0, state.crumblingOverlay);
        matrixStack.pop();
    }

    public void updateRenderState(ZenchantingTableBlockEntity enchantingTableBlockEntity, EnchantingTableBlockEntityRenderState enchantingTableBlockEntityRenderState, float f, Vec3d vec3d, @Nullable ModelCommandRenderer.CrumblingOverlayCommand crumblingOverlayCommand) {
        BlockEntityRenderer.super.updateRenderState(enchantingTableBlockEntity, enchantingTableBlockEntityRenderState, f, vec3d, crumblingOverlayCommand);
        enchantingTableBlockEntityRenderState.pageAngle = MathHelper.lerp(f, enchantingTableBlockEntity.pageAngle, enchantingTableBlockEntity.nextPageAngle);
        enchantingTableBlockEntityRenderState.pageTurningSpeed = MathHelper.lerp(f, enchantingTableBlockEntity.pageTurningSpeed, enchantingTableBlockEntity.nextPageTurningSpeed);
        enchantingTableBlockEntityRenderState.ticks = (float)enchantingTableBlockEntity.ticks + f;

        float g = enchantingTableBlockEntity.bookRotation - enchantingTableBlockEntity.lastBookRotation;
        while (g >= (float)Math.PI) {
            g -= ((float) Math.PI * 2F);
        }

        while(g < -(float)Math.PI) {
            g += ((float)Math.PI * 2F);
        }

        enchantingTableBlockEntityRenderState.bookRotationDegrees = enchantingTableBlockEntity.lastBookRotation + g * f;
    }

    static {
        BOOK_TEXTURE = TexturedRenderLayers.ENTITY_SPRITE_MAPPER.mapVanilla("enchanting_table_book");
    }
}
