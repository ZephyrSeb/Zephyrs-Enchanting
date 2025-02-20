package zephyrseb.zenchants.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import zephyrseb.zenchants.registry;


@Environment(EnvType.CLIENT)
public class ZenchantsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		HandledScreens.register(registry.ENCHANTING_SCREEN_HANDLER, ZenchantingScreen::new);
		BlockEntityRendererFactories.register(registry.ENCHANTING_TABLE_ENTITY, ZenchantingTableBlockEntityRenderer::new);
	}
}