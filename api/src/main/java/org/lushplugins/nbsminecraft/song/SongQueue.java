package org.lushplugins.nbsminecraft.song;

import cz.koca2000.nbs4j.Song;

import java.util.*;

public class SongQueue {
    private final Deque<Song> priorityQueue = new ArrayDeque<>();
    private final Deque<Song> queue = new ArrayDeque<>();
    private boolean loop;

    public SongQueue() {}

    public SongQueue(Song song) {
        queueSong(song);
    }

    public SongQueue(Playlist playlist) {
        queuePlaylist(playlist);
    }

    /**
     * Queue a song to be played
     * @param song song to queue
     */
    public void queueSong(Song song) {
       queue.add(song);
    }

    /**
     * Queue songs to be played
     * @param songs songs to queue
     */
    public void queueSongs(Song... songs) {
        for (Song song : songs) {
            queueSong(song);
        }
    }

    /**
     * Queue songs to be played
     * @param songs songs to queue
     */
    public void queueSongs(Collection<Song> songs) {
        queue.addAll(songs);
    }

    /**
     * Queue a song in the priority queue, songs in the priority queue will be played before
     * songs in the default queue and will not be effected by queue looping or shuffling.
     * @param song song to queue
     */
    public void queueSongPriority(Song song) {
        priorityQueue.add(song);
    }

    /**
     * Queue a playlist of songs
     * @param playlist playlist to queue
     */
    public void queuePlaylist(Playlist playlist) {
        queue.addAll(playlist.getSongs());
    }

    /**
     * Queue and shuffle a playlist of songs
     * @param playlist playlist to shuffle and queue
     */
    public void queueShuffledPlaylist(Playlist playlist) {
        queue.addAll(playlist.getShuffledSongs());
    }

    /**
     * Collects the next queued song and adds the song to the back of the queue if looping is enabled
     * @return queued song
     */
    public Song poll() {
        if (!priorityQueue.isEmpty()) {
            return priorityQueue.poll();
        } else {
            Song song = queue.poll();

            if (this.isLooping()) {
                queueSong(song);
            }

            return song;
        }
    }

    /**
     * Remove all songs from the queue
     */
    public void clearQueue() {
        queue.clear();
    }

    /**
     * @return whether the queue is looping
     */
    public boolean isLooping() {
        return loop;
    }

    /**
     * Set the queue loop status
     * @param loop whether the queue should loop
     */
    public void loop(boolean loop) {
        this.loop = loop;
    }

    /**
     * Shuffle the queue
     */
    public void shuffle() {
        List<Song> queueSnapshot = new ArrayList<>(queue);

        Collections.shuffle(queueSnapshot);

        queue.clear();
        queue.addAll(queueSnapshot);
    }
}
