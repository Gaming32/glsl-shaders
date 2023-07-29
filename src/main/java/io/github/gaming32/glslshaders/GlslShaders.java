package io.github.gaming32.glslshaders;

import net.lenni0451.classtransform.TransformerManager;
import net.lenni0451.lambdaevents.EventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.modding.api.Mod;
import net.minecraft.modding.api.ModInfo;
import net.minecraft.modding.api.Prcraft;
import net.minecraft.modding.api.event.ClientShutdownEvent;
import net.minecraft.modding.api.event.PhasedEvent;
import net.minecraft.modding.api.event.PostGameInitialize;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class GlslShaders implements Mod {
    private static Path resourceRoot;
    private static Path shaderpackRoot;

    @Override
    public void init(ModInfo modInfo, TransformerManager transformerManager) {
        resourceRoot = modInfo.resourcePath();
        Prcraft.getEventManager().register(this);
        System.out.println("GLSL shaders are fun :)");
    }

    @EventHandler
    public void postInit(PostGameInitialize event) {
        final Path shaderpackPath = Minecraft.getMinecraftDir().toPath().resolve("shaderpack.zip");
        if (Files.isRegularFile(shaderpackPath)) {
            System.out.println("Using shaderpack from " + shaderpackPath);
            try {
                shaderpackRoot = FileSystems.newFileSystem(shaderpackPath, null).getRootDirectories().iterator().next();
            } catch (Exception e) {
                System.err.println("Failed to load custom shaderpack.zip");
                e.printStackTrace();
            }
        } else {
            System.out.println("Custom shaderpack not found at " + shaderpackPath + ". Falling back to default shaders.");
            System.out.println("To use custom shaders, put you shaderpack zip file into " + Minecraft.getMinecraftDir() + " and rename it to shaderpack.zip.");
        }
    }

    @EventHandler
    public void shutdown(ClientShutdownEvent event) {
        if (event.getPhase() != PhasedEvent.EventPhase.BEFORE) return;
        if (shaderpackRoot != null) {
            try {
                shaderpackRoot.getFileSystem().close();
            } catch (IOException e) {
                System.err.println("Failed to close custom shaderpack.zip");
                e.printStackTrace();
            }
        }
    }

    public static Path getShaderPath(String filename) {
        if (shaderpackRoot != null) {
            final Path tryPath = shaderpackRoot.resolve(filename);
            if (Files.exists(tryPath)) {
                return tryPath;
            }
        }
        return resourceRoot.resolve(filename);
    }

}
