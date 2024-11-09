package org.lushplugins.nbsminecraft;

import cz.koca2000.nbs4j.Song;
import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.player.SongPlayer;
import org.lushplugins.nbsminecraft.player.SongPlayerManager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class NBSAPI {
    private static final ScheduledExecutorService threads = Executors.newScheduledThreadPool(1);
    private final SongPlayerManager songPlayerManager;

    public NBSAPI(AbstractPlatform platform) {
        this.songPlayerManager = new SongPlayerManager(platform);
    }

    /**
     * Register a song player to the manager
     * @param songPlayer song player to register
     */
    public void registerPlayer(SongPlayer songPlayer) {
        songPlayerManager.registerPlayer(songPlayer);
    }

    public void shutdown() {
        songPlayerManager.shutdown();
    }

    /**
     * Read a song from a file
     * @param file file to read
     * @return parsed song
     */
    public Song readSongFile(File file) {
        try {
            return Song.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Read a song from an input stream
     * @param inputStream input stream to read
     * @return parsed song
     */
    public Song readSongInputStream(InputStream inputStream) {
        return Song.fromStream(inputStream);
    }

    public static ScheduledExecutorService getThreadPool() {
        return threads;
    }
}
