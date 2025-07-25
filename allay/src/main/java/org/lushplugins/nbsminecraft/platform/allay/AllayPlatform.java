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
import org.joml.Vector3dc;
import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.EntityReference;
import org.lushplugins.nbsminecraft.utils.SoundCategory;
import org.lushplugins.nbsminecraft.utils.SoundLocation;
import org.lushplugins.nbsminecraft.platform.allay.utils.AllayConverter;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class AllayPlatform extends AbstractPlatform {
    public static final AllayPlatform INSTANCE = new AllayPlatform();

    private final Cache<UUID, Entity> entityCache = CacheBuilder.newBuilder()
        .expireAfterAccess(5, TimeUnit.SECONDS)
        .build();

    @Override
    public void playSound(AudioListener listener, String sound, SoundCategory category, float volume, float pitch) {
        EntityPlayer player = findPlayer(listener);
        if (player == null) {
            return;
        }

        Vector3dc vector = player.getLocation();
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

        Vector3dc vector = player.getLocation();
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
        Entity entity = entityCache.getIfPresent(entityReference.uuid());
        if (!(entity instanceof EntityPlayer player) || player.isDisconnected()) {
            EntityPlayer player = Server.getInstance().getPlayerService().getPlayers().get(entityReference.uuid());
            if (player != null) {
                entityCache.put(entityReference.uuid(), player);
            }

            return player;
        } else {
            return player;
        }
    }

    private @Nullable Entity findEntity(EntityReference entityReference) {
        Entity entity = entityCache.getIfPresent(entityReference.uuid());
        if (entity == null || !entity.isAlive()) {
            entity = getEntity(entityReference.entityId());
            if (entity != null) {
                entityCache.put(entityReference.uuid(), entity);
            }
        }

        return entity;
    }

    private Entity getEntity(int entityId) {
        for (World world : Server.getInstance().getWorldPool().getWorlds().values()) {
            for (Dimension dimension : world.getDimensions().values()) {
                return dimension.getEntityService().getEntity(entityId);
            }
        }

        return null;
    }

    @Override
    public void invalidateIfCached(AudioListener listener) {
        entityCache.invalidate(listener.uuid());
    }
}
