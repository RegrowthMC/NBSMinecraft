plugins {
    id("net.neoforged.gradle.userdev") version("7.0.190")
}

dependencies {
    // Dependencies
    compileOnly("net.neoforged:neoforge:21.8.23")

    // Projects
    api(project(":api"))

    // Dependencies to include in jar
//    include(project(":api"))
//    include("com.github.koca2000:NBS4j:1.3.0")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}