import xyz.wagyourtail.unimined.internal.minecraft.MinecraftProvider
import xyz.wagyourtail.unimined.minecraft.patch.prcraft.PrcraftMinecraftTransformer

plugins {
    id("java")
    id("xyz.wagyourtail.unimined")
}

unimined.useGlobalCache = false

group = "io.github.gaming32"
version = "1.0-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_1_8
java.targetCompatibility = JavaVersion.VERSION_1_8

repositories {
    maven("https://libraries.minecraft.net/")
    mavenCentral()
    maven("https://maven.jemnetworks.com/releases")
    maven("https://maven.fabricmc.net")
    maven("https://maven.lenni0451.net/releases")
}

dependencies {
}

val minecraft_version = project.properties["minecraft_version"] as String
val prcraft_version = project.properties["prcraft_version"] as String


unimined.minecraft(sourceSets.main.get()) {
    version(minecraft_version)
    side("client")

    // add our custom transformer
    customPatcher(PrcraftMinecraftTransformer(project, this as MinecraftProvider)) {
        loader(prcraft_version)
    }
}

tasks.processResources {
    inputs.property("version", project.version)
    filesMatching("prcraft.json") {
        expand("version" to project.version)
    }
}

tasks.compileJava {
    if (JavaVersion.current().isJava9Compatible) {
        options.release.set(8)
    }
}
