# NBSMinecraft
NBSMinecraft is an easy to use, asynchronous API for playing `.nbs` music files. This library was made with the intention of offering a lightweight, platformless API to allow the use of note block music on as many Minecraft platforms as possible.

## Supported Platforms
The project is focused on being platformless, so the majority of platforms can be supported. If you'd like to add native support for a platform to the library, feel free to make a pull request.

We current natively support:
- [allay](https://github.com/AllayMC/Allay)
- [bukkit](https://dev.bukkit.org/)
- [packetevents](https://github.com/retrooper/packetevents)

<br>

## Commonly Asked Questions
**Where can I find .nbs files?**
- You can find a list of songs on OpenNBS's website https://noteblock.world/

**Can I use .midi files?**
- You can import .midi files to OpenNBS's [Open Note Block Studio](https://opennbs.org/) and then save them as .nbs files

**What plugins use NBSMinecraft?**
- *If you would like to add your plugin to this list, please open a pull request!*

<br>

## Depending on NBSMinecraft
You can add NBSMinecraft to your project by adding the below into your maven or gradle build file, replace `{platform}` with the platform you would like to target, you can find a list of platforms here.

*Development builds can be found in the https://repo.lushplugins.org/snapshots/ maven repo*

![Version Number](https://repo.lushplugins.org/api/badge/latest/releases/org/lushplugins/pluginupdater/NBSMinecraft-api?color=40c14a&name=Latest)

<details open>
<summary>Maven</summary>

**Repository:**
```xml
<repositories>
    <repository>
        <id>lushplugins.org</id>
        <url>https://repo.lushplugins.org/releases/</url>
    </repository>
</repositories>
```
**Artifact:**
```xml
<dependencies>
    <dependency>
        <groupId>org.lushplugins.nbsminecraft</groupId>
        <artifactId>NBSMinecraft-{platform}</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```
</details>

<details>
<summary>Gradle</summary>

**Repository:**
```gradle
repositories {
    mavenCentral()
    maven { url = "https://repo.lushplugins.org/releases/" }
}
```
**Artifact:**
```gradle
dependencies {
    compileOnly "org.lushplugins.nbsminecraft:NBSMinecraft-{platform}:1.0.0"
}
```
</details>

<br>

## Basic Usage

### Creating a SongPlayer
Each platform has it's own SongPlayer instance that you can create to play songs.
You can make use of different emitters to play songs to different players, these can be tied to locations, played globally or you could even make custom emitters for your specific requirements!

This is an example of how you could make a song player with the bukkit platform that plays a single song on repeat to all online players:

*Note that audio listeners need to be added/removed manually, for a song player playing globally just adding the listener on join and removing them on leave should be suitable!*
```java
SongPlayer player = BukkitSongPlayer.builder()
    .soundEmitter(new GlobalSoundEmitter())
    .queue(song)
    .build();

player.loopQueue(true);
Bukkit.getOnlinePlayers().forEach(onlinePlayer -> player.addListener(new BukkitAudioListener(onlinePlayer)));
player.play();
```

### Getting a Song instance
There are multiple methods in the NBTAPI class that you can use to parse `.nbs` files; `readSongFile(File)`, `readSongInputStream(InputStream)` and `Map<String, Song> readSongsInDirectory(File)`.


<br>

## Getting support
If you need help setting up the API or have any questions, feel free to join the [LushPlugins discord server](https://discord.gg/mbPxvAxP3m)
