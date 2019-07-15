package com.hrznstudio.galacticraft.world.biome.source;

import com.hrznstudio.galacticraft.world.biome.GalacticraftBiomes;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.biome.source.BiomeSourceConfig;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
public class MoonBiomeSourceConfig implements BiomeSourceConfig {
    private Biome[] biomes;
    private int size;

    public MoonBiomeSourceConfig() {
        this.biomes = new Biome[]{GalacticraftBiomes.MOON, GalacticraftBiomes.MOON_PLAINS};
        this.size = 1;
    }

    public MoonBiomeSourceConfig setBiomes(Biome[] biomes_1) {
        this.biomes = biomes_1;
        return this;
    }

    public MoonBiomeSourceConfig setSize(int int_1) {
        this.size = int_1;
        return this;
    }

    public Biome[] getBiomes() {
        return this.biomes;
    }

    public int getSize() {
        return this.size;
    }
}