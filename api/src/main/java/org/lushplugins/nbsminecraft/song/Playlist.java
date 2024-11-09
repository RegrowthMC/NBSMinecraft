package org.lushplugins.nbsminecraft.song;

import cz.koca2000.nbs4j.Song;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Playlist {
    private final List<Song> songs;

    public Playlist(Song... songs) {
        this.songs = Arrays.asList(songs);
    }

    public Playlist(List<Song> songs) {
        this.songs = Collections.unmodifiableList(songs);
    }

    /**
     * @return a list of the songs in this playlist
     */
    public List<Song> getSongs() {
        return songs;
    }

    /**
     * @return a shuffled list of the songs in this playlist
     */
    public List<Song> getShuffledSongs() {
        List<Song> shuffledSongs = new ArrayList<>(songs);
        Collections.shuffle(shuffledSongs);
        return shuffledSongs;
    }
}
