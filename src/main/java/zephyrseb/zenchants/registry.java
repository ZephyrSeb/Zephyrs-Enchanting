package zephyrseb.zenchants;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.recipe.*;
import net.minecraft.recipe.display.CuttingRecipeDisplay;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.resource.ResourceFinder;
import net.minecraft.resource.featuretoggle.FeatureFlags;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class registry {

    public static final Identifier REINFORCED_PATCH_ID = Identifier.of("zenchants", "reinforced_patch");
    public static final Identifier SHINING_STAR_ID = Identifier.of("zenchants", "shining_star");
    public static final Identifier GUARDIAN_SPIKE_ID = Identifier.of("zenchants", "guardian_spike");
    public static final Identifier SNOWFLAKE_ID = Identifier.of("zenchants", "snowflake");
    public static final Identifier BLADE_FRAGMENT_ID = Identifier.of("zenchants", "blade_fragment");
    public static final Identifier BONE_SHARD_ID = Identifier.of("zenchants", "bone_shard");
    public static final Identifier FIRE_DIAMOND_ID = Identifier.of("zenchants", "fire_diamond");
    public static final Identifier SULFUR_DIAMOND_ID = Identifier.of("zenchants", "sulfur_diamond");
    public static final Identifier EARTH_DIAMOND_ID = Identifier.of("zenchants", "earth_diamond");
    public static final Identifier SWEEPING_BLADE_ID = Identifier.of("zenchants", "sweeping_blade");
    public static final Identifier ARROW_HEAD_ID = Identifier.of("zenchants", "arrow_head");
    public static final Identifier ARROWBOROS_ID = Identifier.of("zenchants", "arrowboros");
    public static final Identifier SHARK_TOOTH_ID = Identifier.of("zenchants", "shark_tooth");
    public static final Identifier ARROW_WHEEL_ID = Identifier.of("zenchants", "arrow_wheel");
    public static final Identifier CLOTH_SCRAP_ID = Identifier.of("zenchants", "cloth_scrap");
    public static final Identifier POLISH_ID = Identifier.of("zenchants", "polish");
    public static final Identifier ENCHANTING_TABLE_ID = Identifier.of("zenchants", "enchanting_table");

    public static final RegistryKey<Item> REINFORCED_PATCH_KEY = RegistryKey.of(RegistryKeys.ITEM, REINFORCED_PATCH_ID);
    public static final RegistryKey<Item> SHINING_STAR_KEY = RegistryKey.of(RegistryKeys.ITEM, SHINING_STAR_ID);
    public static final RegistryKey<Item> GUARDIAN_SPIKE_KEY = RegistryKey.of(RegistryKeys.ITEM, GUARDIAN_SPIKE_ID);
    public static final RegistryKey<Item> SNOWFLAKE_KEY = RegistryKey.of(RegistryKeys.ITEM, SNOWFLAKE_ID);
    public static final RegistryKey<Item> BLADE_FRAGMENT_KEY = RegistryKey.of(RegistryKeys.ITEM, BLADE_FRAGMENT_ID);
    public static final RegistryKey<Item> BONE_SHARD_KEY = RegistryKey.of(RegistryKeys.ITEM, BONE_SHARD_ID);
    public static final RegistryKey<Item> FIRE_DIAMOND_KEY = RegistryKey.of(RegistryKeys.ITEM, FIRE_DIAMOND_ID);
    public static final RegistryKey<Item> SULFUR_DIAMOND_KEY = RegistryKey.of(RegistryKeys.ITEM, SULFUR_DIAMOND_ID);
    public static final RegistryKey<Item> EARTH_DIAMOND_KEY = RegistryKey.of(RegistryKeys.ITEM, EARTH_DIAMOND_ID);
    public static final RegistryKey<Item> SWEEPING_BLADE_KEY = RegistryKey.of(RegistryKeys.ITEM, SWEEPING_BLADE_ID);
    public static final RegistryKey<Item> ARROW_HEAD_KEY = RegistryKey.of(RegistryKeys.ITEM, ARROW_HEAD_ID);
    public static final RegistryKey<Item> ARROWBOROS_KEY = RegistryKey.of(RegistryKeys.ITEM, ARROWBOROS_ID);
    public static final RegistryKey<Item> SHARK_TOOTH_KEY = RegistryKey.of(RegistryKeys.ITEM, SHARK_TOOTH_ID);
    public static final RegistryKey<Item> ARROW_WHEEL_KEY = RegistryKey.of(RegistryKeys.ITEM, ARROW_WHEEL_ID);
    public static final RegistryKey<Item> CLOTH_SCRAP_KEY = RegistryKey.of(RegistryKeys.ITEM, CLOTH_SCRAP_ID);
    public static final RegistryKey<Item> POLISH_KEY = RegistryKey.of(RegistryKeys.ITEM, POLISH_ID);
    public static final RegistryKey<Block> ENCHANTING_TABLE_KEY = RegistryKey.of(RegistryKeys.BLOCK, ENCHANTING_TABLE_ID);
    public static final RegistryKey<Item> ENCHANTING_TABLE_KEY_ITEM = RegistryKey.of(RegistryKeys.ITEM, ENCHANTING_TABLE_ID);

    public static final Item REINFORCED_PATCH = new Item(new Item.Settings().rarity(Rarity.UNCOMMON).registryKey(REINFORCED_PATCH_KEY));
    public static final Item SHINING_STAR = new Item(new Item.Settings().rarity(Rarity.UNCOMMON).registryKey(SHINING_STAR_KEY));
    public static final Item GUARDIAN_SPIKE = new Item(new Item.Settings().registryKey(GUARDIAN_SPIKE_KEY));
    public static final Item SNOWFLAKE = new Item(new Item.Settings().rarity(Rarity.UNCOMMON).registryKey(SNOWFLAKE_KEY));
    public static final Item BLADE_FRAGMENT = new Item(new Item.Settings().registryKey(BLADE_FRAGMENT_KEY));
    public static final Item BONE_SHARD = new Item(new Item.Settings().registryKey(BONE_SHARD_KEY));
    public static final Item FIRE_DIAMOND = new Item(new Item.Settings().registryKey(FIRE_DIAMOND_KEY));
    public static final Item SULFUR_DIAMOND = new Item(new Item.Settings().registryKey(SULFUR_DIAMOND_KEY));
    public static final Item EARTH_DIAMOND = new Item(new Item.Settings().registryKey(EARTH_DIAMOND_KEY));
    public static final Item SWEEPING_BLADE = new Item(new Item.Settings().registryKey(SWEEPING_BLADE_KEY));
    public static final Item ARROW_HEAD = new Item(new Item.Settings().registryKey(ARROW_HEAD_KEY));
    public static final Item ARROWBOROS = new Item(new Item.Settings().rarity(Rarity.UNCOMMON).registryKey(ARROWBOROS_KEY));
    public static final Item SHARK_TOOTH = new Item(new Item.Settings().registryKey(SHARK_TOOTH_KEY));
    public static final Item ARROW_WHEEL = new Item(new Item.Settings().registryKey(ARROW_WHEEL_KEY));
    public static final Item CLOTH_SCRAP = new Item(new Item.Settings().rarity(Rarity.UNCOMMON).registryKey(CLOTH_SCRAP_KEY));
    public static final Item POLISH = new Item(new Item.Settings().registryKey(POLISH_KEY));
    public static final Block ENCHANTING_TABLE = new ZenchantingTableBlock(Block.Settings.create().strength(4.0f).registryKey(ENCHANTING_TABLE_KEY));
    public static final RegistryKey<RecipePropertySet> ZENCHANTING_BASE = RegistryKey.of(RecipePropertySet.REGISTRY, Identifier.of("zenchants", "zenchanting_base"));
    public static final RegistryKey<RecipePropertySet> ZENCHANTING_ADDITION = RegistryKey.of(RecipePropertySet.REGISTRY, Identifier.of("zenchants", "zenchanting_addition"));
    public static final RegistryKey<RecipePropertySet> ZENCHANTING_ITEM_COST = RegistryKey.of(RecipePropertySet.REGISTRY, Identifier.of("zenchants", "zenchanting_item_cost"));

    public static final BlockEntityType<ZenchantingTableBlockEntity> ENCHANTING_TABLE_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            Identifier.of("zenchants", "enchanting_table_entity"),
            FabricBlockEntityTypeBuilder.create(ZenchantingTableBlockEntity::new, ENCHANTING_TABLE).build()
    );
    public static final ScreenHandlerType<ZenchantingScreenHandler> ENCHANTING_SCREEN_HANDLER = Registry.register(
            Registries.SCREEN_HANDLER,
            Identifier.of("zenchants", "enchanting"),
            new ScreenHandlerType<>(ZenchantingScreenHandler::new, FeatureFlags.VANILLA_FEATURES)
    );
    public static final RecipeType<EnchantingRecipe> ENCHANTING = Registry.register(
            Registries.RECIPE_TYPE,
            Identifier.of("zenchants", "enchanting"),
            new RecipeType<EnchantingRecipe>() {
                @Override
                public String toString() {return "enchanting";}
            }
    );
    public static final RecipeSerializer<EnchantingTransformRecipe> ENCHANTING_TRANSFORM = Registry.register(
            Registries.RECIPE_SERIALIZER,
            Identifier.of("zenchants", "enchanting"),
            new EnchantingTransformRecipe.Serializer()
    );

    public static void register() {
        Registry.register(Registries.ITEM, REINFORCED_PATCH_ID, REINFORCED_PATCH);
        Registry.register(Registries.ITEM, SHINING_STAR_ID, SHINING_STAR);
        Registry.register(Registries.ITEM, GUARDIAN_SPIKE_ID, GUARDIAN_SPIKE);
        Registry.register(Registries.ITEM, SNOWFLAKE_ID, SNOWFLAKE);
        Registry.register(Registries.ITEM, BLADE_FRAGMENT_ID, BLADE_FRAGMENT);
        Registry.register(Registries.ITEM, BONE_SHARD_ID, BONE_SHARD);
        Registry.register(Registries.ITEM, FIRE_DIAMOND_ID, FIRE_DIAMOND);
        Registry.register(Registries.ITEM, SULFUR_DIAMOND_ID, SULFUR_DIAMOND);
        Registry.register(Registries.ITEM, EARTH_DIAMOND_ID, EARTH_DIAMOND);
        Registry.register(Registries.ITEM, SWEEPING_BLADE_ID, SWEEPING_BLADE);
        Registry.register(Registries.ITEM, ARROW_HEAD_ID, ARROW_HEAD);
        Registry.register(Registries.ITEM, ARROWBOROS_ID, ARROWBOROS);
        Registry.register(Registries.ITEM, SHARK_TOOTH_ID, SHARK_TOOTH);
        Registry.register(Registries.ITEM, ARROW_WHEEL_ID, ARROW_WHEEL);
        Registry.register(Registries.ITEM, CLOTH_SCRAP_ID, CLOTH_SCRAP);
        Registry.register(Registries.ITEM, POLISH_ID, POLISH);

        Registry.register(Registries.BLOCK, Identifier.of("zenchants","enchanting_table"), ENCHANTING_TABLE);
        Registry.register(Registries.ITEM, Identifier.of("zenchants", "enchanting_table"), new BlockItem(ENCHANTING_TABLE, new Item.Settings().registryKey(ENCHANTING_TABLE_KEY_ITEM)));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(content -> content.add(ENCHANTING_TABLE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(content -> {
            content.add(CLOTH_SCRAP);
            content.add(BONE_SHARD);
            content.add(BLADE_FRAGMENT);
            content.add(SWEEPING_BLADE);
            content.add(ARROW_HEAD);
            content.add(ARROW_WHEEL);
            content.add(ARROWBOROS);
            content.add(GUARDIAN_SPIKE);
            content.add(SHARK_TOOTH);
            content.add(POLISH);
            content.add(REINFORCED_PATCH);
            content.add(SHINING_STAR);
            content.add(FIRE_DIAMOND);
            content.add(SULFUR_DIAMOND);
            content.add(EARTH_DIAMOND);
            content.add(SNOWFLAKE);
        });
    }
}
