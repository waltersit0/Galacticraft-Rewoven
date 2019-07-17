package com.hrznstudio.galacticraft.client.render.entity;

import com.hrznstudio.galacticraft.entity.asteroid.OreAsteroidEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class RenderOreAsteroid extends EntityRenderer<OreAsteroidEntity>
{
    public RenderOreAsteroid(EntityRenderDispatcher entityRenderDispatcher_1) {
        super(entityRenderDispatcher_1);
    }

    @Override
    public void render(OreAsteroidEntity asteroid, double x, double y, double z, float f, float partialTickTime) {
        GlStateManager.disableRescaleNormal();

        GlStateManager.pushMatrix();
        GlStateManager.translatef((float) x, (float) y + 0.5F, (float) z);
        GlStateManager.rotatef(asteroid.pitch, 1, 0, 0);
        GlStateManager.rotatef(asteroid.yaw, 0, 1, 0);

        try {
            MinecraftClient.getInstance().getBlockRenderManager().renderDynamic(asteroid.getAsteroidType().getDefaultState(), 1.0F);
        } catch (NullPointerException ignore) {}

        GlStateManager.popMatrix();

        super.render(asteroid, x, y, z, f, partialTickTime);
    }

    @Override
    protected Identifier getTexture(OreAsteroidEntity var1) {
        return null;
    }
}
