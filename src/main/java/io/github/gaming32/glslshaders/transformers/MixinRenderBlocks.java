package io.github.gaming32.glslshaders.transformers;

import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.world.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@Mixin(RenderBlocks.class)
public class MixinRenderBlocks {
    @Inject(method = "renderBottomFace", at = @At("HEAD"))
    private void beforeRenderBottomFace(Block par1Block, double par2, double par4, double par6, int par8) {
        Tessellator.instance.setNormal(0f, -1f, 0f);
    }

    @Inject(method = "renderTopFace", at = @At("HEAD"))
    private void beforeRenderTopFace(Block par1Block, double par2, double par4, double par6, int par8) {
        Tessellator.instance.setNormal(0f, 1f, 0f);
    }

    @Inject(method = "renderEastFace", at = @At("HEAD"))
    private void beforeRenderEastFace(Block par1Block, double par2, double par4, double par6, int par8) {
        Tessellator.instance.setNormal(0f, 0f, -1f);
    }

    @Inject(method = "renderWestFace", at = @At("HEAD"))
    private void beforeRenderWestFace(Block par1Block, double par2, double par4, double par6, int par8) {
        Tessellator.instance.setNormal(0f, 0f, 1f);
    }

    @Inject(method = "renderNorthFace", at = @At("HEAD"))
    private void beforeRenderNorthFace(Block par1Block, double par2, double par4, double par6, int par8) {
        Tessellator.instance.setNormal(-1f, 0f, 0f);
    }

    @Inject(method = "renderSouthFace", at = @At("HEAD"))
    private void beforeRenderSouthFace(Block par1Block, double par2, double par4, double par6, int par8) {
        Tessellator.instance.setNormal(1f, 0f, 0f);
    }
}
