/*
 * Copyright (c) 2019 HRZN LTD
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.hrznstudio.galacticraft.screen;

import alexiil.mc.lib.attributes.item.compat.InventoryFixedWrapper;
import com.hrznstudio.galacticraft.block.entity.CompressorBlockEntity;
import com.hrznstudio.galacticraft.screen.slot.ItemSpecificSlot;
import net.fabricmc.fabric.api.container.ContainerFactory;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.screen.Property;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.FurnaceOutputSlot;
import net.minecraft.screen.slot.Slot;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
public class CompressorScreenHandler extends MachineScreenHandler<CompressorBlockEntity> {

    public static final ContainerFactory<ScreenHandler> FACTORY = createFactory(CompressorBlockEntity.class, CompressorScreenHandler::new);
    public final Property status = Property.create();
    public final Property progress = Property.create();
    public final Property fuelTime = Property.create();
    protected final Inventory inventory;

    public CompressorScreenHandler(int syncId, PlayerEntity playerEntity, CompressorBlockEntity blockEntity) {
        super(syncId, playerEntity, blockEntity);
        this.inventory = new InventoryFixedWrapper(blockEntity.getInventory()) {
            @Override
            public boolean canPlayerUse(PlayerEntity player) {
                return CompressorScreenHandler.this.canUse(player);
            }
        };
        addProperty(status);
        addProperty(progress);
        addProperty(fuelTime);

        // 3x3 compressor input grid
        int slot = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                this.addSlot(new Slot(this.inventory, slot, x * 18 + 19, y * 18 + 18));
                slot++;
            }
        }

        // Fuel slot
        this.addSlot(new ItemSpecificSlot(this.inventory, CompressorBlockEntity.FUEL_INPUT_SLOT, 3 * 18 + 1, 75, AbstractFurnaceBlockEntity.createFuelTimeMap().keySet().toArray(new Item[0])));

        // Output slot
        this.addSlot(new FurnaceOutputSlot(playerEntity, this.inventory, CompressorBlockEntity.OUTPUT_SLOT, getOutputSlotPos()[0], getOutputSlotPos()[1]));

        // Player inventory slots
        int playerInvYOffset = 110;
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerEntity.inventory, j + i * 9 + 9, 8 + j * 18, playerInvYOffset + i * 18));
            }
        }

        // Hotbar slots
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerEntity.inventory, i, 8 + i * 18, playerInvYOffset + 58));
        }

    }

    protected int[] getOutputSlotPos() {
        return new int[]{138, 38};
    }

    @Override
    public void sendContentUpdates() {
        status.set(blockEntity.status.ordinal());
        progress.set(blockEntity.getProgress());
        fuelTime.set(blockEntity.fuelTime);
        super.sendContentUpdates();
    }


    @Override
    public void setProperty(int id, int value) {
        super.setProperty(id, value);
        blockEntity.status = CompressorBlockEntity.CompressorStatus.get(status.get());
        blockEntity.progress = progress.get();
        blockEntity.fuelTime = fuelTime.get();
    }
}
