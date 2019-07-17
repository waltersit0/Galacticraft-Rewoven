package com.hrznstudio.galacticraft.client.render.entity;

import com.hrznstudio.galacticraft.Galacticraft;
import com.hrznstudio.galacticraft.blocks.GalacticraftBlocks;
import com.hrznstudio.galacticraft.entity.asteroid.SpaceDebrisEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.lang3.ObjectUtils;

public class RenderSpaceDebris extends EntityRenderer<SpaceDebrisEntity>
{
    public RenderSpaceDebris(EntityRenderDispatcher entityRenderDispatcher_1) {
        super(entityRenderDispatcher_1);
    }

    @Override
    public void render(SpaceDebrisEntity debris, double x, double y, double z, float f, float partialTickTime) {
        GlStateManager.disableRescaleNormal();

        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) x, (float) y + 0.5F, (float) z);
        GlStateManager.rotatef(debris.pitch, 1, 0, 0);
        GlStateManager.rotatef(debris.yaw, 0, 1, 0);

        try {
            MinecraftClient.getInstance().getBlockRenderManager().renderDynamic(debris.getAsteroidType().getDefaultState(), 1.0F);
        } catch (NullPointerException e) {
            Galacticraft.logger.fatal(e);
            Galacticraft.logger.fatal("FAILED TO RENDER DEBRIS! Block: " + Registry.BLOCK.getId(debris.block));
            try {
                MinecraftClient.getInstance().getBlockRenderManager().renderDynamic(GalacticraftBlocks.MOON_TURF.getDefaultState(), 1.0F);
            } catch (NullPointerException ignore) {}
        }
        GlStateManager.popMatrix();

        super.render(debris, x, y, z, f, partialTickTime);
    }

    @Override
    protected Identifier getTexture(SpaceDebrisEntity var1) {
        return null;
    }
}
