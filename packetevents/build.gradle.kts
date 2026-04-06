dependencies {
    // Dependencies
    compileOnly("com.github.retrooper:packetevents-api:2.12.0")
    compileOnly("net.kyori:adventure-api:4.26.1")

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