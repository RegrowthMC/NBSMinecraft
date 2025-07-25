package org.lushplugins.nbsminecraft.player;

import cz.koca2000.nbs4j.Layer;
import cz.koca2000.nbs4j.Note;
import cz.koca2000.nbs4j.Song;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.nbsminecraft.NBSAPI;
import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.player.emitter.GlobalSoundEmitter;
import org.lushplugins.nbsminecraft.player.emitter.SoundEmitter;
import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.Instruments;
import org.lushplugins.nbsminecraft.song.Playlist;
import org.lushplugins.nbsminecraft.song.SongQueue;
import org.lushplugins.nbsminecraft.utils.PitchUtils;
import org.lushplugins.nbsminecraft.utils.SoundCategory;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class SongPlayer {
    private final Semaphore semaphore = new Semaphore(1);
    private final AbstractPlatform platform;
    private final SoundEmitter soundEmitter;
    private final SongQueue queue;
    private final HashMap<UUID, AudioListener> listeners = new HashMap<>();
    private final SoundCategory soundCategory;
    private int volume;
    private final boolean transposeNotes;

    private Song song = null;
    private boolean playing = true;
    private int songTick = 0;
    private long songStartTime = -1;

    private SongPlayer(AbstractPlatform platform, SoundEmitter soundEmitter, SongQueue queue, SoundCategory soundCategory, int volume, boolean transposeNotes) {
        this.platform = platform;
        this.soundEmitter = soundEmitter;
        this.queue = queue;
        this.soundCategory = soundCategory;
        this.volume = volume;
        this.transposeNotes = transposeNotes;
    }

    /**
     * Gets the song queue, adding songs directly to the queue will not ensure that
     * the player is playing
     * @return player's song queue
     */
    public SongQueue getQueue() {
        return this.queue;
    }

    /**
     * Add an audio listener
     * @param listener audio listener
     */
    public void addListener(AudioListener listener) {
        this.listeners.put(listener.uuid(), listener);
    }

    /**
     * Remove listener
     * @param uuid listener's uuid
     */
    public void removeListener(UUID uuid) {
        this.listeners.remove(uuid);
    }

    /**
     * Set the player's volume
     * @param volume volume
     */
    public void setVolume(int volume) {
        this.volume = volume;
    }

    /**
     * @return current song
     */
    public @Nullable Song getCurrentSong() {
        return this.song;
    }

    /**
     * @return time in seconds since the start of the current song
     */
    public long getSongPlaytime() {
        return this.songStartTime != -1 ? Instant.now().getEpochSecond() - this.songStartTime : -1;
    }

    /**
     * @return total duration of current song in seconds
     */
    public long getSongDuration() {
        return (long) this.song.getSongLengthInSeconds();
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

    private void ensurePlaying() {
        if (!this.playing) {
            this.playing = true;
            tickSong();
        }
    }

    /**
     * Pause the current song
     */
    public void pause() {
        this.playing = false;
    }

    /**
     * Stop the player and clear the queue
     */
    public void stop() {
        this.playing = false;
        this.queue.clearQueue();
        this.song = null;
        this.songTick = 0;
    }

    /**
     * Skip to the next queue song
     */
    public void skip() {
        playSong(this.queue.poll());
    }

    /**
     * Set the queue loop status
     * @param loop whether the queue should loop
     */
    public void loopQueue(boolean loop) {
        this.queue.loop(loop);
    }

    /**
     * Shuffle the queue
     */
    public void shuffleQueue() {
        this.queue.shuffle();
    }

    /**
     * Immediately plays a song, this will stop the current song
     * @param song song to play
     */
    public void playSong(Song song) {
        this.song = song;
        this.songTick = 0;
        this.songStartTime = Instant.now().getEpochSecond();
        ensurePlaying();
    }

    /**
     * Queue a song to be played
     * @param song song to queue
     */
    public void queueSong(Song song) {
        this.queue.queueSong(song);
        ensurePlaying();
    }

    /**
     * Queue songs to be played
     * @param songs songs to queue
     */
    public void queueSongs(Collection<Song> songs) {
        this.queue.queueSongs(songs);
        ensurePlaying();
    }

    /**
     * Queue a song in the priority queue, songs in the priority queue will be played before
     * songs in the default queue and will not be effected by queue looping or shuffling.
     * @param song song to queue
     */
    public void queueSongPriority(Song song) {
        this.queue.queueSongPriority(song);
        ensurePlaying();
    }

    /**
     * Tick the current song
     */
    public void tickSong() {
        if (!isPlaying()) {
            return;
        }

        try {
            this.semaphore.acquire();

            if (this.song == null) {
                if (this.queue.isEmpty()) {
                    playing = false;
                    return;
                } else {
                    playSong(queue.poll());
                }
            }

            float tempo = this.song.getTempo(this.songTick + 1);
            long period = (long) (1000 / tempo);
            NBSAPI.INSTANCE.getThreadPool().schedule(this::tickSong, period, TimeUnit.MILLISECONDS);

            if (!this.listeners.isEmpty() && this.volume > 0) {
                for (Layer layer : this.song.getLayers()) {
                    Note note = layer.getNote(this.songTick);
                    if (note == null) {
                        continue;
                    }

                    String sound;
                    if (note.isCustomInstrument()) {
                        sound = this.song.getCustomInstrument(note.getInstrument()).getName();
                    } else {
                        sound = Instruments.getSound(note.getInstrument());
                    }

                    float volume = (layer.getVolume() * this.volume * note.getVolume()) / 1_000_000F;
                    float pitch;
                    if (this.transposeNotes) {
                        pitch = PitchUtils.getTransposedPitch(note);
                    } else {
                        sound = PitchUtils.addOctaveSuffix(sound, note.getKey());
                        pitch = PitchUtils.getPitchInOctave(note);
                    }

                    for (AudioListener listener : this.listeners.values()) {
                        this.soundEmitter.playSound(this.platform, listener, sound, this.soundCategory, volume, pitch);
                    }
                }
            }

            this.songTick++;

            if (this.song.getSongLength() < this.songTick) {
                onSongFinish();
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.semaphore.release();
        }
    }

    private void onSongFinish() {
        playSong(this.queue.poll());
    }

    public static class Builder {
        private final AbstractPlatform platform;
        private SoundEmitter soundEmitter = new GlobalSoundEmitter();
        private SongQueue queue = new SongQueue();
        private SoundCategory soundCategory = SoundCategory.RECORDS;
        private int volume = 100;
        private boolean transposeNotes = false;

        public Builder(AbstractPlatform platform) {
            this.platform = platform;
        }

        public Builder soundEmitter(SoundEmitter soundEmitter) {
            this.soundEmitter = soundEmitter;
            return this;
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
            return new SongPlayer(platform, soundEmitter, queue, soundCategory, volume, transposeNotes);
        }
    }
}
