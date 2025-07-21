package org.lushplugins.nbsminecraft.platform.allay.player;

import org.lushplugins.nbsminecraft.platform.allay.AllayPlatform;
import org.lushplugins.nbsminecraft.player.SongPlayer;

public class AllaySongPlayer {

    private AllaySongPlayer() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends SongPlayer.Builder {

        /**
         * @see #builder()
         */
        @Deprecated
        public Builder() {
            super(AllayPlatform.INSTANCE);
        }
    }
}
