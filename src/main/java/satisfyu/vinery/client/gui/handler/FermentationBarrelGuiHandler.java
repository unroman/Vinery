package satisfyu.vinery.client.gui.handler;

import net.minecraft.screen.slot.FurnaceOutputSlot;
import satisfyu.vinery.block.entity.FermentationBarrelBlockEntity;
import satisfyu.vinery.client.gui.handler.slot.ExtendedSlot;
import satisfyu.vinery.client.gui.handler.slot.StoveOutputSlot;
import satisfyu.vinery.registry.ObjectRegistry;
import satisfyu.vinery.registry.VineryScreenHandlerTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import satisfyu.vinery.screen.sideTip.RecipeGUIHandler;

public class FermentationBarrelGuiHandler extends RecipeGUIHandler {

    public FermentationBarrelGuiHandler(int syncId, PlayerInventory playerInventory) {
        this(syncId, playerInventory, new SimpleInventory(6), new ArrayPropertyDelegate(2));
    }
    public FermentationBarrelGuiHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(VineryScreenHandlerTypes.FERMENTATION_BARREL_GUI_HANDLER, syncId, playerInventory, inventory, 5, propertyDelegate);
        buildBlockEntityContainer(playerInventory, inventory);
        buildPlayerContainer(playerInventory);
    }

    private void buildBlockEntityContainer(PlayerInventory playerInventory, Inventory inventory) {
        // Wine input
        this.addSlot(new ExtendedSlot(inventory, 0, 63, 50, stack -> {
            return stack.isOf(Item.fromBlock(ObjectRegistry.WINE_BOTTLE));
        }));
        // Inputs
        this.addSlot(new Slot(inventory, 2, 18, 26));
        this.addSlot(new Slot(inventory, 3, 36, 26));
        this.addSlot(new Slot(inventory, 4, 18, 44));
        this.addSlot(new Slot(inventory, 5, 36, 44));
        // Output
        this.addSlot(new StoveOutputSlot(playerInventory.player, inventory, 1, 125,  35));
    }

    private void buildPlayerContainer(PlayerInventory playerInventory) {
        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    public int getScaledProgress(int arrowWidth) {
        final int progress = this.propertyDelegate.get(0);
        final int totalProgress = FermentationBarrelBlockEntity.COOKING_TIME_IN_TICKS;
        if (progress == 0) {
            return 0;
        }
        return progress * arrowWidth/ totalProgress + 1;
    }
}