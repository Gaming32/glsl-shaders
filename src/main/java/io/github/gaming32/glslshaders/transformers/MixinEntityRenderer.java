package io.github.gaming32.glslshaders.transformers;

import io.github.gaming32.glslshaders.Shaders;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityRenderer;
import net.minecraft.src.RenderGlobal;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderer.class)
public class MixinEntityRenderer {
    @Shadow private Minecraft mc;

    @Shadow private float fogColorRed;

    @Shadow private float fogColorGreen;

    @Shadow private float fogColorBlue;

    @Inject(method = "renderWorld", at = @At("HEAD"))
    private void beforeRenderWorld(float tickDelta, long par2) {
        Shaders.beginRender(mc, tickDelta, par2);
    }

    @Inject(method = "renderWorld", at = @At("TAIL"))
    private void afterRenderWorld(float tickDelta, long par2) {
        Shaders.endRender();
    }

    @Inject(
        method = "renderWorld",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/EntityRenderer;setupCameraTransform(FI)V"
        )
    )
    private void preSetupCameraTransform(float tickDelta, long par2) {
        Shaders.setClearColor(fogColorRed, fogColorGreen, fogColorBlue);
    }

    @Inject(
        method = "renderWorld",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/EntityRenderer;setupCameraTransform(FI)V",
            shift = At.Shift.AFTER
        )
    )
    private void postSetupCameraTransform(float tickDelta, long par2) {
        Shaders.setCamera(tickDelta);
    }

    @Redirect(
        method = "renderWorld",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/RenderGlobal;sortAndRender(Lnet/minecraft/world/entity/LivingEntity;ID)I"
        )
    )
    private int sortAndRender(RenderGlobal instance, LivingEntity par1LivingEntity, int par2, double par3) {
        final int result;
        if (par2 == 0) {
            Shaders.beginTerrain();
            result = instance.sortAndRender(par1LivingEntity, par2, par3);
            Shaders.endTerrain();
        } else if (par2 == 1) {
            Shaders.beginWater();
            result = instance.sortAndRender(par1LivingEntity, par2, par3);
            Shaders.endTerrain();
        } else {
            result = instance.sortAndRender(par1LivingEntity, par2, par3);
        }
        return result;
    }

    @Inject(
        method = "renderWorld",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/RenderGlobal;renderAllRenderLists(ID)V"
        )
    )
    private void preRenderAllRenderLists(float tickDelta, long par2) {
        Shaders.beginWater();
    }

    @Inject(
        method = "renderWorld",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/RenderGlobal;renderAllRenderLists(ID)V",
            shift = At.Shift.AFTER
        )
    )
    private void postRenderAllRenderLists(float tickDelta, long par2) {
        Shaders.endWater();
    }

    @Inject(
        method = "renderWorld",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/EntityRenderer;renderRainSnow(F)V"
        )
    )
    private void preRenderRainSnow(float tickDelta, long par2) {
        Shaders.beginWeather();
    }

    @Inject(
        method = "renderWorld",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/EntityRenderer;renderRainSnow(F)V",
            shift = At.Shift.AFTER
        )
    )
    private void postRenderRainSnow(float tickDelta, long par2) {
        Shaders.endWeather();
    }

    @Inject(
        method = "renderWorld",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/EntityRenderer;renderHand(FI)V"
        )
    )
    private void preRenderHand(float tickDelta, long par2) {
        Shaders.beginHand();
    }

    @Inject(
        method = "renderWorld",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/EntityRenderer;renderHand(FI)V",
            shift = At.Shift.AFTER
        )
    )
    private void postRenderHand(float tickDelta, long par2) {
        Shaders.endHand();
    }

    @Inject(method = "enableLightmap", at = @At("TAIL"))
    private void afterEnableLightmap(double par1) {
        Shaders.enableLightmap();
    }

    @Inject(method = "disableLightmap", at = @At("TAIL"))
    private void afterDisableLightmap(double par1) {
        Shaders.disableLightmap();
    }
}
