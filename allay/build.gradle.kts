dependencies {
    // Dependencies
    compileOnly("org.allaymc.allay:api:0.15.0")

    // Projects
    api(project(":api"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}