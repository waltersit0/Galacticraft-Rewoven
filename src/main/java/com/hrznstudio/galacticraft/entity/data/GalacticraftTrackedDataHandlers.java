package com.hrznstudio.galacticraft.entity.data;

import net.minecraft.block.Block;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;

public class GalacticraftTrackedDataHandlers {

    public static final TrackedDataHandler<Block> BLOCK = new TrackedDataHandler<Block>() {
        @Override
        public void write(PacketByteBuf var1, Block var2) {
            var1.writeIdentifier(Registry.BLOCK.getId(var2));
        }

        @Override
        public Block read(PacketByteBuf var1) {
            return Registry.BLOCK.get(var1.readIdentifier());
        }

        @Override
        public Block copy(Block var1) {
            return var1;
        }
    };

    public static void register() {
        TrackedDataHandlerRegistry.register(BLOCK);
    }
}
