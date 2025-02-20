package zephyrseb.zenchants;

import net.fabricmc.api.ModInitializer;

public class Zenchants implements ModInitializer {

	@Override
	public void onInitialize() {
		registry.register();
		LootTableRegistry.registerLootTables();
	}
}