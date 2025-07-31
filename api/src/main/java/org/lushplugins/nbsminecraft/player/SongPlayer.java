package org.lushplugins.nbsminecraft.player;

import net.raphimc.noteblocklib.data.MinecraftDefinitions;
import net.raphimc.noteblocklib.data.MinecraftInstrument;
import net.raphimc.noteblocklib.format.nbs.model.NbsCustomInstrument;
import net.raphimc.noteblocklib.model.Note;
import net.raphimc.noteblocklib.model.Song;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.nbsminecraft.NBSAPI;
import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.player.emitter.GlobalSoundEmitter;
import org.lushplugins.nbsminecraft.player.emitter.SoundEmitter;
import org.lushplugins.nbsminecraft.song.Playlist;
import org.lushplugins.nbsminecraft.song.SongQueue;
import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.SoundCategory;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class SongPlayer {
    private final InternalSongPlayer player = new InternalSongPlayer();
    private final AbstractPlatform platform;
    private final SoundEmitter soundEmitter;
    private final SongQueue queue;
    private final HashMap<UUID, AudioListener> listeners = new HashMap<>();
    private final SoundCategory soundCategory;
    private int volume;
    private final boolean transposeNotes;

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
        return this.player.getSong();
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
        return this.player.getSong().getLengthInSeconds();
    }

    /**
     * @return whether the player is marked as playing (A player can still be playing
     * even if no song is currently playing)
     */
    public boolean isPlaying() {
        return !this.player.isPaused() && this.player.isRunning();
    }

    /**
     * Start/continue playing the current song
     */
    public void play() {
        this.ensurePlaying();
    }

    private void ensurePlaying() {
        this.player.setPaused(false);
        this.playNextSongIfIdle();

        if (!this.player.isRunning()) {
            this.player.start(0, this.player.getTick());
        }
    }

    /**
     * Pause the current song
     */
    public void pause() {
        this.player.setPaused(true);
    }

    /**
     * Stop the player and clear the queue
     */
    public void stop() {
        this.player.stop();
        this.queue.clearQueue();
        this.player.setSong(null);
        this.player.setTick(0);
        this.songStartTime = -1;
    }

    /**
     * Skips to the next song in the queue, if no songs are left in the queue
     * then the player will be stopped.
     */
    public void skip() {
        playNextSong();
    }

    /**
     * Skips to the next song in the queue, if no songs are left in the queue
     * then the player will be stopped.
     */
    public void playNextSong() {
        if (!this.queue.isEmpty()) {
            playSong(this.queue.poll());
        } else {
            stop();
        }
    }

    /**
     * Plays the next song in the queue if no song is currently playing, if no songs are
     * left in the queue then the player will be stopped.
     */
    public void playNextSongIfIdle() {
        if (this.player.getSong() == null) {
            playNextSong();
        }
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
     * @param song song to play, if {@code null} then the song player will stop ticking until started again
     */
    public void playSong(Song song) {
        this.player.setSong(song);
        this.player.setTick(0);

        if (song == null) {
            this.player.stop();
            return;
        }

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

    private class InternalSongPlayer extends net.raphimc.noteblocklib.player.SongPlayer {

        public InternalSongPlayer() {
            super(null);
        }

        @Override
        public void setSong(Song song) {
            super.setSong(song);
        }

        protected void playNote(Note note) {
            // TODO: Move note shifting into NBSAPI class and modify notes when reading song
            if (transposeNotes) {
                MinecraftDefinitions.instrumentShiftNote(note);
                MinecraftDefinitions.transposeNoteKey(note);
            } else {
                MinecraftDefinitions.applyExtendedNotesResourcePack(note);
            }

            String sound;
            if (note.getInstrument() instanceof NbsCustomInstrument instrument) {
                sound = instrument.getName();
            } else if (note.getInstrument() instanceof MinecraftInstrument instrument) {
                sound = instrument.mcSoundName();
            } else {
                throw new IllegalStateException("Invalid instrument found");
            }

            float volume = ((SongPlayer.this.volume / 100F) * note.getVolume());
            float pitch = note.getPitch();

            sound = sound.toLowerCase();
            for (AudioListener listener : listeners.values()) {
                soundEmitter.playSound(platform, listener, sound, soundCategory, volume, pitch);
            }
        }

        @Override
        protected void playNotes(List<Note> notes) {
            for (Note note : notes) {
                if (note == null) {
                    continue;
                }

                try {
                    playNote(note);
                } catch (Throwable e) {
                    String exceptionClassName = e.getClass().getSimpleName();
                    if (exceptionClassName.equals("ResourceLocationException")) {
                        NBSAPI.INSTANCE.log(Level.WARNING, "Invalid instrument: " + e.getMessage());
                        return;
                    }

                    // TODO: Shrink invalid instrument exceptions to 1 line (or parse and remove invalid instruments on song queue)
                    e.printStackTrace();
                }
            }
        }

        @Override
        protected boolean shouldTick() {
            playNextSongIfIdle();
            return getSong() != null;
        }

        @Override
        protected void onSongFinished() {
            skip();
        }

        @Override
        protected void onTickException(Throwable e) {
            e.printStackTrace();
        }
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

        public SongPlayer.Builder soundEmitter(SoundEmitter soundEmitter) {
            this.soundEmitter = soundEmitter;
            return this;
        }

        public SongPlayer.Builder setQueue(SongQueue queue) {
            this.queue = queue;
            return this;
        }

        public SongPlayer.Builder queue(Song song) {
            this.queue.queueSong(song);
            return this;
        }

        public SongPlayer.Builder queue(Playlist playlist) {
            this.queue.queuePlaylist(playlist);
            return this;
        }

        public SongPlayer.Builder soundCategory(SoundCategory soundCategory) {
            this.soundCategory = soundCategory;
            return this;
        }

        public SongPlayer.Builder volume(int volume) {
            this.volume = volume;
            return this;
        }

        public SongPlayer.Builder transposeNotes(boolean transposeNotes) {
            this.transposeNotes = transposeNotes;
            return this;
        }

        public SongPlayer build() {
            return new SongPlayer(platform, soundEmitter, queue, soundCategory, volume, transposeNotes);
        }
    }
}
