package org.lushplugins.nbsminecraft.player;

import cz.koca2000.nbs4j.Layer;
import cz.koca2000.nbs4j.Note;
import cz.koca2000.nbs4j.Song;
import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.Instruments;
import org.lushplugins.nbsminecraft.song.Playlist;
import org.lushplugins.nbsminecraft.song.SongQueue;
import org.lushplugins.nbsminecraft.utils.SoundCategory;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

public class SongPlayer {
    private final SongQueue queue;
    private final HashMap<UUID, AudioListener> listeners = new HashMap<>();
    private final SoundCategory soundCategory;
    private int volume;
    private boolean shutdown = false;

    private Song song = null;
    private boolean playing = false;
    private int songTick = 0;
    private long songStartTime = -1;

    public SongPlayer(SongQueue queue, SoundCategory soundCategory) {
        this.queue = queue;
        this.soundCategory = soundCategory;
    }

    public SongPlayer(SongQueue queue) {
        this(queue, SoundCategory.RECORDS);
    }

    public SongPlayer(Song song) {
        this(new SongQueue(song));
    }

    public SongPlayer(Playlist playlist) {
        this(new SongQueue(playlist));
    }

    /**
     * @return player's song queue
     */
    public SongQueue getQueue() {
        return queue;
    }

    /**
     * Add an audio listener
     * @param listener audio listener
     */
    public void addListener(AudioListener listener) {
        listeners.put(listener.uuid(), listener);
    }

    /**
     * Remove listener
     * @param uuid listener's uuid
     */
    public void removeListener(UUID uuid) {
        listeners.remove(uuid);
    }

    /**
     * Set the player's volume
     * @param volume volume
     */
    public void setVolume(int volume) {
        this.volume = volume;
    }

    /**
     * @return time in seconds since the start of the current song
     */
    public long getSongPlaytime() {
        return Instant.now().getEpochSecond() - songStartTime;
    }

    /**
     * @return total duration of current song in seconds
     */
    public long getSongDuration() {
        return (long) song.getSongLengthInSeconds();
    }

    /**
     * @return whether the player is marked as playing (A player can still be playing
     * even if no song is currently playing)
     */
    public boolean isPlaying() {
        return this.playing;
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
        songTick = 0;
    }

    /**
     * Skip to the next queue song
     */
    public void skip() {
        playSong(queue.poll());
    }

    /**
     * @return whether the player is shutdown
     */
    public boolean isShutdown() {
        return shutdown;
    }

    /**
     * Shutdown the player, after running this method the player will no longer tick
     */
    public void shutdown() {
        stop();
        shutdown = true;
    }

    /**
     * Immediately plays a song, this will stop the current song
     * @param song song to play
     */
    public void playSong(Song song) {
        this.song = song;
        this.songTick = 0;
        this.songStartTime = Instant.now().getEpochSecond();
    }

    /**
     * Tick the current song
     */
    public void tickSong(AbstractPlatform platform) {
        if (song == null) {
            return;
        }

        for (Layer layer : song.getLayers()) {
            Note note = layer.getNote(songTick);
            if (note == null) {
                continue;
            }

            String sound;
            if (note.isCustomInstrument()) {
                sound = song.getCustomInstrument(note.getInstrument()).getName();
            } else {
                sound = Instruments.getSound(note.getInstrument());
            }

            float volume = (layer.getVolume() * this.volume * note.getVolume()) / 1_000_000F;
            float pitch = note.getPitch() / 100f;

            for (AudioListener listener : listeners.values()) {
                platform.playSound(listener, sound, soundCategory, volume, pitch);
            }
        }

        songTick++;

        if (song.getSongLength() < songTick) {
            onSongFinish();
        }
    }

    private void onSongFinish() {
        playSong(queue.poll());
    }
}
