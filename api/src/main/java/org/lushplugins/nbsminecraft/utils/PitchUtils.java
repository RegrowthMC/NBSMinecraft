/*
 * This package uses methods adapted from koca2000's NoteBlockAPI
 * Link: https://github.com/koca2000/NoteBlockAPI
 */

package org.lushplugins.nbsminecraft.utils;

import cz.koca2000.nbs4j.Note;

public class PitchUtils {

    /**
     * Get pitch within the note's octave
     * @param note note
     * @return pitch within octave
     */
    public static float getPitchInOctave(Note note) {
        int key = note.getKey();
        float pitch = note.getPitch() / 100F;

        // Apply pitch to key
        key += (int) (pitch / 100);
        pitch %= 100;


        key = adjustKey(key);

        return (float) Math.pow(2, ((key * 100 + pitch) - 1200d) / 1200d);
    }

    private static int adjustKey(int key) {
        int start = 9;
        int difference = 24;

        if (key < start) {
            return key + 15; // Handles first interval
        }

        int octave = calculateOctave(key) + 1;
        int threshold = start + (octave * difference);

        return key - threshold;
    }

    /**
     * Transpose pitch within vanilla minecraft's bounds
     * @param note note
     * @return transposed pitch
     */
    public static float getTransposedPitch(Note note) {
        int key = note.getKey();
        float pitch = note.getPitch() / 100F;

        // Apply key to pitch
        pitch += key * 100;

        // Transposes Notes
        pitch = ((pitch - 3300) % 1200 + 1200) % 1200;

        return (float) Math.pow(2, (pitch - 1200d) / 1200d);
    }

    /**
     * Add an octave suffix to provide support for custom resource packs providing
     * a larger range of notes
     * @param sound original sound name
     * @param key note key
     * @return sound name with octave suffix
     */
    public static String addOctaveSuffix(String sound, int key) {
        int octave = calculateOctave(key);
        if (octave == 0) {
            return sound;
        } else {
            return sound + "_" + octave;
        }
    }

    private static int calculateOctave(int key) {
        // Calculate which octave this needs to be in
        // '9' is the start of the first octave
        // '24' is the number of available keys per minecraft sound file
        return ((key - 9) / 24) - 1;
    }
}
