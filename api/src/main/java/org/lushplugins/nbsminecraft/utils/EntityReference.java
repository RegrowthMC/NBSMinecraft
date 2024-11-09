package org.lushplugins.nbsminecraft.utils;

import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class EntityReference {
    private final int entityId;
    private final UUID uuid;

    public EntityReference(int entityId, @NotNull UUID uuid) {
        this.entityId = entityId;
        this.uuid = uuid;
    }

    public int entityId() {
        return entityId;
    }

    public UUID uuid() {
        return uuid;
    }
}
