package org.lushplugins.nbsminecraft;

import net.raphimc.noteblocklib.NoteBlockLib;
import net.raphimc.noteblocklib.format.SongFormat;
import net.raphimc.noteblocklib.model.Song;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBSAPI {
    public static final NBSAPI INSTANCE = new NBSAPI();
    private static final Logger LOGGER = Logger.getLogger("NBSMinecraft");

    private final ScheduledExecutorService threads = Executors.newScheduledThreadPool(1);

    /**
     * @return the thread pool used to send sounds to players
     */
    public ScheduledExecutorService getThreadPool() {
        return threads;
    }

    /**
     * Read a song from a file
     * @param file file to read
     * @return parsed song
     */
    public Song readSongFile(File file) {
        try {
            return NoteBlockLib.readSong(file);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to read song '%s': ".formatted(file.getName()), e);
            return null;
        }
    }

    /**
     * Read a song from an input stream
     * @param inputStream input stream to read
     * @return parsed song
     */
    public Song readSongInputStream(InputStream inputStream) {
        try {
            return NoteBlockLib.readSong(inputStream, SongFormat.NBS);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to read song from input stream: ", e);
            return null;
        }
    }

    /**
     * Read all songs in a directory
     * @param directory directory to read from
     * @return map of file name to song
     */
    public Map<String, Song> readSongsInDirectory(File directory) {
        try (
            DirectoryStream<Path> fileStream = Files.newDirectoryStream(directory.toPath(), "*.nbs")
        ) {
            HashMap<String, Song> songs = new HashMap<>();

            for (Path filePath : fileStream) {
                File file = filePath.toFile();
                Song song = readSongFile(file);

                if (song != null) {
                    songs.put(file.getName(), song);
                }
            }

            return songs;
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, String.format("Failed to read songs in directory '%s': ", directory.getName()), e);
            return null;
        }
    }

    public void log(Level level, String message) {
        LOGGER.log(level, message);
    }

}
