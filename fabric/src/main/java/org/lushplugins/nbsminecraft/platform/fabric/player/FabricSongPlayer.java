package org.lushplugins.nbsminecraft.platform.fabric.player;

import net.minecraft.server.MinecraftServer;
import org.lushplugins.nbsminecraft.platform.fabric.FabricPlatform;
import org.lushplugins.nbsminecraft.player.SongPlayer;

public class FabricSongPlayer {

    private FabricSongPlayer() {}

    public static Builder builder(MinecraftServer server) {
        return new Builder(server);
    }

    public static class Builder extends SongPlayer.Builder {

        private Builder(MinecraftServer server) {
            super(new FabricPlatform(server));
        }
    }
}
