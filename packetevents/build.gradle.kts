dependencies {
    // Dependencies
    compileOnly("com.github.retrooper:packetevents-api:2.11.2")
    compileOnly("net.kyori:adventure-api:5.0.0")

    // Libraries
    implementation("com.github.ben-manes.caffeine:caffeine:v3.2.3")

    // Projects
    api(project(":api"))
}

tasks {
    shadowJar {
        relocate("com.github.benmanes.caffeine", "org.lushplugins.nbsminecraft.libs.caffeine")
    }
}