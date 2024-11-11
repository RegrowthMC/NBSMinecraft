package org.lushplugins.nbsminecraft.player.emitter;

import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.SoundCategory;
import org.lushplugins.nbsminecraft.utils.SoundLocation;

public class StaticSoundEmitter extends SoundEmitter {
    private final SoundLocation location;

    public StaticSoundEmitter(SoundLocation location) {
        this.location = location;
    }

    @Override
    public void playSound(AbstractPlatform platform, AudioListener listener, String sound, SoundCategory category, float volume, float pitch) {
        platform.playSound(listener, location, sound, category, volume, pitch);
    }
}
