package zephyrseb.zenchants;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.world.World;

import java.util.Optional;

public interface EnchantingRecipe extends Recipe<EnchantingRecipeInput> {
    @Override
    default RecipeType<EnchantingRecipe> getType() {
        return registry.ENCHANTING;
    }

    default boolean fits(int width, int height) {
        return width >= 3 && height >= 1;
    }

    default ItemStack createIcon() {
        return new ItemStack(Blocks.ENCHANTING_TABLE);
    }

    @Override
    default boolean isIgnoredInRecipeBook() {
        return true;
    }

    @Override
    default String getGroup() {
        return "";
    }

    @Override
    default boolean showNotification() {
        return false;
    }

    boolean testAddition(ItemStack stack);

    boolean testItemCost(ItemStack stack);

    default int craftCost(EnchantingRecipeInput enchantingRecipeInput) {
        return 0;
    }

    default boolean matches(EnchantingRecipeInput enchantingRecipeInput, World world) {
        return Ingredient.matches(this.base(), enchantingRecipeInput.base()) && Ingredient.matches(this.addition(), enchantingRecipeInput.addition()) && Ingredient.matches(this.itemCost(), enchantingRecipeInput.itemCost());
    }

    Optional<Ingredient> base();

    Optional<Ingredient> addition();

    Optional<Ingredient> itemCost();
}
