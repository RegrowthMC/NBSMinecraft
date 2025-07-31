package org.lushplugins.nbsminecraft.platform.bukkit.test.command;

import net.raphimc.noteblocklib.model.Song;
import org.lushplugins.nbsminecraft.NBSAPI;
import org.lushplugins.nbsminecraft.platform.bukkit.test.TestPlugin;
import revxrsal.commands.annotation.Command;

@SuppressWarnings("unused")
public class TestCommand {
    private final TestPlugin plugin;

    public TestCommand(TestPlugin plugin) {
        this.plugin = plugin;
    }

    @Command("nbstest")
    public void nbstest(String songName) {
        songName = songName.contains(".nbs") ? songName : songName + ".nbs";
        Song song = NBSAPI.INSTANCE.readSongInputStream(this.plugin.getResource("songs/" + songName));
        this.plugin.getSongPlayer().queueSong(song);
    }
}
