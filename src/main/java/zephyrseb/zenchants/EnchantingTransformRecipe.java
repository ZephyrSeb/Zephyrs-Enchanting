package zephyrseb.zenchants;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.IngredientPlacement;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

import static java.lang.Math.min;

public class EnchantingTransformRecipe implements EnchantingRecipe {
    final Optional<Ingredient> base;
    final Optional<Ingredient> addition;
    final Optional<Ingredient> itemCost;
    final ItemStack result;
    @Nullable
    private IngredientPlacement ingredientPlacement;

    public EnchantingTransformRecipe(Optional<Ingredient> base, Optional<Ingredient> addition, Optional<Ingredient> itemCost, ItemStack result) {
        this.base = base;
        this.addition = addition;
        this.itemCost = itemCost;
        this.result = result;
    }

    public boolean matches(EnchantingRecipeInput enchantingRecipeInput, World world) {
        return this.addition.get().test(enchantingRecipeInput.addition()) && this.itemCost.get().test(enchantingRecipeInput.itemCost());
    }

    public IngredientPlacement getIngredientPlacement() {
        if (this.ingredientPlacement == null) {
            this.ingredientPlacement = IngredientPlacement.forMultipleSlots(List.of(this.base, this.addition, this.itemCost));
        }

        return this.ingredientPlacement;
    }

    @Override
    public RecipeBookCategory getRecipeBookCategory() {
        return null;
    }

    public ItemStack craft(EnchantingRecipeInput enchantingRecipeInput, RegistryWrapper.WrapperLookup wrapperLookup) {
        ItemStack itemStack = enchantingRecipeInput.base().copyComponentsToNewStack(enchantingRecipeInput.getStackInSlot(0).getItem(), enchantingRecipeInput.getStackInSlot(0).getCount());
        if (enchantingRecipeInput.getStackInSlot(1).contains(DataComponentTypes.STORED_ENCHANTMENTS) && itemStack.getItem() != Items.BOOK) {
            ItemEnchantmentsComponent itemEnchantmentsComponent = enchantingRecipeInput.getStackInSlot(1).getComponents().get(DataComponentTypes.STORED_ENCHANTMENTS);
            if (itemEnchantmentsComponent != null) {
                for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
                    RegistryEntry<Enchantment> registryEntry = entry.getKey();
                    Enchantment enchantment = registryEntry.value();
                    int level = enchantment.getMaxLevel();
                    boolean bl = true;
                    for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry2 : enchantingRecipeInput.getStackInSlot(0).getEnchantments().getEnchantmentEntries()) {
                        if (enchantment.exclusiveSet().contains(entry2.getKey()) && entry2.getKey().value() != enchantment) {
                            bl = false;
                        }
                    }
                    if (enchantment.getApplicableItems().contains(enchantingRecipeInput.getStackInSlot(0).getItem().getRegistryEntry()) && bl) {
                        int i = itemEnchantmentsComponent.getLevel(registryEntry);
                        int j = enchantingRecipeInput.getStackInSlot(0).getEnchantments().getLevel(registryEntry);
                        if (j < level) {
                            itemStack.addEnchantment(registryEntry, min(i + j, level));
                        }
                    }
                }
            }
        } else if (itemStack.getItem() == Items.BOOK && !enchantingRecipeInput.getStackInSlot(1).contains(DataComponentTypes.STORED_ENCHANTMENTS)) {
            itemStack = Items.ENCHANTED_BOOK.getDefaultStack();
            ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(this.result);
            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
                RegistryEntry<Enchantment> registryEntry = entry.getKey();
                Enchantment enchantment = registryEntry.value();
                int level = enchantment.getMaxLevel();
                boolean bl = true;
                for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry2 : enchantingRecipeInput.getStackInSlot(0).getEnchantments().getEnchantmentEntries()) {
                    if (enchantment.exclusiveSet().contains(entry2.getKey()) && entry2.getKey().value() != enchantment) {
                        bl = false;
                    }
                }
                if (bl) {
                    int i = this.result.getEnchantments().getLevel(registryEntry);
                    int j = 0;
                    if (j < level) {
                        itemStack.addEnchantment(registryEntry, min(i + j, level));
                    }
                }
            }
        } else {
            ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(this.result);
            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
                RegistryEntry<Enchantment> registryEntry = entry.getKey();
                Enchantment enchantment = registryEntry.value();
                int level = enchantment.getMaxLevel();
                boolean bl = true;
                for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry2 : enchantingRecipeInput.getStackInSlot(0).getEnchantments().getEnchantmentEntries()) {
                    if (enchantment.exclusiveSet().contains(entry2.getKey()) && entry2.getKey().value() != enchantment) {
                        bl = false;
                    }
                }
                if (enchantment.getApplicableItems().contains(enchantingRecipeInput.getStackInSlot(0).getItem().getRegistryEntry()) && bl) {
                    int i = this.result.getEnchantments().getLevel(registryEntry);
                    int j = enchantingRecipeInput.getStackInSlot(0).getEnchantments().getLevel(registryEntry);
                    if (j < level) {
                        itemStack.addEnchantment(registryEntry, min(i + j, level));
                    }
                }
            }
        }
        return itemStack;
    }

    public int craftCost (EnchantingRecipeInput enchantingRecipeInput) {
        int out = 0;
        ItemStack itemStack = enchantingRecipeInput.base().copyComponentsToNewStack(enchantingRecipeInput.getStackInSlot(0).getItem(), enchantingRecipeInput.getStackInSlot(0).getCount());
        if (enchantingRecipeInput.getStackInSlot(1).contains(DataComponentTypes.STORED_ENCHANTMENTS) && itemStack.getItem() != Items.BOOK) {
            ItemEnchantmentsComponent itemEnchantmentsComponent = enchantingRecipeInput.getStackInSlot(1).getComponents().get(DataComponentTypes.STORED_ENCHANTMENTS);
            if (itemEnchantmentsComponent != null) {
                for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
                    RegistryEntry<Enchantment> registryEntry = entry.getKey();
                    Enchantment enchantment = registryEntry.value();
                    int level = enchantment.getMaxLevel();
                    boolean bl = true;
                    for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry2 : enchantingRecipeInput.getStackInSlot(0).getEnchantments().getEnchantmentEntries()) {
                        if (enchantment.exclusiveSet().contains(entry2.getKey()) && entry2.getKey().value() != enchantment) {
                            bl = false;
                        }
                    }
                    if (enchantment.getApplicableItems().contains(enchantingRecipeInput.getStackInSlot(0).getItem().getRegistryEntry()) && bl) {
                        int i = itemEnchantmentsComponent.getLevel(registryEntry);
                        int j = enchantingRecipeInput.getStackInSlot(0).getEnchantments().getLevel(registryEntry);
                        if (j < level) {
                            out += min(i + j, level);
                        }
                    }
                }
            }
        } else if (itemStack.getItem() == Items.BOOK && !enchantingRecipeInput.getStackInSlot(1).contains(DataComponentTypes.STORED_ENCHANTMENTS)) {
            ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(this.result);
            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
                RegistryEntry<Enchantment> registryEntry = entry.getKey();
                Enchantment enchantment = registryEntry.value();
                int level = enchantment.getMaxLevel();
                boolean bl = true;
                for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry2 : enchantingRecipeInput.getStackInSlot(0).getEnchantments().getEnchantmentEntries()) {
                    if (enchantment.exclusiveSet().contains(entry2.getKey()) && entry2.getKey().value() != enchantment) {
                        bl = false;
                    }
                }
                if (bl) {
                    int i = this.result.getEnchantments().getLevel(registryEntry);
                    int j = 0;
                    if (j < level) {
                        out += min(i + j, level);
                    }
                }
            }
        } else {
            ItemEnchantmentsComponent itemEnchantmentsComponent = EnchantmentHelper.getEnchantments(this.result);
            for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : itemEnchantmentsComponent.getEnchantmentEntries()) {
                RegistryEntry<Enchantment> registryEntry = entry.getKey();
                Enchantment enchantment = registryEntry.value();
                int level = enchantment.getMaxLevel();
                boolean bl = true;
                for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry2 : enchantingRecipeInput.getStackInSlot(0).getEnchantments().getEnchantmentEntries()) {
                    if (enchantment.exclusiveSet().contains(entry2.getKey()) && entry2.getKey().value() != enchantment) {
                        bl = false;
                    }
                }
                if (enchantment.getApplicableItems().contains(enchantingRecipeInput.getStackInSlot(0).getItem().getRegistryEntry()) && bl) {
                    int i = this.result.getEnchantments().getLevel(registryEntry);
                    int j = enchantingRecipeInput.getStackInSlot(0).getEnchantments().getLevel(registryEntry);
                    if (j < level) {
                        out += min(i + j, level);
                    }
                }
            }
        }
        return out;
    }

    public Optional<Ingredient> getResult(RegistryWrapper.WrapperLookup registriesLookup) {
        return this.base;
    }

    @Override
    public boolean testAddition(ItemStack stack) {
        return this.addition.get().test(stack);
    }

    @Override
    public boolean testItemCost(ItemStack stack) {
        return this.itemCost.get().test(stack);
    }

    @Override
    public RecipeSerializer<EnchantingTransformRecipe> getSerializer() {
        return registry.ENCHANTING_TRANSFORM;
    }

    public Optional<Ingredient> base() {
        return this.base;
    }

    public Optional<Ingredient> addition() {
        return this.addition;
    }

    public Optional<Ingredient> itemCost() {
        return this.itemCost;
    }

    public static class Serializer implements RecipeSerializer<EnchantingTransformRecipe> {
        private static final MapCodec<EnchantingTransformRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                                Ingredient.CODEC.optionalFieldOf("base").forGetter(recipe -> recipe.base),
                                Ingredient.CODEC.optionalFieldOf("ingredient").forGetter(recipe -> recipe.addition),
                                Ingredient.CODEC.optionalFieldOf("itemCost").forGetter(recipe -> recipe.itemCost),
                                ItemStack.VALIDATED_CODEC.fieldOf("result").forGetter(recipe -> recipe.result)
                        )
                        .apply(instance, EnchantingTransformRecipe::new)
        );
        public static final PacketCodec<RegistryByteBuf, EnchantingTransformRecipe> PACKET_CODEC = PacketCodec.ofStatic(
                EnchantingTransformRecipe.Serializer::write, EnchantingTransformRecipe.Serializer::read
        );

        @Override
        public MapCodec<EnchantingTransformRecipe> codec() {
            return CODEC;
        }

        @Override
        public PacketCodec<RegistryByteBuf, EnchantingTransformRecipe> packetCodec() {
            return PACKET_CODEC;
        }

        private static EnchantingTransformRecipe read(RegistryByteBuf buf) {
            Ingredient ingredient = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient ingredient2 = Ingredient.PACKET_CODEC.decode(buf);
            Ingredient ingredient3 = Ingredient.PACKET_CODEC.decode(buf);
            ItemStack enchantment = ItemStack.PACKET_CODEC.decode(buf);
            return new EnchantingTransformRecipe(Optional.ofNullable(ingredient), Optional.ofNullable(ingredient2), Optional.ofNullable(ingredient3), enchantment);
        }

        private static void write(RegistryByteBuf buf, EnchantingTransformRecipe recipe) {
            Ingredient.PACKET_CODEC.encode(buf, recipe.base.get());
            Ingredient.PACKET_CODEC.encode(buf, recipe.addition.get());
            Ingredient.PACKET_CODEC.encode(buf, recipe.itemCost.get());
            ItemStack.PACKET_CODEC.encode(buf, recipe.result);
        }
    }
}
