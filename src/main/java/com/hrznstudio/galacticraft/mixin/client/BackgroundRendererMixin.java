package com.hrznstudio.galacticraft.mixin.client;

import com.hrznstudio.galacticraft.world.dimension.GalacticraftDimensions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BackgroundRenderer.class)
@Environment(EnvType.CLIENT)
public class BackgroundRendererMixin {
    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/world/ClientWorld;getSkyAngle(F)F", ordinal = 1))
    private static float stopRedSkyGCMoon(ClientWorld clientWorld, float tickDelta) {
        if (clientWorld.getDimension().getType() == GalacticraftDimensions.MOON) {
            return 1.0F;
        } else {
            return clientWorld.getSkyAngle(tickDelta);
        }
    }
}
