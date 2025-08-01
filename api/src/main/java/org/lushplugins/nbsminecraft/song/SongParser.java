package org.lushplugins.nbsminecraft.song;

import net.raphimc.noteblocklib.data.MinecraftDefinitions;
import net.raphimc.noteblocklib.data.MinecraftInstrument;
import net.raphimc.noteblocklib.model.Song;
import org.lushplugins.nbsminecraft.utils.ExtendedMinecraftInstrument;

@FunctionalInterface
public interface SongParser {
    void parse(Song song);

    SongParser DEFAULT = (song) -> song.getNotes().forEach((note) -> {
        MinecraftDefinitions.instrumentShiftNote(note);
        MinecraftDefinitions.transposeNoteKey(note);
    });
    SongParser EXTENDED_NOTES_RESOURCE_PACK = (song) -> song.getNotes().forEach((note) -> {
        if (note.getInstrument() instanceof MinecraftInstrument instrument) {
            int octaveShift = MinecraftDefinitions.applyExtendedNotesResourcePack(note);
            if (octaveShift != 0) {
                note.setInstrument(new ExtendedMinecraftInstrument(instrument, octaveShift));
            }
        } else {
            DEFAULT.parse(song);
        }
    });
}
