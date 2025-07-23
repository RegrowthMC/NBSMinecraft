package org.lushplugins.nbsminecraft.platform.fabric.utils;

import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.Vec3d;
import org.lushplugins.nbsminecraft.utils.SoundLocation;

public class FabricConverter {

    public static Vec3d convert(SoundLocation location) {
        return new Vec3d(location.x(), location.y(), location.z());
    }

    public static SoundCategory convert(org.lushplugins.nbsminecraft.utils.SoundCategory category) {
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
