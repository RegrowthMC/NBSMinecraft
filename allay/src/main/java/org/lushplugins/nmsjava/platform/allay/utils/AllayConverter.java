package org.lushplugins.nmsjava.platform.allay.utils;

import org.cloudburstmc.math.vector.Vector3f;
import org.lushplugins.nbsjava.utils.SoundLocation;

public class AllayConverter {

    public static Vector3f convert(SoundLocation location) {
        return Vector3f.from(location.x(), location.y(), location.z());
    }
}
