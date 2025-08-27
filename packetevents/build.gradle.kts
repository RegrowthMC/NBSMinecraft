dependencies {
    // Dependencies
    compileOnly("com.github.retrooper:packetevents-api:2.9.5")

    // Libraries
    implementation("com.github.ben-manes.caffeine:caffeine:v3.2.2")

    // Projects
    api(project(":api"))
}

tasks {
    shadowJar {
        relocate("com.github.benmanes.caffeine", "org.lushplugins.nbsminecraft.libs.caffeine")
    }
}