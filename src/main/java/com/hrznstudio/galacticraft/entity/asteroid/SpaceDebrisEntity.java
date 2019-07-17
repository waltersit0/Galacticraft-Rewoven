package com.hrznstudio.galacticraft.entity.asteroid;

import com.hrznstudio.galacticraft.Constants;
import com.hrznstudio.galacticraft.blocks.GalacticraftBlocks;
import com.hrznstudio.galacticraft.container.GalacticraftContainers;
import com.hrznstudio.galacticraft.entity.data.GalacticraftTrackedDataHandlers;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
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
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class SpaceDebrisEntity extends Entity {
    private static final TrackedData<Float> SPIN_PITCH = DataTracker.registerData(SpaceDebrisEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> SPIN_YAW = DataTracker.registerData(SpaceDebrisEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Block> ORE_VARIANT = DataTracker.registerData(SpaceDebrisEntity.class, GalacticraftTrackedDataHandlers.BLOCK);

    public float spinPitch;
    public float spinYaw;
    public Block block;

    private boolean positiveX, positiveZ;

    private int axis; //0 = x, 1 = z

    private boolean firstTick = true;
    private int l = 1;

    public SpaceDebrisEntity(EntityType<SpaceDebrisEntity> entityType, World world_1) {
        super(entityType, world_1);
    }

    @Override
    public void tick() {
        if (this.firstTick) {
            positiveX = this.world.random.nextInt(100) % 2 == 0;
            positiveZ = this.world.random.nextInt(100) % 2 == 0;
            axis = this.world.random.nextInt(2);
            if (axis == 0) {
                if (positiveX) {
                    this.setVelocity(world.random.nextInt(100) % 2 == 0 ? 0.2D : 0.1D, 0.0D, 0.0D);
                } else {
                    this.setVelocity(world.random.nextInt(100) % 2 == 0 ? -0.2D : -0.1D, 0.0D, 0.0D);
                }
            } else if (axis == 1) {
                if (positiveZ) {
                    this.setVelocity(0.0D, 0.0D, world.random.nextInt(100) % 2 == 0 ? 0.2D : 0.1D);
                } else {
                    this.setVelocity(0.0D, 0.0D, world.random.nextInt(100) % 2 == 0 ? -0.2D : -0.1D);
                }
            }
        } else {
            if (axis == 0) {
                if (this.world.random.nextInt(10) <= 4) {
                    this.addVelocity(0.01D, 0.0D, 0.0D);
                }
            } else if (axis == 1) {
                if (this.world.random.nextInt(10) <= 4) {
                    this.addVelocity(0.0D, 0.0D, 0.01D);
                }
            }
            if (axis == 0) {
                if (this.world.random.nextInt(10) <= 4) {
                    this.addVelocity(-0.01D, 0.0D, 0.0D);
                }
            } else if (axis == 1) {
                if (this.world.random.nextInt(10) <= 4) {
                    this.addVelocity(0.0D, 0.0D, -0.01D);
                }
            }
        }

        if (this.getVelocity().x > 0.3D) {
            this.setVelocity(0.3D, this.getVelocity().y, this.getVelocity().z);
        }
        if (this.getVelocity().z > 0.3D) {
            this.setVelocity(this.getVelocity().x, this.getVelocity().y, 0.3D);
        }
        if (this.getVelocity().x < -0.3D) {
            this.setVelocity(-0.3D, this.getVelocity().y, this.getVelocity().z);
        }
        if (this.getVelocity().z < -0.3D) {
            this.setVelocity(this.getVelocity().x, this.getVelocity().y, -0.3D);
        }

        if (l <= 80) {
            this.setVelocity(this.getVelocity().x, 0.02D, this.getVelocity().z);
        } else if (l < 160) {
            this.setVelocity(this.getVelocity().x, -0.02D, this.getVelocity().z);
        } else {
            this.setVelocity(this.getVelocity().x, -0.02D, this.getVelocity().z);
            l = 0;
        }
        l++;

        if (!this.firstTick) {
            if (this.y > 288D || this.y < -32D || this.age > 3000) {
                this.remove();
            }
            if (!this.world.isPlayerInRange(this.getPos().x, this.getPos().y, this.getPos().z, (16 * 5) - 1)) {
                this.remove();
            }
        } else {
            this.firstTick = false;
        }

        super.tick();

        if (block == null) {
            block = GalacticraftBlocks.MOON_TURF;
        }

        if (!this.world.isClient()) {
            this.setSpinPitch(this.spinPitch);
            this.setSpinYaw(this.spinYaw);
            this.setAsteroidType(block);
        }

        pitch += this.world.random.nextFloat() + 1.5F;
        yaw += this.world.random.nextFloat() + 1.5F;

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
        block = type;
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(SPIN_PITCH, spinPitch);
        this.dataTracker.startTracking(SPIN_YAW, spinYaw);
        this.dataTracker.startTracking(ORE_VARIANT, block);
    }

    @Override
    public void readCustomDataFromTag(CompoundTag var1) {
        this.block = Registry.BLOCK.get(new Identifier(var1.getString("block")));
        this.spinPitch = var1.getFloat("spinPitch");
        this.spinYaw = var1.getFloat("spinYaw");
    }

    @Override
    public void writeCustomDataToTag(CompoundTag var1) {
        var1.putString("block", Registry.BLOCK.getId(block).toString());
        var1.putFloat("spinPitch", this.spinPitch);
        var1.putFloat("spinYaw", this.spinYaw);
    }

    public static void registerUseEntityCallback() {
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            System.out.println("A");
            if (entity instanceof SpaceDebrisEntity) {
                ContainerProviderRegistry.INSTANCE.openContainer(GalacticraftContainers.BASIC_SOLAR_PANEL_CONTAINER, player, packetByteBuf -> packetByteBuf.writeBlockPos(entity.getBlockPos()));
                return ActionResult.PASS;
            }
            return ActionResult.PASS;
        });
    }

    @Override
    public Packet<?> createSpawnPacket() {
        return new CustomPayloadS2CPacket(new Identifier(Constants.MOD_ID, "debris_entity_spawn"), new PacketByteBuf(new PacketByteBuf(new PacketByteBuf(Unpooled.buffer()).writeVarInt(Registry.ENTITY_TYPE.getRawId(this.getType())).writeVarInt(this.getEntityId()).writeUuid(this.uuid).writeIdentifier(Registry.BLOCK.getId(this.block)).writeDouble(x).writeDouble(y).writeDouble(z).writeByte((int)(pitch / 360F * 256F))).writeByte((int)(pitch / 360F * 256F))));
    }
}
