package org.lushplugins.nbsminecraft.platform.fabric;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.s2c.play.PlaySoundFromEntityS2CPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.platform.fabric.utils.FabricConverter;
import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.EntityReference;
import org.lushplugins.nbsminecraft.utils.SoundLocation;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class FabricPlatform extends AbstractPlatform {
    private final Cache<UUID, Entity> entityCache = CacheBuilder.newBuilder()
        .expireAfterAccess(5, TimeUnit.SECONDS)
        .build();
    private final MinecraftServer server;

    public FabricPlatform(MinecraftServer server) {
        this.server = server;
    }

    @Override
    public void playSound(AudioListener listener, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        ServerPlayerEntity player = findPlayer(listener.uuid());
        if (player == null || player.isRemoved()) {
            return;
        }

        playSound(player, player, sound, category, volume, pitch);
    }

    @Override
    public void playSound(AudioListener listener, EntityReference entityReference, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        ServerPlayerEntity player = findPlayer(listener.uuid());
        if (player == null || player.isRemoved()) {
            return;
        }

        Entity entity = findEntity(listener.uuid());
        if (entity == null) {
            return;
        }

        playSound(player, entity, sound, category, volume, pitch);
    }

    private void playSound(ServerPlayerEntity player, Entity entity, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        SoundCategory fabricCategory = FabricConverter.convert(category);

        RegistryEntry.Reference<SoundEvent> soundEvent = Registries.SOUND_EVENT.getEntry(Identifier.of(sound)).orElse(null);
        if (soundEvent == null) {
            return;
        }

        player.networkHandler.sendPacket(new PlaySoundFromEntityS2CPacket(
            soundEvent,
            fabricCategory,
            entity,
            volume,
            pitch,
            player.getRandom().nextLong()
        ));
    }

    @Override
    public void playSound(AudioListener listener, SoundLocation location, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        ServerWorld world = this.server.getWorld(RegistryKey.of(RegistryKeys.WORLD, Identifier.of(location.world())));
        if (world == null) {
            return;
        }

        ServerPlayerEntity player = findPlayer(listener.uuid());
        if (player == null || player.isRemoved()) {
            return;
        }

        RegistryEntry.Reference<SoundEvent> soundEvent = Registries.SOUND_EVENT.getEntry(Identifier.of(sound)).orElse(null);
        if (soundEvent == null) {
            return;
        }

        Vec3d position = FabricConverter.convert(location);
        SoundCategory fabricCategory = FabricConverter.convert(category);

        player.networkHandler.sendPacket(new PlaySoundS2CPacket(
            soundEvent,
            fabricCategory,
            position.getX(),
            position.getY(),
            position.getZ(),
            volume,
            pitch,
            player.getRandom().nextLong()
        ));
    }

    private @Nullable ServerPlayerEntity findPlayer(UUID uuid) {
        Entity entity = entityCache.getIfPresent(uuid);
        if (!(entity instanceof ServerPlayerEntity player) || player.isRemoved()) {
            ServerPlayerEntity player = this.server.getPlayerManager().getPlayer(uuid);
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
            for (ServerWorld world : this.server.getWorlds()) {
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
