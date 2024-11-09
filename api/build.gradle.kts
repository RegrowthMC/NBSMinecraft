dependencies {
    // Libraries
    api("com.github.koca2000:NBS4j:1.3.0")
}

tasks {
    shadowJar {
        relocate("cz.koca2000.nbs4j", "org.lushplugins.nbsminecraft.libs.nbs4j")
    }
}