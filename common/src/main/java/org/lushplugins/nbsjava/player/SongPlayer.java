package org.lushplugins.nbsjava.player;

import cz.koca2000.nbs4j.Song;
import org.lushplugins.nbsjava.song.Playlist;
import org.lushplugins.nbsjava.song.SongQueue;

public class SongPlayer {
    private Song song = null;
    private final SongQueue queue;
    private boolean playing = false;

    public SongPlayer(Song song) {
        this.queue = new SongQueue(song);
    }

    public SongPlayer(Playlist playlist) {
        this.queue = new SongQueue(playlist);
    }

    public SongPlayer(SongQueue queue) {
        this.queue = queue;
    }

    /**
     * Start/continue playing the current song
     */
    public void play() {
        this.playing = true;
    }

    /**
     * Pause the current song
     */
    public void pause() {
        playing = false;
    }

    /**
     * Stop the player and clear the queue
     */
    public void stop() {
        playing = false;
        queue.clearQueue();
        song = null;
    }

    private void tickSong() {

    }

    private void onSongFinish() {
        song = queue.poll();
    }
}
