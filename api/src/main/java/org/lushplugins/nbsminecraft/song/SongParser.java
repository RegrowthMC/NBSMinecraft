package org.lushplugins.nbsminecraft.song;

import net.raphimc.noteblocklib.format.minecraft.MinecraftDefinitions;
import net.raphimc.noteblocklib.format.minecraft.MinecraftInstrument;
import net.raphimc.noteblocklib.model.song.Song;

@FunctionalInterface
public interface SongParser {
    void parse(Song song);

    SongParser DEFAULT = (song) -> song.getNotes().forEach((note) -> {
        MinecraftDefinitions.instrumentShiftNote(note);
        MinecraftDefinitions.transposeNoteKey(note);
    });
    SongParser EXTENDED_NOTES_RESOURCE_PACK = (song) -> song.getNotes().forEach((note) -> {
        if (note.getInstrument() instanceof MinecraftInstrument) {
            MinecraftDefinitions.applyExtendedNotesResourcePack(note);
        } else {
            DEFAULT.parse(song);
        }
    });
}
