package org.lushplugins.nbsminecraft.platform.bukkit.test.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.lushplugins.nbsminecraft.platform.bukkit.test.TestPlugin;
import org.lushplugins.nbsminecraft.platform.bukkit.utils.BukkitAudioListener;

public class PlayerListener implements Listener {
    private final TestPlugin plugin;

    public PlayerListener(TestPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.plugin.getSongPlayer().addListener(new BukkitAudioListener(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        this.plugin.getSongPlayer().removeListener(event.getPlayer().getUniqueId());
    }
}
