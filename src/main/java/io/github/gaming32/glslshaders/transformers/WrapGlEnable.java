package io.github.gaming32.glslshaders.transformers;

import net.lenni0451.classtransform.annotations.injection.CASM;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.RenderLiving;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({RenderGlobal.class, RenderLiving.class})
public class WrapGlEnable {
    @CASM("*")
    private static void wrapGlEnable(MethodNode node) {
        for (AbstractInsnNode insn : node.instructions) {
            if (!(insn instanceof MethodInsnNode)) continue;
            final MethodInsnNode methodInsn = (MethodInsnNode)insn;
            if (methodInsn.owner.equals("org/lwjgl/opengl/GL11")) {
                if (methodInsn.name.equals("glEnable")) {
                    methodInsn.owner = "io/github/gaming32/glslshaders/Shaders";
                    methodInsn.name = "glEnableWrapper";
                } else if (methodInsn.name.equals("glDisable")) {
                    methodInsn.owner = "io/github/gaming32/glslshaders/Shaders";
                    methodInsn.name = "glDisableWrapper";
                }
            }
        }
    }
}
