package com.hrznstudio.galacticraft.blocks.machines.forcefield;

import alexiil.mc.lib.attributes.item.filter.ItemFilter;
import com.hrznstudio.galacticraft.api.block.entity.ConfigurableElectricMachineBlockEntity;
import com.hrznstudio.galacticraft.entity.GalacticraftBlockEntities;
import com.hrznstudio.galacticraft.entity.asteroid.OreAsteroidEntity;
import com.hrznstudio.galacticraft.items.BatteryItem;
import com.hrznstudio.galacticraft.items.GalacticraftItems;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Tickable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
public class ForceFieldBlockEntity extends ConfigurableElectricMachineBlockEntity implements Tickable {

    private static final Item[] mandatoryMaterials = new Item[]{Items.DIAMOND, GalacticraftItems.RAW_SILICON, GalacticraftItems.RAW_SILICON, Items.REDSTONE};

    public ForceFieldStatus status = ForceFieldStatus.INSUFFICIENT_POWER;

    List<OreAsteroidEntity> asteroidsNear = new ArrayList<>();

    public ForceFieldBlockEntity() {
        super(GalacticraftBlockEntities.FORCE_FIELD_TYPE);
    }

    @Override
    protected int getInvSize() {
        return 1;
    }

    @Override
    protected ItemFilter getFilterForSlot(int slot) {
        return slot == 0 ? stack -> stack.getItem() instanceof BatteryItem : null;
    }

    @Override
    public void tick() {
        if (world.isClient || !active()) {
            return;
        }
        asteroidsNear.clear();
        attemptChargeFromStack(0);

        if (this.energy.getCurrentEnergy() <= -100000) { //TODO make a balanced amount of power usage
            status = ForceFieldStatus.INSUFFICIENT_POWER;
        } else {
            status = ForceFieldStatus.PROTECTED;
        }

        if (status == ForceFieldStatus.INSUFFICIENT_POWER) {
            return;
        }

        System.out.println("AHHH");

        if (OreAsteroidEntity.asteroids.size() > 0) {
            Iterator<OreAsteroidEntity> var9 = OreAsteroidEntity.asteroids.iterator();
            do {
                OreAsteroidEntity asteroid = var9.next();
                if (asteroid.squaredDistanceTo(pos.getX(), pos.getY(), pos.getZ()) >= 8 * 8 && asteroid.bounceTime <= 0) {
                    asteroidsNear.add(asteroid);
                }
            } while (var9.hasNext());
        }
        for (OreAsteroidEntity asteroid : asteroidsNear) {
            int r = world.random.nextInt(9);
            if (r % 2 == 0) {
                asteroid.addVelocity(world.random.nextDouble() * 13.0D * (asteroid.positiveZ ? 1.0D : -1.0D), 13.0D, world.random.nextDouble() * (asteroid.positiveX ? 1.0D : -1.0D));
            } else {
                asteroid.addVelocity(world.random.nextDouble() * (asteroid.positiveZ ? 1.0D : -1.0D), 13.0D, world.random.nextDouble() * 13 * (asteroid.positiveX ? 1.0D : -1.0D));
            }
            asteroid.bounceTime = 50;
        }

        //this.energy.extractEnergy(GalacticraftEnergy.GALACTICRAFT_JOULES, 2, Simulation.ACTION);
    }
}