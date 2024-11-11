package org.lushplugins.nbsminecraft.player;

import cz.koca2000.nbs4j.Layer;
import cz.koca2000.nbs4j.Note;
import cz.koca2000.nbs4j.Song;
import org.lushplugins.nbsminecraft.NBSAPI;
import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.Instruments;
import org.lushplugins.nbsminecraft.song.Playlist;
import org.lushplugins.nbsminecraft.song.SongQueue;
import org.lushplugins.nbsminecraft.utils.PitchUtils;
import org.lushplugins.nbsminecraft.utils.SoundCategory;

import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class SongPlayer {
    private final AbstractPlatform platform;
    private final SongQueue queue;
    private final HashMap<UUID, AudioListener> listeners = new HashMap<>();
    private final SoundCategory soundCategory;
    private int volume;
    private final boolean transposeNotes;

    private Song song = null;
    private boolean playing = true;
    private int songTick = 0;
    private long songStartTime = -1;

    private SongPlayer(AbstractPlatform platform, SongQueue queue, SoundCategory soundCategory, int volume, boolean transposeNotes) {
        this.platform = platform;
        this.queue = queue;
        this.soundCategory = soundCategory;
        this.volume = volume;
        this.transposeNotes = transposeNotes;
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
        return songStartTime != -1 ? Instant.now().getEpochSecond() - songStartTime : -1;
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
        tickSong();
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
     * Immediately plays a song, this will stop the current song
     * @param song song to play
     */
    public void playSong(Song song) {
        if (song != null) {
            this.song = song;
            this.songTick = 0;
            this.songStartTime = Instant.now().getEpochSecond();
        }
    }

    /**
     * Tick the current song
     */
    public void tickSong() {
        if (!isPlaying()) {
            return;
        }

        float tempo = song != null ? song.getTempo(songTick + 1) : 10;
        long period = (long) (1000 / tempo);
        NBSAPI.INSTANCE.getThreadPool().schedule(this::tickSong, period, TimeUnit.MILLISECONDS);

        if (song == null) {
            playSong(queue.poll());
            return;
        }

        if (!listeners.isEmpty() && this.volume > 0) {
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
                float pitch;
                if (transposeNotes) {
                    pitch = PitchUtils.getTransposedPitch(note);
                } else {
                    sound = PitchUtils.addOctaveSuffix(sound, note.getKey());
                    pitch = PitchUtils.getPitchInOctave(note);
                }

                for (AudioListener listener : listeners.values()) {
                    platform.playSound(listener, sound, soundCategory, volume, pitch);
                }
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

    public static class Builder {
        private final AbstractPlatform platform;
        private SongQueue queue = new SongQueue();
        private SoundCategory soundCategory = SoundCategory.RECORDS;
        private int volume = 100;
        private boolean transposeNotes = true;

        public Builder(AbstractPlatform platform) {
            this.platform = platform;
        }

        public Builder setQueue(SongQueue queue) {
            this.queue = queue;
            return this;
        }

        public Builder queue(Song song) {
            this.queue.queueSong(song);
            return this;
        }

        public Builder queue(Playlist playlist) {
            this.queue.queuePlaylist(playlist);
            return this;
        }

        public Builder soundCategory(SoundCategory soundCategory) {
            this.soundCategory = soundCategory;
            return this;
        }

        public Builder volume(int volume) {
            this.volume = volume;
            return this;
        }

        public Builder transposeNotes(boolean transposeNotes) {
            this.transposeNotes = transposeNotes;
            return this;
        }

        public SongPlayer build() {
            return new SongPlayer(platform, queue, soundCategory, volume, transposeNotes);
        }
    }
}
