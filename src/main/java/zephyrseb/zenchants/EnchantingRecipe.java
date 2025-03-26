package zephyrseb.zenchants;

import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.world.World;

import java.util.Optional;

public interface EnchantingRecipe extends Recipe<EnchantingRecipeInput> {
    @Override
    default RecipeType<EnchantingRecipe> getType() {
        return registry.ENCHANTING;
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
