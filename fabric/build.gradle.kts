plugins {
    id("fabric-loom") version("1.15-SNAPSHOT")
}

dependencies {
    // Dependencies
    minecraft("com.mojang:minecraft:1.21.11")
    mappings("net.fabricmc:yarn:1.21.11+build.4")
    modCompileOnly("net.fabricmc:fabric-loader:0.18.5")
    modCompileOnly("net.fabricmc.fabric-api:fabric-api:0.141.3+1.21.11")

    // Projects
    api(project(":api"))

    // Dependencies to include in jar
    include(project(":api"))
    include("net.raphimc:NoteBlockLib:3.1.1-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    shadowJar {
        enabled = false
    }
}