package io.github.gaming32.glslshaders.transformers;

import io.github.gaming32.glslshaders.Shaders;
import io.github.gaming32.glslshaders.ext.TesselatorExt;
import net.minecraft.src.RenderBlocks;
import net.minecraft.src.Tessellator;
import net.minecraft.src.WorldRenderer;
import net.minecraft.world.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(WorldRenderer.class)
public class MixinWorldRenderer {
    @Redirect(
        method = "updateRenderer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/src/RenderBlocks;renderBlockByRenderType(Lnet/minecraft/world/block/Block;III)Z"
        )
    )
    private boolean renderBlockByRenderType(RenderBlocks instance, Block par1Block, int par2, int par3, int par4) {
        if (Shaders.entityAttrib >= 0) {
            ((TesselatorExt)Tessellator.instance).setEntity(par1Block.blockID);
        }
        return instance.renderBlockByRenderType(par1Block, par2, par3, par4);
    }

    @Inject(method = "updateRenderer", at = @At("RETURN"))
    private void afterUpdateRenderer() {
        if (Shaders.entityAttrib >= 0) {
            ((TesselatorExt)Tessellator.instance).setEntity(-1);
        }
    }
}
