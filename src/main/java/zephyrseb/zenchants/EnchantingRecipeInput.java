package zephyrseb.zenchants;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.input.RecipeInput;

public record EnchantingRecipeInput(ItemStack base, ItemStack addition, ItemStack itemCost) implements RecipeInput {
    @Override
    public ItemStack getStackInSlot(int slot) {
        return switch (slot) {
            case 0 -> this.base;
            case 1 -> this.addition;
            case 2 -> this.itemCost;
            default -> throw new IllegalArgumentException("Recipe does not contain slot " + slot);
        };
    }

    @Override
    public int size() {
        return 3;
    }

    @Override
    public boolean isEmpty() {
        return this.base.isEmpty() && this.addition.isEmpty() && this.itemCost.isEmpty();
    }

    public ItemStack base() {
        return this.base;
    }

    public ItemStack addition() {
        return this.addition;
    }

    public ItemStack itemCost() {
        return this.itemCost;
    }
}
