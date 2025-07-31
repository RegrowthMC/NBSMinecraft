plugins {
    id("xyz.jpenilla.run-paper") version("2.2.4")
}

dependencies {
    // Dependencies
    compileOnly("org.spigotmc:spigot-api:1.21.8-R0.1-SNAPSHOT")
    testCompileOnly("org.spigotmc:spigot-api:1.21.8-R0.1-SNAPSHOT")

    // Projects
    api(project(":api"))

    // Libraries
    testImplementation("io.github.revxrsal:lamp.common:4.0.0-rc.12")
    testImplementation("io.github.revxrsal:lamp.bukkit:4.0.0-rc.12")
}

tasks {
    processResources{
        filesMatching("plugin.yml") {
            expand(project.properties)
        }

        inputs.property("version", rootProject.version)
        filesMatching("plugin.yml") {
            expand("version" to rootProject.version)
        }
    }

    runServer {
        minecraftVersion("1.21.8")
    }
}

tasks.withType(xyz.jpenilla.runtask.task.AbstractRun::class) {
    javaLauncher = javaToolchains.launcherFor {
        languageVersion = JavaLanguageVersion.of(21)
    }
}