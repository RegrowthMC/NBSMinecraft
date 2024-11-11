package org.lushplugins.nbsminecraft.player.emitter;

import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.EntityReference;
import org.lushplugins.nbsminecraft.utils.SoundCategory;

public class EntitySoundEmitter extends SoundEmitter {
    private final EntityReference entityReference;

    public EntitySoundEmitter(EntityReference entityReference) {
        this.entityReference = entityReference;
    }

    @Override
    public void playSound(AbstractPlatform platform, AudioListener listener, String sound, SoundCategory category, float volume, float pitch) {
        platform.playSound(listener, entityReference, sound, category, volume, pitch);
    }
}
