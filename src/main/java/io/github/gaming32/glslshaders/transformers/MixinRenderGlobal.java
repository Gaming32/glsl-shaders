package io.github.gaming32.glslshaders.transformers;

import io.github.gaming32.glslshaders.Shaders;
import net.minecraft.src.RenderGlobal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(RenderGlobal.class)
public class MixinRenderGlobal {
    @Inject(
        method = "renderSky",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/World;getStarBrightness(F)F",
            shift = At.Shift.AFTER
        )
    )
    private void postGetStarBrightness(float par1) {
        Shaders.setCelestialPosition();
    }
}
