package org.lushplugins.nbsminecraft.platform.neoforge.utils;

import net.minecraft.sounds.SoundSource;
import org.joml.Vector3d;
import org.lushplugins.nbsminecraft.utils.SoundLocation;

public class NeoForgeConverter {

    public static Vector3d convert(SoundLocation location) {
        return new Vector3d(location.x(), location.y(), location.z());
    }

    public static SoundSource convert(org.lushplugins.nbsminecraft.utils.SoundCategory category) {
        return switch (category) {
            case MASTER -> SoundSource.MASTER;
            case MUSIC -> SoundSource.MUSIC;
            case RECORDS -> SoundSource.RECORDS;
            case WEATHER -> SoundSource.WEATHER;
            case BLOCKS -> SoundSource.BLOCKS;
            case HOSTILE -> SoundSource.HOSTILE;
            case NEUTRAL -> SoundSource.NEUTRAL;
            case PLAYERS -> SoundSource.PLAYERS;
            case AMBIENT -> SoundSource.AMBIENT;
            case VOICE -> SoundSource.VOICE;
        };
    }
}
