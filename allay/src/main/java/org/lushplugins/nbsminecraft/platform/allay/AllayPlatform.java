package org.lushplugins.nbsminecraft.platform.allay;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.allaymc.api.entity.Entity;
import org.allaymc.api.entity.interfaces.EntityPlayer;
import org.allaymc.api.server.Server;
import org.allaymc.api.world.Dimension;
import org.allaymc.api.world.World;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.packet.PlaySoundPacket;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3fc;
import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.EntityReference;
import org.lushplugins.nbsminecraft.utils.SoundCategory;
import org.lushplugins.nbsminecraft.utils.SoundLocation;
import org.lushplugins.nbsminecraft.platform.allay.utils.AllayConverter;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class AllayPlatform extends AbstractPlatform {
    private final Cache<UUID, Entity> ENTITY_CACHE = CacheBuilder.newBuilder()
        .expireAfterAccess(60, TimeUnit.SECONDS)
        .build();

    @Override
    public void playSound(AudioListener listener, String sound, SoundCategory category, float volume, float pitch) {
        EntityPlayer player = findPlayer(listener);
        if (player == null) {
            return;
        }

        Vector3fc vector = player.getLocation();
        Vector3f position = Vector3f.from(vector.x(), vector.y(), vector.z());

        playSound(player, position, sound, volume, pitch);
    }

    @Override
    public void playSound(AudioListener listener, EntityReference entityReference, String sound, SoundCategory category, float volume, float pitch) {
        EntityPlayer player = findPlayer(listener);
        if (player == null) {
            return;
        }

        Entity entity = findEntity(entityReference);
        if (entity == null) {
            return;
        }

        Vector3fc vector = player.getLocation();
        Vector3f position = Vector3f.from(vector.x(), vector.y(), vector.z());

        playSound(player, position, sound, volume, pitch);
    }

    @Override
    public void playSound(AudioListener listener, SoundLocation location, String sound, SoundCategory category, float volume, float pitch) {
        EntityPlayer player = findPlayer(listener);
        if (player == null) {
            return;
        }

        Vector3f position = AllayConverter.convert(location);

        playSound(player, position, sound, volume, pitch);
    }

    private void playSound(EntityPlayer listener, Vector3f position, String sound, float volume, float pitch) {
        PlaySoundPacket packet = new PlaySoundPacket();
        packet.setSound(sound);
        packet.setVolume(volume);
        packet.setPitch(pitch);
        packet.setPosition(position);

        listener.sendPacket(packet);
    }

    private @Nullable EntityPlayer findPlayer(EntityReference entityReference) {
        try {
            Entity entity = ENTITY_CACHE.get(entityReference.uuid(), () -> Server.getInstance().getOnlinePlayers().get(entityReference.uuid()));
            if (entity instanceof EntityPlayer player) {
                return player;
            } else {
                return null;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private @Nullable Entity findEntity(EntityReference entityReference) {
        try {
            return ENTITY_CACHE.get(entityReference.uuid(), () -> {
                for (World world : Server.getInstance().getWorldPool().getWorlds().values()) {
                    for (Dimension dimension : world.getDimensions().values()) {
                        Entity entity = dimension.getEntityByRuntimeId(entityReference.entityId());
                        if (entity != null) {
                            return entity;
                        }
                    }
                }

                return null;
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
