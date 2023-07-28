package io.github.gaming32.glslshaders;

import net.lenni0451.classtransform.TransformerManager;
import net.lenni0451.classtransform.utils.ASMUtils;
import net.minecraft.modding.api.Mod;
import net.minecraft.modding.api.ModInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GlslShaders implements Mod {

    @Override
    public void init(ModInfo modInfo, TransformerManager transformerManager) throws Exception {
        System.out.println("GLSL shaders are fun :)");
        transformerManager.addPostTransformConsumer((s, bytes) -> {
            final Path p = Paths.get("classexport", ASMUtils.slash(s).concat(".class"));
            try {
                Files.createDirectories(p.getParent());
                Files.write(p, bytes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
