package org.lushplugins.nbsminecraft.platform.packetevents.player;

import org.lushplugins.nbsminecraft.platform.packetevents.PacketEventsPlatform;
import org.lushplugins.nbsminecraft.player.SongPlayer;

public class PacketEventsSongPlayer {

    private PacketEventsSongPlayer() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends SongPlayer.Builder {

        /**
         * @see #builder()
         */
        @Deprecated
        public Builder() {
            super(PacketEventsPlatform.INSTANCE);
        }
    }
}
