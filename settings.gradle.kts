rootProject.name = "NBSMinecraft"

include("api")

include("allay")
include("bukkit")
include("fabric")
include("packetevents")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven("https://maven.fabricmc.net/")
    }
}