package com.hrznstudio.galacticraft.entity.asteroid;

import com.hrznstudio.galacticraft.Constants;
import com.hrznstudio.galacticraft.entity.data.GalacticraftTrackedDataHandlers;
import io.netty.buffer.Unpooled;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.client.network.packet.CustomPayloadS2CPacket;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class OreAsteroidEntity extends Entity {
    private static final TrackedData<Float> SPIN_PITCH = DataTracker.registerData(OreAsteroidEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> SPIN_YAW = DataTracker.registerData(OreAsteroidEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Block> ORE_VARIANT = DataTracker.registerData(OreAsteroidEntity.class, GalacticraftTrackedDataHandlers.BLOCK);

    public static Set<OreAsteroidEntity> asteroids = new HashSet<>();

    public float spinPitch;
    public float spinYaw;
    public Block block;

    public final boolean positiveX;
    public final boolean positiveZ;

    private byte exploded = 0;

    private boolean firstTick = false;

    public int bounceTime = 0;

    public OreAsteroidEntity(EntityType<OreAsteroidEntity> entityType, World world_1) {
        super(entityType, world_1);
        asteroids.add(this);
        positiveX = this.world.random.nextInt(1) == 1;
        positiveZ = this.world.random.nextInt(1) == 1;
    }

    @Override
    public void tick() {
        if (!this.firstTick) {
            if (this.y > 288D || this.y < -32D || this.age > 6000) {
                //this.remove();
            }
        } else {
            yaw = this.world.random.nextInt(360) - 180;
            pitch = this.world.random.nextInt(90);

            firstTick = false;
        }

        super.tick();

        if (!(this.world.getBlockState(this.getBlockPos().down()).getBlock() instanceof AirBlock) || !(this.world.getBlockState(this.getBlockPos().north()).getBlock() instanceof AirBlock) ||
                !(this.world.getBlockState(this.getBlockPos().east()).getBlock() instanceof AirBlock) || !(this.world.getBlockState(this.getBlockPos().west()).getBlock() instanceof AirBlock ||
                !(this.world.getBlockState(this.getBlockPos().south()).getBlock() instanceof AirBlock)) || !(this.world.getBlockState(this.getBlockPos().up()).getBlock() instanceof AirBlock) ||
                !(this.world.getBlockState(this.getBlockPos()).getBlock() instanceof AirBlock)) {
            if (exploded == 0) {
                world.createExplosion(this, this.getPos().x, this.getPos().y, this.getPos().z, 5.0F, false, Explosion.DestructionType.DESTROY);
                exploded = 1;
            } else if (exploded == 1){
                world.createExplosion(this, this.getPos().x, this.getPos().y, this.getPos().z, 5.0F, false, Explosion.DestructionType.NONE);
                exploded = 2;
            } else {
                if (this.getAsteroidType() != null) {
                    world.setBlockState(getBlockPos(), this.getAsteroidType().getDefaultState());
                }
                remove();
            }
        }

        if (!this.world.isClient()) {
            this.setSpinPitch(this.spinPitch);
            this.setSpinYaw(this.spinYaw);
            this.setAsteroidType(this.block);
        }

        this.yaw += 2.0F;
        this.pitch += 2.0F;

        if (bounceTime <= 0) {
            if (this.world.random.nextInt(3) <= 2) {
                this.addVelocity(0.0D, -0.02D, 0.0D);
            }
        } else {
            bounceTime--;
            this.addVelocity(0.0D, -0.003D, 0.0D);
        }
        if (this.world.random.nextInt(1) == 1) {
            this.addVelocity(positiveX ? 0.01D : -0.01D, 0.0D, 0.0D);
        }
        if (this.world.random.nextInt(1) == 1) {
            this.addVelocity(0.0D, 0.0D, positiveZ ? 0.01D : -0.01D);
        }

        this.move(MovementType.SELF, this.getVelocity());
    }

    public float getSpinPitch() {
        return this.dataTracker.get(SPIN_PITCH);
    }

    public void setSpinPitch(float pitch) {
        this.dataTracker.set(SPIN_PITCH, pitch);
    }

    public float getSpinYaw() {
        return this.dataTracker.get(SPIN_YAW);
    }

    public void setSpinYaw(float yaw) {
        this.dataTracker.set(SPIN_YAW, yaw);
    }

    public Block getAsteroidType() {
        return this.dataTracker.get(ORE_VARIANT);
    }

    public void setAsteroidType(Block type) {
        this.dataTracker.set(ORE_VARIANT, type);
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(SPIN_PITCH, spinPitch);
        this.dataTracker.startTracking(SPIN_YAW, spinYaw);
        this.dataTracker.startTracking(ORE_VARIANT, block);
    }

    @Override
    protected void readCustomDataFromTag(CompoundTag var1) {
        this.block = Registry.BLOCK.get(new Identifier(var1.getString("block")));
        this.spinPitch = var1.getFloat("spinPitch");
        this.spinYaw = var1.getFloat("spinYaw");
    }

    @Override
    protected void writeCustomDataToTag(CompoundTag var1) {
        var1.putString("block", Registry.BLOCK.getId(block).toString());
        var1.putFloat("spinPitch", this.spinPitch);
        var1.putFloat("spinYaw", this.spinYaw);
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new CustomPayloadS2CPacket(new Identifier(Constants.MOD_ID, "asteroid_entity_spawn"), new PacketByteBuf(new PacketByteBuf(new PacketByteBuf(Unpooled.buffer()).writeVarInt(Registry.ENTITY_TYPE.getRawId(this.getType())).writeVarInt(this.getEntityId()).writeUuid(this.uuid).writeIdentifier(Registry.BLOCK.getId(this.block)).writeDouble(x).writeDouble(y).writeDouble(z).writeByte((int)(pitch / 360F * 256F))).writeByte((int)(pitch / 360F * 256F))));
    }
}
