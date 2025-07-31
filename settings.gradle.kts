rootProject.name = "NBSMinecraft"

include("api")

include("allay")
include("bukkit")
include("bukkit:test")
include("fabric")
include("neoforge")
include("packetevents")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases")
    }
}