package org.lushplugins.nbsjava.utils;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AudioListener extends EntityReference {
    private float volume;

    public AudioListener(int entityId, @NotNull UUID uuid, float volume) {
        super(entityId, uuid);
        this.volume = volume;
    }

    public AudioListener(int entityId, @NotNull UUID uuid) {
        this(entityId, uuid, 1.0f);
    }

    /**
     * @return the listener's volume
     */
    public float getVolume() {
        return volume;
    }

    /**
     * Set the listener's volume
     * @param volume volume
     */
    public void setVolume(float volume) {
        this.volume = volume;
    }
}
