plugins {
    id("net.neoforged.gradle.userdev") version("7.1.21")
}

dependencies {
    // Dependencies
    compileOnly("net.neoforged:neoforge:26.1.2.73")

    // Projects
    api(project(":api"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}