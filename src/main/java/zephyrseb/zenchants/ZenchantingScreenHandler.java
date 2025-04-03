package zephyrseb.zenchants;

import net.minecraft.block.BlockState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.OptionalInt;

public class ZenchantingScreenHandler extends ForgingScreenHandler {
    public static final int SLOT_0_X = 8 + 24;
    public static final int SLOT_1_X = 26 + 24;
    public static final int SLOT_2_X = 44 + 24;
    private static final int OUTPUT_X = 98 + 24;
    public static final int SLOT_Y = 48;
    private final World world;
    @Nullable
    private RecipeEntry<EnchantingRecipe> currentRecipe;
    public int errorCode = 0;
    private final Property levelCost;

    public ZenchantingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY);
    }

    public ZenchantingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(registry.ENCHANTING_SCREEN_HANDLER, syncId, playerInventory, context, createForgingSlotsManager());
        this.world = playerInventory.player.getWorld();
        this.levelCost = Property.create();
        this.addProperty(this.levelCost);
    }

    //Creates slots, using tag lists to determine what can go into the slots
    protected static ForgingSlotsManager createForgingSlotsManager() {
        ForgingSlotsManager.Builder var10000 = ForgingSlotsManager.builder();
        var10000 = var10000.input(0, SLOT_0_X, SLOT_Y, x -> x.isIn(registry.ZENCHANTING_ENCHANTABLE));
        var10000 = var10000.input(1, SLOT_1_X, SLOT_Y, x -> x.isIn(registry.ZENCHANTING_INGREDIENTS));
        return var10000.input(2, SLOT_2_X, SLOT_Y, x -> x.isIn(registry.ZENCHANTING_CONDUITS)).output(3, OUTPUT_X, SLOT_Y).build();
    }

    @Override
    protected boolean canUse(BlockState state) {
        return state.isOf(registry.ENCHANTING_TABLE);
    }

    @Override
    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return this.currentRecipe != null && this.currentRecipe.value().matches(this.createRecipeInput(), this.world);
    }

    //Runs when the player takes to proposed output, consuming resources and levels
    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraftByPlayer(player, stack.getCount());
        this.decrementStack(0);
        this.decrementStack(1);
        this.decrementStack(2);
        this.context.run((world, pos) -> {
                    if (!player.isInCreativeMode()) {
                        player.applyEnchantmentCosts(stack, getLevelCost());
                    }
                });
        world.playSound(null, player.getBlockPos(), SoundEvents.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, world.random.nextFloat() * 0.1F + 0.9F);
    }

    private EnchantingRecipeInput createRecipeInput() {
        return new EnchantingRecipeInput(this.input.getStack(0), this.input.getStack(1), this.input.getStack(2));
    }

    private void decrementStack(int slot) {
        ItemStack itemStack = this.input.getStack(slot);
        if (!itemStack.isEmpty()) {
            itemStack.decrement(1);
            this.input.setStack(slot, itemStack);
        }
    }

    //Updates the output slot to show the result of crafting
    @Override
    public void updateResult() {
        EnchantingRecipeInput zenchantingRecipeInput = this.createRecipeInput();
        Optional<RecipeEntry<EnchantingRecipe>> list;
        if (this.world instanceof ServerWorld serverWorld) {
             list = serverWorld.getRecipeManager().getFirstMatch(registry.ENCHANTING, zenchantingRecipeInput, this.world);
        }
        else {
            list = Optional.empty();
        }

        int level = this.player.experienceLevel;
        if (list.isEmpty()) {
            this.output.setStack(0, ItemStack.EMPTY);
            this.errorCode = 1;
        } else if (this.input.getStack(0).getItem() == Items.BOOK && this.input.getStack(1).getItem() == Items.ENCHANTED_BOOK) {
            this.output.setStack(0, ItemStack.EMPTY);
            this.errorCode = 1;
        } else {
            //Sets the output itemstack to show the result of enchanting
            boolean bl = true;
            ItemStack stack1 = this.input.getStack(0);
            RecipeEntry<EnchantingRecipe> recipeEntry = list.get();
            ItemEnchantmentsComponent storedEnchantmentsComponent = stack1.getComponents().get(DataComponentTypes.STORED_ENCHANTMENTS);
            ItemEnchantmentsComponent resultEnchantmentsComponent = recipeEntry.value().craft(zenchantingRecipeInput, this.world.getRegistryManager()).getComponents().get(DataComponentTypes.STORED_ENCHANTMENTS);
            if (stack1.getEnchantments() == recipeEntry.value().craft(zenchantingRecipeInput, this.world.getRegistryManager()).getEnchantments() && storedEnchantmentsComponent == resultEnchantmentsComponent) {
                bl = false;
                this.errorCode = 2;
            }
            if ((getLevelCost() > level && !this.player.isInCreativeMode())) {
                bl = false;
                this.errorCode = 3;
            }
            if (bl) {
                ItemStack itemStack = recipeEntry.value().craft(zenchantingRecipeInput, this.world.getRegistryManager());
                if (itemStack.isItemEnabled(this.world.getEnabledFeatures())) {
                    this.currentRecipe = recipeEntry;
                    this.output.setLastRecipe(recipeEntry);
                    this.output.setStack(0, itemStack);
                }
            } else {
                this.output.setStack(0, ItemStack.EMPTY);
            }

            //Determines the experience cost of the enchantment
            if (this.output.getStack(0) != ItemStack.EMPTY) {
                this.context.run((world, pos) -> {
                    if (recipeEntry.value().craftCost(zenchantingRecipeInput) > 0) {
                        setLevelCost(recipeEntry.value().craftCost(zenchantingRecipeInput));
                    }
                    int ix = 0;
                    //Apply bookshelf discount
                    for (BlockPos blockPos : ZenchantingTableBlock.POWER_PROVIDER_OFFSETS) {
                        if (ZenchantingTableBlock.canAccessPowerProvider(world, pos, blockPos)) {
                            ix++;
                        }
                    }
                    ix = Math.min(ix, 15);
                    setLevelCost((int) Math.ceil(getLevelCost() * ((61d - (3 * ix)) / 64d)));

                    this.sendContentUpdates();
                });
            }
        }
    }

    @Override
    public boolean canInsertIntoSlot(ItemStack stack, Slot slot) {
        return slot.inventory != this.output && super.canInsertIntoSlot(stack, slot);
    }

    @Override
    public boolean isValidIngredient(ItemStack stack) {
        return this.getQuickMoveSlot(stack).isPresent();
    }

    private OptionalInt getQuickMoveSlot(ItemStack stack) {
        if (stack.isIn(registry.ZENCHANTING_ENCHANTABLE)) return OptionalInt.of(0);
        else if (stack.isIn(registry.ZENCHANTING_INGREDIENTS)) return OptionalInt.of(1);
        else if (stack.isIn(registry.ZENCHANTING_CONDUITS)) return OptionalInt.of(2);
        else return OptionalInt.of(0);
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getLevelCost() {
        return this.levelCost.get();
    }

    public void setLevelCost(int i) {
        this.levelCost.set(i);
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }
}