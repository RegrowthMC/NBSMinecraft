package org.lushplugins.nbsjava.player;

import org.lushplugins.nbsjava.NBSAPI;
import org.lushplugins.nbsjava.platform.AbstractPlatform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SongPlayerManager {
    private final AbstractPlatform platform;
    private final List<SongPlayer> players = new ArrayList<>();
    private final ScheduledFuture<?> heartbeat;

    public SongPlayerManager(AbstractPlatform platform) {
        this.platform = platform;

        float tempo = 10;
        long period = (long) (1000 / tempo);
        heartbeat = NBSAPI.getThreadPool().scheduleAtFixedRate(this::tickSongPlayers, 0, period, TimeUnit.MILLISECONDS);
    }

    public void registerPlayer(SongPlayer player) {
        players.add(player);
    }

    public void unregisterPlayer(SongPlayer player) {
        players.remove(player);
    }

    public void tickSongPlayers() {
        for (SongPlayer songPlayer : Collections.unmodifiableList(players)) {
            if (songPlayer.isShutdown()) {
                unregisterPlayer(songPlayer);
                continue;
            }

            songPlayer.tickSong(platform);
        }
    }

    public void shutdown() {
        heartbeat.cancel(false);
    }
}
