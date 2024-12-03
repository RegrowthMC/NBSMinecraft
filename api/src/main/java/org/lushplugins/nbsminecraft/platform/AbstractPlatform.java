package org.lushplugins.nbsminecraft.platform;

import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.EntityReference;
import org.lushplugins.nbsminecraft.utils.SoundCategory;
import org.lushplugins.nbsminecraft.utils.SoundLocation;

public abstract class AbstractPlatform {

    public abstract void playSound(AudioListener listener, String sound, SoundCategory category, float volume, float pitch);

    public abstract void playSound(AudioListener listener, EntityReference entityReference, String sound, SoundCategory category, float volume, float pitch);

    public abstract void playSound(AudioListener listener, SoundLocation location, String sound, SoundCategory category, float volume, float pitch);

    public void invalidateIfCached(AudioListener listener) {}
}
