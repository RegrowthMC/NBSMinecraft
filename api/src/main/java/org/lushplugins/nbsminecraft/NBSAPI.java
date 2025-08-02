package org.lushplugins.nbsminecraft;

import net.raphimc.noteblocklib.NoteBlockLib;
import net.raphimc.noteblocklib.data.MinecraftInstrument;
import net.raphimc.noteblocklib.format.SongFormat;
import net.raphimc.noteblocklib.format.nbs.model.NbsCustomInstrument;
import net.raphimc.noteblocklib.model.Song;
import net.raphimc.noteblocklib.model.instrument.Instrument;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.nbsminecraft.song.SongParser;
import org.lushplugins.nbsminecraft.utils.ExtendedMinecraftInstrument;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NBSAPI {
    @Deprecated
    public static final NBSAPI INSTANCE = new NBSAPI();
    private static final Logger LOGGER = Logger.getLogger("NBSMinecraft");
    private static SongParser SONG_PARSER = SongParser.DEFAULT;


    public static void setSongParser(SongParser songParser) {
        SONG_PARSER = songParser;
    }

    /**
     * Read a song from a file
     * @param file file to read
     * @return parsed song
     */
    public static Song readSongFile(File file) {
        try {
            Song song = NoteBlockLib.readSong(file);
            validateSong(song, file.getName());
            return song;
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
    public static Song readSongInputStream(InputStream inputStream) {
        try {
            Song song = NoteBlockLib.readSong(inputStream, SongFormat.NBS);
            validateSong(song);
            return song;
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
    public static Map<String, Song> readSongsInDirectory(File directory) {
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

    public static void validateSong(Song song, @Nullable String fileName) {
        SONG_PARSER.parse(song);

        Set<String> invalidInstrumentTypes = new HashSet<>();
        Set<String> invalidInstrumentNames = new HashSet<>();

        song.getNotes().removeIf(note -> {
            Instrument instrument = note.getInstrument();
            if (!(instrument instanceof MinecraftInstrument) && !(instrument instanceof NbsCustomInstrument) && !(instrument instanceof ExtendedMinecraftInstrument)) {
                invalidInstrumentTypes.add(note.getInstrument().getClass().getName());
                return true;
            }

            if (instrument instanceof NbsCustomInstrument nbsInstrument && !nbsInstrument.getName().toLowerCase().matches("[a-z0-9/._:-]+")) {
                invalidInstrumentNames.add(nbsInstrument.getName());
                return true;
            }

            return false;
        });

        if (!invalidInstrumentTypes.isEmpty()) {
            log(Level.WARNING, "Invalid instrument types found in song '%s': %s".formatted(fileName != null ? fileName : song.getTitleOrFileName(), Arrays.toString(invalidInstrumentTypes.toArray())));
        }

        if (!invalidInstrumentNames.isEmpty()) {
            log(Level.WARNING, "Invalid instrument names found in song '%s': %s".formatted(fileName != null ? fileName : song.getTitleOrFileName(), Arrays.toString(invalidInstrumentNames.toArray())));
        }
    }

    public static void validateSong(Song song) {
        validateSong(song, null);
    }

    public static void log(Level level, String message, Throwable thrown) {
        LOGGER.log(level, message, thrown);
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, message);
    }
}
