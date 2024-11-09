package org.lushplugins.nbsminecraft.utils;

import java.util.List;

/**
 * This class exists as a temporary more useful implementation of {@link cz.koca2000.nbs4j.Instrument}
 */
public class Instruments {
    private static final List<String> instruments = List.of(
        "minecraft:block.note_block.harp",
        "minecraft:block.note_block.bass",
        "minecraft:block.note_block.basedrum",
        "minecraft:block.note_block.snare",
        "minecraft:block.note_block.hat",
        "minecraft:block.note_block.guitar",
        "minecraft:block.note_block.flute",
        "minecraft:block.note_block.bell",
        "minecraft:block.note_block.chime",
        "minecraft:block.note_block.xylophone",
        "minecraft:block.note_block.iron_xylophone",
        "minecraft:block.note_block.cow_bell",
        "minecraft:block.note_block.didgeridoo",
        "minecraft:block.note_block.bit",
        "minecraft:block.note_block.banjo",
        "minecraft:block.note_block.pling"
    );

    public static String getSound(int index) {
        return instruments.get(index);
    }
}
