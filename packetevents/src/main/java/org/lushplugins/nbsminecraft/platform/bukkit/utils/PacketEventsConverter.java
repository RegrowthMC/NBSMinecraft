package org.lushplugins.nbsminecraft.platform.bukkit.utils;

import com.github.retrooper.packetevents.protocol.sound.SoundCategory;
import com.github.retrooper.packetevents.util.Vector3i;
import org.lushplugins.nbsminecraft.utils.SoundLocation;

public class PacketEventsConverter {

    public static Vector3i convert(SoundLocation location) {
        return new Vector3i((int) location.x(), (int) location.y(), (int) location.z());
    }

    public static SoundCategory convert(org.lushplugins.nbsminecraft.utils.SoundCategory category) {
        return switch (category) {
            case MASTER -> SoundCategory.MASTER;
            case MUSIC -> SoundCategory.MUSIC;
            case RECORDS -> SoundCategory.RECORD;
            case WEATHER -> SoundCategory.WEATHER;
            case BLOCKS -> SoundCategory.BLOCK;
            case HOSTILE -> SoundCategory.HOSTILE;
            case NEUTRAL -> SoundCategory.NEUTRAL;
            case PLAYERS -> SoundCategory.PLAYER;
            case AMBIENT -> SoundCategory.AMBIENT;
            case VOICE -> SoundCategory.VOICE;
        };
    }
}
