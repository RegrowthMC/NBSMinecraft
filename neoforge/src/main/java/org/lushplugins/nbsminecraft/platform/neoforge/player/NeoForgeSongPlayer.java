package org.lushplugins.nbsminecraft.platform.neoforge.player;

import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;
import org.lushplugins.nbsminecraft.platform.neoforge.NeoForgePlatform;
import org.lushplugins.nbsminecraft.player.SongPlayer;

public class NeoForgeSongPlayer {

    private NeoForgeSongPlayer() {}

    @ApiStatus.Experimental
    public static Builder builder(MinecraftServer server) {
        return new Builder(server);
    }

    public static class Builder extends SongPlayer.Builder {

        private Builder(MinecraftServer server) {
            super(new NeoForgePlatform(server));
        }
    }
}
