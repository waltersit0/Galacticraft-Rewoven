package com.hrznstudio.galacticraft.world.biome.source;

import com.google.common.collect.Sets;
import com.hrznstudio.galacticraft.world.biome.GalacticraftBiomes;
import com.hrznstudio.galacticraft.world.gen.chunk.MoonChunkGeneratorConfig;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.layer.BiomeLayerSampler;
import net.minecraft.world.biome.layer.BiomeLayers;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.level.LevelProperties;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
public class MoonBiomeSource extends BiomeSource {

    private final BiomeLayerSampler noiseLayer;
    private final BiomeLayerSampler biomeLayer;
    private final Biome[] biomes;

    public MoonBiomeSource(MoonBiomeSourceConfig moonBiomeSourceConfig) {
        this.biomes = new Biome[]{GalacticraftBiomes.MOON_PLAINS, GalacticraftBiomes.MOON};
        LevelProperties levelProperties_1 = moonBiomeSourceConfig.getLevelProperties();
        MoonChunkGeneratorConfig moonChunkGeneratorConfig = moonBiomeSourceConfig.getGeneratorSettings();
        BiomeLayerSampler[] biomeLayerSamplers_1 = BiomeLayers.build(levelProperties_1.getSeed(), levelProperties_1.getGeneratorType(), moonChunkGeneratorConfig);
        this.noiseLayer = biomeLayerSamplers_1[0];
        this.biomeLayer = biomeLayerSamplers_1[1];
    }

    public Biome getBiome(int int_1, int int_2) {
        return this.biomeLayer.sample(int_1, int_2);
    }

    public Biome getBiomeForNoiseGen(int int_1, int int_2) {
        return this.noiseLayer.sample(int_1, int_2);
    }

    public Biome[] sampleBiomes(int int_1, int int_2, int int_3, int int_4, boolean boolean_1) {
        return this.biomeLayer.sample(int_1, int_2, int_3, int_4);
    }

    public Set<Biome> getBiomesInArea(int int_1, int int_2, int int_3) {
        int int_4 = int_1 - int_3 >> 2;
        int int_5 = int_2 - int_3 >> 2;
        int int_6 = int_1 + int_3 >> 2;
        int int_7 = int_2 + int_3 >> 2;
        int int_8 = int_6 - int_4 + 1;
        int int_9 = int_7 - int_5 + 1;
        Set<Biome> set_1 = Sets.newHashSet();
        Collections.addAll(set_1, this.noiseLayer.sample(int_4, int_5, int_8, int_9));
        return set_1;
    }

    @Nullable
    public BlockPos locateBiome(int int_1, int int_2, int int_3, List<Biome> list_1, Random random_1) {
        int int_4 = int_1 - int_3 >> 2;
        int int_5 = int_2 - int_3 >> 2;
        int int_6 = int_1 + int_3 >> 2;
        int int_7 = int_2 + int_3 >> 2;
        int int_8 = int_6 - int_4 + 1;
        int int_9 = int_7 - int_5 + 1;
        Biome[] biomes_1 = this.noiseLayer.sample(int_4, int_5, int_8, int_9);
        BlockPos blockPos_1 = null;
        int int_10 = 0;

        for(int int_11 = 0; int_11 < int_8 * int_9; ++int_11) {
            int int_12 = int_4 + int_11 % int_8 << 2;
            int int_13 = int_5 + int_11 / int_8 << 2;
            if (list_1.contains(biomes_1[int_11])) {
                if (blockPos_1 == null || random_1.nextInt(int_10 + 1) == 0) {
                    blockPos_1 = new BlockPos(int_12, 0, int_13);
                }

                ++int_10;
            }
        }

        return blockPos_1;
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
}
