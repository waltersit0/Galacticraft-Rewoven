package com.hrznstudio.galacticraft.mixin;

import com.hrznstudio.galacticraft.world.biome.source.MoonBiomeSource;
import com.hrznstudio.galacticraft.world.gen.chunk.MoonChunkGenerator;
import com.hrznstudio.galacticraft.world.gen.chunk.MoonChunkGeneratorConfig;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorConfig;
import net.minecraft.world.gen.chunk.ChunkGeneratorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkGeneratorType.class)
public class ChunkGeneratorTypeMixin<C extends ChunkGeneratorConfig, T extends ChunkGenerator<C>> {

    @Inject(method = "create", at = @At("HEAD"), cancellable = true)
    private void create(World world_1, BiomeSource biomeSource_1, C chunkGeneratorConfig_1, CallbackInfoReturnable<T> cir) {
        if (biomeSource_1 instanceof MoonBiomeSource) {
            cir.setReturnValue((T)new MoonChunkGenerator(world_1, biomeSource_1, (MoonChunkGeneratorConfig) chunkGeneratorConfig_1));
        }
    }
}
