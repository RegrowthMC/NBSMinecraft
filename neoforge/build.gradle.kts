plugins {
    id("net.neoforged.gradle.userdev") version("7.0.190")
}

dependencies {
    // Dependencies
    compileOnly("net.neoforged:neoforge:21.8.46")

    // Projects
    api(project(":api"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}