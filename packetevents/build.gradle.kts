dependencies {
    // Dependencies
    compileOnly("com.github.retrooper:packetevents-api:2.6.0")

    // Libraries
    implementation("com.github.ben-manes.caffeine:caffeine:v3.2.2")

    // Projects
    api(project(":api"))
}