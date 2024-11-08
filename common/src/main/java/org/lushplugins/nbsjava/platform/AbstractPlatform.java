package org.lushplugins.nbsjava.platform;

import org.lushplugins.nbsjava.utils.AudioListener;
import org.lushplugins.nbsjava.utils.EntityReference;
import org.lushplugins.nbsjava.utils.SoundCategory;
import org.lushplugins.nbsjava.utils.SoundLocation;

public abstract class AbstractPlatform {

    public abstract void playSound(AudioListener listener, String sound, SoundCategory category, float volume, float pitch);

    public abstract void playSound(AudioListener listener, EntityReference entityReference, String sound, SoundCategory category, float volume, float pitch);

    public abstract void playSound(AudioListener listener, SoundLocation location, String sound, SoundCategory category, float volume, float pitch);
}
