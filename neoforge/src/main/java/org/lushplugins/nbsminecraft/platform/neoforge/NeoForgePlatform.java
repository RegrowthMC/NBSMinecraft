package org.lushplugins.nbsminecraft.platform.neoforge;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.platform.neoforge.utils.NeoForgeConverter;
import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.EntityReference;
import org.lushplugins.nbsminecraft.utils.SoundLocation;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class NeoForgePlatform extends AbstractPlatform {
    private final Cache<UUID, Entity> entityCache = CacheBuilder.newBuilder()
        .expireAfterAccess(5, TimeUnit.SECONDS)
        .build();
    private final MinecraftServer server;

    public NeoForgePlatform(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void playSound(AudioListener listener, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        ServerPlayer player = findPlayer(listener.uuid());
        if (player == null || player.isRemoved()) {
            return;
        }

        playSound(player, player, sound, category, volume, pitch);
    }

    @Override
    public void playSound(AudioListener listener, EntityReference entityReference, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        ServerPlayer player = findPlayer(listener.uuid());
        if (player == null || player.isRemoved()) {
            return;
        }

        Entity entity = findEntity(listener.uuid());
        if (entity == null) {
            return;
        }

        playSound(player, entity, sound, category, volume, pitch);
    }

    private void playSound(ServerPlayer player, Entity entity, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        SoundSource soundSource = NeoForgeConverter.convert(category);

        Holder.Reference<SoundEvent> soundEvent = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse(sound)).orElse(null);
        if (soundEvent == null) {
            return;
        }

        player.connection.send(new ClientboundSoundEntityPacket(
            soundEvent,
            soundSource,
            entity,
            volume,
            pitch,
            player.getRandom().nextLong()
        ));
    }

    @Override
    public void playSound(AudioListener listener, SoundLocation location, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        ServerLevel world = this.server.getLevel(ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(location.world())));
        if (world == null) {
            return;
        }

        ServerPlayer player = findPlayer(listener.uuid());
        if (player == null || player.isRemoved()) {
            return;
        }

        Holder.Reference<SoundEvent> soundEvent = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.parse(sound)).orElse(null);
        if (soundEvent == null) {
            return;
        }

        Vector3d position = NeoForgeConverter.convert(location);
        SoundSource soundSource = NeoForgeConverter.convert(category);

        player.connection.send(new ClientboundSoundPacket(
            soundEvent,
            soundSource,
            position.x(),
            position.y(),
            position.z(),
            volume,
            pitch,
            player.getRandom().nextLong()
        ));
    }

    private @Nullable ServerPlayer findPlayer(UUID uuid) {
        Entity entity = entityCache.getIfPresent(uuid);
        if (!(entity instanceof ServerPlayer player) || player.isRemoved()) {
            ServerPlayer player = this.server.getPlayerList().getPlayer(uuid);
            if (player != null) {
                entityCache.put(uuid, player);
            }

            return player;
        } else {
            return player;
        }
    }

    private @Nullable Entity findEntity(UUID uuid) {
        Entity entity = entityCache.getIfPresent(uuid);
        if (entity == null || entity.isRemoved()) {
            for (ServerLevel world : this.server.getAllLevels()) {
                entity = world.getEntity(uuid);
                if (entity != null) {
                    entityCache.put(uuid, entity);
                    return entity;
                }
            }

            return null;
        }

        return entity;
    }

    @Override
    public void invalidateIfCached(AudioListener listener) {
        entityCache.invalidate(listener.uuid());
    }
}
