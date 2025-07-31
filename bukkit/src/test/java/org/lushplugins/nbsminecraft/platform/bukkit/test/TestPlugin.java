package org.lushplugins.nbsminecraft.platform.bukkit.test;

import org.bukkit.plugin.java.JavaPlugin;
import org.lushplugins.nbsminecraft.platform.bukkit.player.BukkitSongPlayer;
import org.lushplugins.nbsminecraft.platform.bukkit.test.command.TestCommand;
import org.lushplugins.nbsminecraft.platform.bukkit.test.listener.PlayerListener;
import org.lushplugins.nbsminecraft.player.SongPlayer;
import org.lushplugins.nbsminecraft.player.emitter.GlobalSoundEmitter;
import revxrsal.commands.bukkit.BukkitLamp;

public class TestPlugin extends JavaPlugin {
    private final SongPlayer songPlayer = BukkitSongPlayer.builder()
        .soundEmitter(new GlobalSoundEmitter())
        .transposeNotes(true)
        .build();

    @Override
    public void onEnable() {
        BukkitLamp.builder(this)
            .build()
            .register(new TestCommand(this));

        this.getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    public SongPlayer getSongPlayer() {
        return songPlayer;
    }
}
