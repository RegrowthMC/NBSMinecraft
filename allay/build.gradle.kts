dependencies {
    // Dependencies
    compileOnly("org.allaymc.allay:api:f7bb44f7aa")

    // Projects
    api(project(":api"))
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}