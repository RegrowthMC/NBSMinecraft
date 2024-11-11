package org.lushplugins.nbsminecraft.player.emitter;

import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.SoundCategory;

public abstract class SoundEmitter {

    public abstract void playSound(AbstractPlatform platform, AudioListener listener, String sound, SoundCategory category, float volume, float pitch);
}
