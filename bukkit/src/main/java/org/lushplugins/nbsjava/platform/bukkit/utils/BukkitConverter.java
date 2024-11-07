package org.lushplugins.nbsjava.platform.bukkit.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.lushplugins.nbsjava.utils.SoundLocation;

public class BukkitConverter {

    public static Location convert(SoundLocation location) {
        return new Location(Bukkit.getWorld(location.world()), location.x(), location.y(), location.z());
    }

    public static SoundCategory convert(org.lushplugins.nbsjava.utils.SoundCategory category) {
        return switch (category) {
            case MASTER -> SoundCategory.MASTER;
            case MUSIC -> SoundCategory.MUSIC;
            case RECORDS -> SoundCategory.RECORDS;
            case WEATHER -> SoundCategory.WEATHER;
            case BLOCKS -> SoundCategory.BLOCKS;
            case HOSTILE -> SoundCategory.HOSTILE;
            case NEUTRAL -> SoundCategory.NEUTRAL;
            case PLAYERS -> SoundCategory.PLAYERS;
            case AMBIENT -> SoundCategory.AMBIENT;
            case VOICE -> SoundCategory.VOICE;
        };
    }
}
