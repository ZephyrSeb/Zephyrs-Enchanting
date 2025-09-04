package zephyrseb.zenchants;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.condition.RandomChanceLootCondition;
import net.minecraft.loot.condition.RandomChanceWithEnchantedBonusLootCondition;
import net.minecraft.loot.entry.AlternativeEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryKey;

public class LootTableRegistry {
    private static final RegistryKey<LootTable> SKELETON_LOOT_TABLE_ID = EntityType.SKELETON.getLootTableKey().isPresent() ? EntityType.SKELETON.getLootTableKey().get() : null;
    private static final RegistryKey<LootTable> STRAY_LOOT_TABLE_ID = EntityType.STRAY.getLootTableKey().isPresent() ? EntityType.STRAY.getLootTableKey().get() : null;
    private static final RegistryKey<LootTable> BOGGED_LOOT_TABLE_ID = EntityType.BOGGED.getLootTableKey().isPresent() ? EntityType.BOGGED.getLootTableKey().get() : null;
    private static final RegistryKey<LootTable> ZOMBIE_LOOT_TABLE_ID = EntityType.ZOMBIE.getLootTableKey().isPresent() ? EntityType.ZOMBIE.getLootTableKey().get() : null;
    private static final RegistryKey<LootTable> HUSK_LOOT_TABLE_ID = EntityType.HUSK.getLootTableKey().isPresent() ? EntityType.HUSK.getLootTableKey().get() : null;
    private static final RegistryKey<LootTable> GUARDIAN_LOOT_TABLE_ID = EntityType.GUARDIAN.getLootTableKey().isPresent() ? EntityType.GUARDIAN.getLootTableKey().get() : null;
    private static final RegistryKey<LootTable> ELDER_GUARDIAN_LOOT_TABLE_ID = EntityType.ELDER_GUARDIAN.getLootTableKey().isPresent() ? EntityType.ELDER_GUARDIAN.getLootTableKey().get() : null;
    private static final RegistryKey<LootTable> WITCH_LOOT_TABLE_ID = EntityType.WITCH.getLootTableKey().isPresent() ? EntityType.WITCH.getLootTableKey().get() : null;

    public static void registerLootTables() {
        //Adds mod-specific loot to loot tables
        LootTableEvents.MODIFY.register((id, table, setter, registries) -> {
            //Adds loot to structure chests
            if (LootTables.SIMPLE_DUNGEON_CHEST == id) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(registry.REINFORCED_PATCH).weight(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,3))))
                        .with(ItemEntry.builder(registry.BLADE_FRAGMENT).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,5))))
                        .with(ItemEntry.builder(registry.BONE_SHARD).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,5))));
                table.pool(poolBuilder);
            }
            if (LootTables.IGLOO_CHEST_CHEST == id) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(registry.SNOWFLAKE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,2))));
                table.pool(poolBuilder);
            }
            if (LootTables.END_CITY_TREASURE_CHEST == id) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(registry.REINFORCED_PATCH).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,3))))
                        .with(ItemEntry.builder(registry.SHINING_STAR).weight(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,3))))
                        .with(ItemEntry.builder(registry.SULFUR_DIAMOND).weight(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,4))))
                        .with(ItemEntry.builder(registry.FIRE_DIAMOND).weight(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,4))))
                        .with(ItemEntry.builder(registry.EARTH_DIAMOND).weight(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,4))));
                table.pool(poolBuilder);
            }
            if (LootTables.PILLAGER_OUTPOST_CHEST == id) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(registry.ARROWBOROS).weight(1))
                        .with(ItemEntry.builder(registry.ARROW_WHEEL).weight(1))
                        .with(ItemEntry.builder(registry.POLISH).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,3))))
                        .with(ItemEntry.builder(registry.ARROW_HEAD).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,5))));
                table.pool(poolBuilder);
            }
            if (LootTables.SHIPWRECK_TREASURE_CHEST == id) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .with(ItemEntry.builder(registry.REINFORCED_PATCH).weight(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,3))))
                        .with(ItemEntry.builder(registry.SULFUR_DIAMOND).weight(2))
                        .with(ItemEntry.builder(registry.FIRE_DIAMOND).weight(2))
                        .with(ItemEntry.builder(registry.EARTH_DIAMOND).weight(2));
                table.pool(poolBuilder);
            }
            if (LootTables.BURIED_TREASURE_CHEST == id) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.5f))
                        .with(ItemEntry.builder(registry.SHINING_STAR).weight(1))
                        .with(ItemEntry.builder(registry.SHARK_TOOTH).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,3))))
                        .with(ItemEntry.builder(registry.GUARDIAN_SPIKE).weight(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,3))));
                table.pool(poolBuilder);
            }
            if (LootTables.DESERT_PYRAMID_CHEST == id) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceLootCondition.builder(0.25f))
                        .with(ItemEntry.builder(registry.BONE_SHARD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3,6))));
                table.pool(poolBuilder);
            }
            if (LootTables.TRAIL_RUINS_COMMON_ARCHAEOLOGY == id) {
                table.modifyPools(builder -> builder
                        .with(AlternativeEntry.builder(ItemEntry.builder(registry.CLOTH_SCRAP).weight(2))));
            }
            if (LootTables.FISHING_TREASURE_GAMEPLAY == id) {
                table.modifyPools(builder -> builder
                        .with(AlternativeEntry.builder(ItemEntry.builder(registry.SHARK_TOOTH).weight(1))));
            }
            //Adds loot to appear in trial chambers
            if (LootTables.TRIAL_CHAMBERS_CORRIDOR_POT == id) {
                table.modifyPools(builder -> builder
                        .with(AlternativeEntry.builder(ItemEntry.builder(Items.LAPIS_LAZULI).weight(50).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2,6))))));
            }
            if (LootTables.TRIAL_CHAMBER_CONSUMABLES_SPAWNER == id) {
                table.modifyPools(builder -> builder
                        .with(AlternativeEntry.builder(ItemEntry.builder(registry.ARROW_HEAD).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2,5)))))
                        .with(AlternativeEntry.builder(ItemEntry.builder(registry.REINFORCED_PATCH).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,3)))))
                        .with(AlternativeEntry.builder(ItemEntry.builder(registry.BLADE_FRAGMENT).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,5))))));
            }
            if (LootTables.OMINOUS_TRIAL_CHAMBER_CONSUMABLES_SPAWNER == id) {
                table.modifyPools(builder -> builder
                        .with(AlternativeEntry.builder(ItemEntry.builder(registry.SHINING_STAR).weight(1)))
                        .with(AlternativeEntry.builder(ItemEntry.builder(registry.ARROWBOROS).weight(1)))
                        .with(AlternativeEntry.builder(ItemEntry.builder(registry.ARROW_HEAD).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2,5)))))
                        .with(AlternativeEntry.builder(ItemEntry.builder(registry.REINFORCED_PATCH).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,3)))))
                        .with(AlternativeEntry.builder(ItemEntry.builder(registry.BLADE_FRAGMENT).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1,5))))));
            }
            if (LootTables.TRIAL_CHAMBERS_REWARD_RARE_CHEST == id) {
                table.modifyPools(builder -> builder
                        .with(AlternativeEntry.builder(ItemEntry.builder(registry.REINFORCED_PATCH).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2,6)))))
                        .with(AlternativeEntry.builder(ItemEntry.builder(registry.SHINING_STAR).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2,4))))));
            }
            if (LootTables.TRIAL_CHAMBERS_REWARD_OMINOUS_RARE_CHEST == id) {
                table.modifyPools(builder -> builder
                        .with(AlternativeEntry.builder(ItemEntry.builder(registry.SHINING_STAR).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2,4))))));
            }
            //Adds loot to be dropped by certain types of mob
            if (id == ZOMBIE_LOOT_TABLE_ID || id == HUSK_LOOT_TABLE_ID) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(BuiltinRegistries.createWrapperLookup(), 0.1f, 0.05f))
                        .with(ItemEntry.builder(registry.BLADE_FRAGMENT));
                table.pool(poolBuilder);
            }
            if (id == SKELETON_LOOT_TABLE_ID || id == STRAY_LOOT_TABLE_ID || id == BOGGED_LOOT_TABLE_ID) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(BuiltinRegistries.createWrapperLookup(), 0.1f, 0.05f))
                        .with(ItemEntry.builder(registry.BONE_SHARD));
                table.pool(poolBuilder);
            }
            if (id == GUARDIAN_LOOT_TABLE_ID || id == ELDER_GUARDIAN_LOOT_TABLE_ID) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(BuiltinRegistries.createWrapperLookup(), 0.1f, 0.05f))
                        .with(ItemEntry.builder(registry.GUARDIAN_SPIKE));
                table.pool(poolBuilder);
            }
            if (id == WITCH_LOOT_TABLE_ID) {
                LootPool.Builder poolBuilder = LootPool.builder()
                        .rolls(ConstantLootNumberProvider.create(1))
                        .conditionally(RandomChanceWithEnchantedBonusLootCondition.builder(BuiltinRegistries.createWrapperLookup(), 0.1f, 0.05f))
                        .with(ItemEntry.builder(registry.POLISH));
                table.pool(poolBuilder);
            }
        });
    }
}
