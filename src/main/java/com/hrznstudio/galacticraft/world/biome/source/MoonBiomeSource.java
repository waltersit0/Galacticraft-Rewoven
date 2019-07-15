package com.hrznstudio.galacticraft.world.biome.source;

import com.google.common.collect.Sets;
import com.hrznstudio.galacticraft.world.biome.GalacticraftBiomes;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.feature.StructureFeature;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
public class MoonBiomeSource extends BiomeSource {
    private final Biome[] biomes = {GalacticraftBiomes.MOON, GalacticraftBiomes.MOON_PLAINS};
    private final int gridSize;

    public MoonBiomeSource(MoonBiomeSourceConfig moonBiomeSourceConfig) {
        moonBiomeSourceConfig = moonBiomeSourceConfig.setBiomes(biomes);
        this.gridSize = moonBiomeSourceConfig.getSize() + 4;
    }

    public Biome getBiome(int int_1, int int_2) {
        return this.biomes[Math.abs(((int_1 >> this.gridSize) + (int_2 >> this.gridSize)) % this.biomes.length)];
    }

    public Biome[] sampleBiomes(int int_1, int int_2, int int_3, int int_4, boolean boolean_1) {
        Biome[] biomes_1 = new Biome[int_3 * int_4];

        for(int int_5 = 0; int_5 < int_4; ++int_5) {
            for(int int_6 = 0; int_6 < int_3; ++int_6) {
                int int_7 = Math.abs(((int_1 + int_5 >> this.gridSize) + (int_2 + int_6 >> this.gridSize)) % this.biomes.length);
                Biome biome_1 = this.biomes[int_7];
                biomes_1[int_5 * int_3 + int_6] = biome_1;
            }
        }

        return biomes_1;
    }

    @Nullable
    public BlockPos locateBiome(int int_1, int int_2, int int_3, List<Biome> list_1, Random random_1) {
        return null;
    }

    public boolean hasStructureFeature(StructureFeature<?> structureFeature_1) {
        return this.structureFeatures.computeIfAbsent(structureFeature_1, (structureFeature_1x) -> {
            for (Biome biome_1 : this.biomes) {
                if (biome_1.hasStructureFeature(structureFeature_1x)) {
                    return true;
                }
            }
            return false;
        });
    }

    public Set<BlockState> getTopMaterials() {
        if (this.topMaterials.isEmpty()) {
            for (Biome biome_1 : this.biomes) {
                this.topMaterials.add(biome_1.getSurfaceConfig().getTopMaterial());
            }
        }
        return this.topMaterials;
    }

    public Set<Biome> getBiomesInArea(int int_1, int int_2, int int_3) {
        return Sets.newHashSet(this.biomes);
    }
}
