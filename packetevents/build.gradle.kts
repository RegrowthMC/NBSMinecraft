dependencies {
    // Dependencies
    compileOnly("com.github.retrooper:packetevents-api:2.9.4")

    // Libraries
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

    // Projects
    api(project(":api"))
}