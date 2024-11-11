package org.lushplugins.nbsminecraft.platform.allay.player;

import org.lushplugins.nbsminecraft.platform.allay.AllayPlatform;
import org.lushplugins.nbsminecraft.player.SongPlayer;

public class AllaySongPlayer {

    private AllaySongPlayer() {}

    public static class Builder extends SongPlayer.Builder {

        public Builder() {
            super(AllayPlatform.INSTANCE);
        }
    }
}
