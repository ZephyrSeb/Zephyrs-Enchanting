package zephyrseb.zenchants;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.*;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicBoolean;

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
    public static int levelCost;

    public ZenchantingScreenHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, ScreenHandlerContext.EMPTY, playerInventory.player.getWorld());
    }

    public ZenchantingScreenHandler(int syncId, PlayerInventory playerInventory, ScreenHandlerContext context, World world) {
        super(registry.ENCHANTING_SCREEN_HANDLER, syncId, playerInventory, context, createForgingSlotsManager(world.getRecipeManager()));
        this.world = playerInventory.player.getWorld();
    }

    protected static ForgingSlotsManager createForgingSlotsManager(RecipeManager recipeManager) {
        RecipePropertySet recipePropertySet = recipeManager.getPropertySet(registry.ZENCHANTING_BASE);
        RecipePropertySet recipePropertySet2 = recipeManager.getPropertySet(registry.ZENCHANTING_ADDITION);
        RecipePropertySet recipePropertySet3 = recipeManager.getPropertySet(registry.ZENCHANTING_ITEM_COST);
        ForgingSlotsManager.Builder var10000 = ForgingSlotsManager.builder();
        Objects.requireNonNull(recipePropertySet);
        var10000 = var10000.input(0, SLOT_0_X, SLOT_Y, x -> x.isEnchantable() || x.hasEnchantments());
        Objects.requireNonNull(recipePropertySet2);
        var10000 = var10000.input(1, SLOT_1_X, SLOT_Y, x -> !x.isOf(Items.LAPIS_LAZULI));
        Objects.requireNonNull(recipePropertySet3);
        return var10000.input(2, SLOT_2_X, SLOT_Y, x -> x.isOf(Items.LAPIS_LAZULI)).output(3, OUTPUT_X, SLOT_Y).build();
    }

    @Override
    protected boolean canUse(BlockState state) {
        return state.isOf(registry.ENCHANTING_TABLE);
    }

    @Override
    protected boolean canTakeOutput(PlayerEntity player, boolean present) {
        return this.currentRecipe != null && this.currentRecipe.value().matches(this.createRecipeInput(), this.world);
    }

    @Override
    protected void onTakeOutput(PlayerEntity player, ItemStack stack) {
        stack.onCraftByPlayer(player.getWorld(), player, stack.getCount());
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
            boolean bl = true;
            ItemStack stack1 = this.input.getStack(0);
            RecipeEntry<EnchantingRecipe> recipeEntry = list.get();
            if (recipeEntry.value().craftCost(zenchantingRecipeInput) > 0) {
                setLevelCost(recipeEntry.value().craftCost(zenchantingRecipeInput));
            }
            //Trying to wrap this in a context lambda results in a client desync, but everything seems to work perfectly server side.
            this.context.run((world, pos) -> {
                int ix = 0;
                for (BlockPos blockPos : ZenchantingTableBlock.POWER_PROVIDER_OFFSETS) {
                    if (ZenchantingTableBlock.canAccessPowerProvider(world, pos, blockPos)) {
                        ix++;
                    }
                }
                ix = Math.min(ix, 15);
                setLevelCost((int) Math.ceil(getLevelCost() * ((61d - (3 * ix)) / 64d)));
            });
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
        if (stack.isEnchantable() || stack.hasEnchantments()) return OptionalInt.of(0);
        if (stack.isOf(Items.LAPIS_LAZULI)) return OptionalInt.of(2);
        return OptionalInt.of(1);
    }

    private static boolean testBook(ItemStack stack) {
        return stack.getItem() == Items.ENCHANTED_BOOK;
    }

    public static boolean isEnchantable(ItemStack stack) {
        return stack.isEnchantable() || stack.getItem() == Items.BOOK;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public int getLevelCost() {
        return levelCost;
    }

    public void setLevelCost(int i) {
        levelCost = i;
    }

    public PlayerEntity getPlayer() {
        return this.player;
    }
}