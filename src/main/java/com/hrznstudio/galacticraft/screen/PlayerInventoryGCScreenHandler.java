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
import com.hrznstudio.galacticraft.Constants;
import com.hrznstudio.galacticraft.accessor.GCPlayerAccessor;
import com.hrznstudio.galacticraft.screen.slot.ItemSpecificSlot;
import com.hrznstudio.galacticraft.items.GalacticraftItems;
import com.hrznstudio.galacticraft.items.OxygenTankItem;
import com.hrznstudio.galacticraft.items.ThermalArmorItem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Identifier;

import javax.annotation.Nullable;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
public class PlayerInventoryGCScreenHandler extends ScreenHandler {
    private static final Identifier[] EMPTY_ARMOR_SLOT_IDS = new Identifier[]{
            new Identifier(Constants.MOD_ID, Constants.SlotSprites.THERMAL_BOOTS),
            new Identifier(Constants.MOD_ID, Constants.SlotSprites.THERMAL_PANTS),
            new Identifier(Constants.MOD_ID, Constants.SlotSprites.THERMAL_CHEST),
            new Identifier(Constants.MOD_ID, Constants.SlotSprites.THERMAL_HEAD)};
    private static final EquipmentSlot[] EQUIPMENT_SLOT_ORDER = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public static final int OXYGEN_TANK_1_SLOT = 6;
    public static final int OXYGEN_TANK_2_SLOT = 7;

    public final Inventory inventory;

    private final PlayerEntity player;

    public PlayerInventoryGCScreenHandler(PlayerInventory playerInventory, PlayerEntity playerEntity) {
        super(null, 1);

        this.player = playerEntity;
        this.inventory = new InventoryFixedWrapper(((GCPlayerAccessor) player).getGearInventory()) {
            @Override
            public boolean canPlayerUse(PlayerEntity player) {
                return player.getUuid() == PlayerInventoryGCScreenHandler.this.player.getUuid();
            }
        };

        for (int slotY = 0; slotY < 4; ++slotY) {
            EquipmentSlot slot = EQUIPMENT_SLOT_ORDER[slotY];
            int finalSlotY = slotY;
            this.addSlot(new Slot(this.inventory, finalSlotY, 8, 8 + slotY * 18) {
                @Override
                public int getMaxStackAmount() {
                    return 1;
                }

                @Override
                public boolean canInsert(ItemStack itemStack_1) {
                    return slot == getPreferredEquipmentSlot(itemStack_1);
                }

                @Override
                public boolean canTakeItems(PlayerEntity playerEntity) {
                    return player.getUuid() == playerEntity.getUuid();
                }

                @Override
                public Pair<Identifier, Identifier> getBackgroundSprite() {
                    return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, EMPTY_ARMOR_SLOT_IDS[slot.getEntitySlotId()]);
                }
            });
        }

        this.addSlot(new ItemSpecificSlot(this.inventory, 4, 80, 8, GalacticraftItems.OXYGEN_MASK) {
            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Constants.MOD_ID, Constants.SlotSprites.OXYGEN_MASK));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return player.getUuid() == playerEntity.getUuid();
            }
        });

        this.addSlot(new ItemSpecificSlot(this.inventory, 5, 80, 8 + 18, GalacticraftItems.OXYGEN_GEAR) {
            @Override
            public Pair<Identifier, Identifier> getBackgroundSprite() {
                return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Constants.MOD_ID, Constants.SlotSprites.OXYGEN_GEAR));
            }

            @Override
            public boolean canTakeItems(PlayerEntity playerEntity) {
                return player.getUuid() == playerEntity.getUuid();
            }
        });
        this.addSlot(new OxygenTankSlot(this.inventory, OXYGEN_TANK_1_SLOT, 80, 8 + 2 * 18));
        this.addSlot(new OxygenTankSlot(this.inventory, OXYGEN_TANK_2_SLOT, 80, 8 + 3 * 18));

        int accessorySlot = 0;
        for (int i = 8; i < 12; i++) {
            this.addSlot(new Slot(this.inventory, i, 80 + 18, 8 + accessorySlot * 18));
            accessorySlot++;
        }


        // Player main inv
        for (int slotY = 0; slotY < 3; ++slotY) {
            for (int slotX = 0; slotX < 9; ++slotX) {
                this.addSlot(new Slot(playerInventory, slotX + (slotY + 1) * 9, 8 + slotX * 18, 84 + slotY * 18));
            }
        }

        // Player hotbar
        for (int slotY = 0; slotY < 9; ++slotY) {
            this.addSlot(new Slot(playerInventory, slotY, 8 + slotY * 18, 142));
        }
    }

    private EquipmentSlot getPreferredEquipmentSlot(ItemStack itemStack_1) {
        Item item_1 = itemStack_1.getItem();
        return ((ThermalArmorItem) item_1).getSlotType();
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return player.getUuid().equals(this.player.getUuid());
    }

    private static class OxygenTankSlot extends Slot {
        public OxygenTankSlot(Inventory gearInventory, int slotId, int x, int y) {
            super(gearInventory, slotId, x, y);
        }

        @Override
        public boolean canInsert(ItemStack itemStack_1) {
            return itemStack_1.getItem() instanceof OxygenTankItem;
        }

        @Override
        public int getMaxStackAmount() {
            return 1;
        }

        @Nullable
        @Override
        public Pair<Identifier, Identifier> getBackgroundSprite() {
            return Pair.of(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, new Identifier(Constants.MOD_ID, Constants.SlotSprites.OXYGEN_TANK));
        }
    }
}