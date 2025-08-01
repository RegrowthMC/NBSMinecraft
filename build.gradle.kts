plugins {
    `java-library`
    `maven-publish`
    id("com.gradleup.shadow") version("8.3.0")
}

group = "org.lushplugins"
version = "1.0.0-alpha42"

allprojects {
    apply(plugin="java-library")
    apply(plugin="com.gradleup.shadow")

    tasks {
        build {
            dependsOn(shadowJar)
        }

        shadowJar {
            relocate("net.raphimc.noteblocklib", "org.lushplugins.nbsminecraft.libs.noteblocklib")
        }
    }
}

subprojects {
    apply(plugin="java-library")
    apply(plugin="maven-publish")

    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://maven.lenni0451.net/snapshots/") // NoteblockLib
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/") // Spigot
        maven("https://repo.codemc.io/repository/maven-releases/") // PacketEvents
        maven("https://maven.neoforged.net/releases") // NeoForge
        maven("https://repo.opencollab.dev/maven-releases/") // Allay
        maven("https://repo.opencollab.dev/maven-snapshots/") // Allay
        maven("https://storehouse.okaeri.eu/repository/maven-public/") // Allay
        maven("https://jitpack.io/") // Allay
    }

    dependencies {
        // Annotations
        compileOnlyApi("org.jetbrains:annotations:26.0.2")
    }

    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(17))

        registerFeature("optional") {
            usingSourceSet(sourceSets["main"])
        }

        withSourcesJar()
    }

    tasks {
        withType<JavaCompile> {
            options.encoding = "UTF-8"
        }
    }

    publishing {
        repositories {
            maven {
                name = "lushReleases"
                url = uri("https://repo.lushplugins.org/releases")
                credentials(PasswordCredentials::class)
                authentication {
                    isAllowInsecureProtocol = true
                    create<BasicAuthentication>("basic")
                }
            }

            maven {
                name = "lushSnapshots"
                url = uri("https://repo.lushplugins.org/snapshots")
                credentials(PasswordCredentials::class)
                authentication {
                    isAllowInsecureProtocol = true
                    create<BasicAuthentication>("basic")
                }
            }
        }

        publications {
            create<MavenPublication>("maven") {
                groupId = rootProject.group.toString() + ".nbsminecraft"
                artifactId = rootProject.name + "-" + project.name
                version = rootProject.version.toString()
                from(project.components["java"])
            }
        }
    }
}
