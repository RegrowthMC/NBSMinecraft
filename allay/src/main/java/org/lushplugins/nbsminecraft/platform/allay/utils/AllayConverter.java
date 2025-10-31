package org.lushplugins.nbsminecraft.platform.allay.utils;

import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.lushplugins.nbsminecraft.utils.SoundLocation;

public class AllayConverter {
    public static Vector3dc convert(SoundLocation location) {
        return new Vector3d(location.x(), location.y(), location.z());
    }
}
