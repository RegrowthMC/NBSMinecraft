package org.lushplugins.nbsminecraft.platform.bukkit.player;

import org.lushplugins.nbsminecraft.platform.bukkit.BukkitPlatform;
import org.lushplugins.nbsminecraft.player.SongPlayer;

public class BukkitSongPlayer {

    private BukkitSongPlayer() {}

    public static class Builder extends SongPlayer.Builder {

        public Builder() {
            super(BukkitPlatform.INSTANCE);
        }
    }
}
