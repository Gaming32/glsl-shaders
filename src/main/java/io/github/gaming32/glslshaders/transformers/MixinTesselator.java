package io.github.gaming32.glslshaders.transformers;

import io.github.gaming32.glslshaders.Shaders;
import io.github.gaming32.glslshaders.ext.TesselatorExt;
import net.minecraft.src.GLAllocation;
import net.minecraft.src.Tessellator;
import org.lwjgl.opengl.ARBVertexProgram;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

@Mixin(Tessellator.class)
public class MixinTesselator implements TesselatorExt {
    @Shadow private int drawMode;
    @Shadow private static boolean convertQuadsToTriangles;
    @Shadow private int addedVertices;
    @Shadow private boolean hasNormals;
    @Shadow private int[] rawBuffer;
    @Shadow private int rawBufferIndex;
    private ByteBuffer shadersBuffer;
    private ShortBuffer shadersShortBuffer;
    private short[] shadersData = new short[] {-1, 0};

    @Override
    public void setEntity(int id) {
        shadersData[0] = (short)id;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void afterInit(int par1) {
        shadersBuffer = GLAllocation.createDirectByteBuffer(par1 / 8 * 4);
        shadersShortBuffer = shadersBuffer.asShortBuffer();
    }

    @Redirect(
        method = "draw",
        at = @At(
            value = "INVOKE",
            target = "Lorg/lwjgl/opengl/GL11;glDrawArrays(III)V"
        )
    )
    private void glDrawArrays(int mode, int first, int count) {
        if (Shaders.entityAttrib >= 0) {
            ARBVertexProgram.glEnableVertexAttribArrayARB(Shaders.entityAttrib);
            ARBVertexProgram.glVertexAttribPointerARB(Shaders.entityAttrib, 2, false, false, 4, (ShortBuffer)shadersShortBuffer.position(0));
        }
        GL11.glDrawArrays(mode, first, count);
        if (Shaders.entityAttrib >= 0) {
            ARBVertexProgram.glDisableVertexAttribArrayARB(Shaders.entityAttrib);
        }
    }

    @Inject(method = "reset", at = @At("HEAD"))
    private void beforeReset() {
        shadersBuffer.clear();
    }

    @Inject(method = "addVertex", at = @At("HEAD"))
    private void beforeAddVertex(double par1, double par3, double par5) {
        if (drawMode == 7 && convertQuadsToTriangles && (addedVertices + 1) % 4 == 0 && hasNormals) {
            rawBuffer[rawBufferIndex + 6] = rawBuffer[(rawBufferIndex - 24) + 6];
            shadersBuffer.putShort(shadersData[0]).putShort(shadersData[1]);
            rawBuffer[rawBufferIndex + 8 + 6] = rawBuffer[(rawBufferIndex + 8 - 16) + 6];
        }
        shadersBuffer.putShort(shadersData[0]).putShort(shadersData[1]);
    }
}
