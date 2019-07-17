package com.hrznstudio.galacticraft.mixin;

import com.hrznstudio.galacticraft.api.space.CelestialBody;
import com.hrznstudio.galacticraft.api.wire.WireNetwork;
import com.hrznstudio.galacticraft.entity.GalacticraftEntityTypes;
import com.hrznstudio.galacticraft.entity.asteroid.OreAsteroidEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * @author <a href="https://github.com/StellarHorizons">StellarHorizons</a>
 */
@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {

    private boolean hasRunOnceForWorldReload = false;

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(BooleanSupplier booleanSupplier_1, CallbackInfo ci) {
        if (!hasRunOnceForWorldReload) {
            hasRunOnceForWorldReload = true;
            WireNetwork.blockPlaced(); //Runs at the end of tick() - BE's should've ticked, meaning there are wires in the map... right?
        }
        WireNetwork.networkMap.forEach((wireNetwork, blockPos) -> wireNetwork.update());
    }

    private int tick = 0;
    @Inject(method = "tick", at = @At("RETURN"))
    private void spawnAsteroid(BooleanSupplier booleanSupplier_1, CallbackInfo ci) {
        if (tick++ >= 100) {
            tick = 0;
            Consumer<PlayerEntity> action = playerEntity -> {
                if (((ServerWorld)(Object)this).dimension instanceof CelestialBody) {
                    System.out.println("e");
                    OreAsteroidEntity entity = new OreAsteroidEntity(GalacticraftEntityTypes.ORE_ASTEROID, ((ServerWorld)(Object)this));
                    Block block = ((CelestialBody)((ServerWorld)(Object)this).dimension).getOreAsteroidBlocks()[((ServerWorld)(Object)this).random.nextInt(((CelestialBody)((ServerWorld)(Object)this).dimension).getOreAsteroidBlocks().length - 1)];
                    entity.setAsteroidType(block);
                    entity.block = block;
                    int x = playerEntity.getPos().x + ((ServerWorld) (Object) this).random.nextInt(100) % 2 == 0 ? ((ServerWorld) (Object) this).random.nextInt(80) : ((ServerWorld) (Object) this).random.nextInt(80) - 80;
                    int z = playerEntity.getPos().z + ((ServerWorld) (Object) this).random.nextInt(100) % 2 == 0 ? ((ServerWorld) (Object) this).random.nextInt(80) : ((ServerWorld) (Object) this).random.nextInt(80) - 80;
                    entity.x = x;
                    entity.z = z;
                    entity.y = 255;
                    entity.method_18003(x, 255, z);
                    entity.prevX = x;
                    entity.prevY = 255;
                    entity.prevZ = z;
                    System.out.println("s");
                    ((ServerWorld)(Object)this).spawnEntity(entity);
                }
            };
            for (PlayerEntity playerEntity1 : ((ServerWorld)(Object)this).getPlayers()) {
                action.accept(playerEntity1);
            }
        }
    }

    public void close() {
        WireNetwork.networkMap.clear();
        hasRunOnceForWorldReload = false;
    }
}