package com.hrznstudio.galacticraft.entity;

import com.hrznstudio.galacticraft.Constants;
import com.hrznstudio.galacticraft.Galacticraft;
import com.hrznstudio.galacticraft.entity.asteroid.OreAsteroidEntity;
import com.hrznstudio.galacticraft.entity.moonvillager.MoonVillagerEntity;
import com.hrznstudio.galacticraft.entity.moonvillager.T1RocketEntity;
import net.fabricmc.fabric.api.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityCategory;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
public class GalacticraftEntityTypes {

    public static final EntityType<MoonVillagerEntity> MOON_VILLAGER = FabricEntityTypeBuilder.create(EntityCategory.CREATURE, MoonVillagerEntity::new).build();
    public static final EntityType<T1RocketEntity> T1_ROCKET = FabricEntityTypeBuilder.create(EntityCategory.MISC, T1RocketEntity::new).size(EntityDimensions.fixed(2, 4)).build();
    public static final EntityType<OreAsteroidEntity> ORE_ASTEROID = FabricEntityTypeBuilder.create(EntityCategory.MISC, OreAsteroidEntity::new).size(EntityDimensions.fixed(1, 1)).setImmuneToFire().build();

    public static void register() {
        Registry.register(Registry.ENTITY_TYPE, new Identifier(Constants.MOD_ID, Constants.Entities.MOON_VILLAGER), MOON_VILLAGER);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(Constants.MOD_ID, Constants.Entities.T1_ROCKET), T1_ROCKET);
        Registry.register(Registry.ENTITY_TYPE, new Identifier(Constants.MOD_ID, Constants.Entities.ORE_ASTEROID), ORE_ASTEROID);
        Galacticraft.logger.info("Registered entity types!");
    }
}
