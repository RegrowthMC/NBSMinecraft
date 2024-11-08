package org.lushplugins.nbsjava.platform;

import org.lushplugins.nbsjava.utils.SoundCategory;
import org.lushplugins.nbsjava.utils.SoundLocation;

import java.util.UUID;

public abstract class AbstractPlatform {

    public abstract void playSound(int entityId, String sound, SoundCategory category, float volume, float pitch);

    public abstract void playSound(int entityId, SoundLocation location, String sound, SoundCategory category, float volume, float pitch);
}
