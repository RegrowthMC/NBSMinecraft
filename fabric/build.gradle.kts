plugins {
    id("fabric-loom") version("1.11-SNAPSHOT")
}

dependencies {
    // Dependencies
    minecraft("com.mojang:minecraft:1.21.8")
    mappings("net.fabricmc:yarn:1.21.8+build.1:v2")
    modCompileOnly("net.fabricmc:fabric-loader:0.16.14")
    modCompileOnly("net.fabricmc.fabric-api:fabric-api:0.129.0+1.21.8")

    // Projects
    api(project(":api"))

    // Dependencies to include in jar
    include(project(":api"))
    include("net.raphimc:NoteBlockLib:3.2.0-SNAPSHOT")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    shadowJar {
        enabled = false
    }
}