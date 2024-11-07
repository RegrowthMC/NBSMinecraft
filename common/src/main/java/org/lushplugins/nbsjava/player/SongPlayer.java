package org.lushplugins.nbsjava.player;

public class SongPlayer {
    private boolean playing = false;

    /**
     * Start/continue playing the current song
     */
    public void play() {
        if (playing) {
            return;
        }

        this.playing = true;


    }

    /**
     * Pause the current song
     */
    public void pause() {}

    /**
     * Stop the player and clear the queue
     */
    public void stop() {}
}
