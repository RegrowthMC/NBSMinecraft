package org.lushplugins.nbsminecraft.platform.bukkit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.platform.bukkit.utils.BukkitConverter;
import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.EntityReference;
import org.lushplugins.nbsminecraft.utils.SoundLocation;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class BukkitPlatform extends AbstractPlatform {
    private final Cache<UUID, Entity> ENTITY_CACHE = CacheBuilder.newBuilder()
        .expireAfterAccess(60, TimeUnit.SECONDS)
        .build();

    @Override
    public void playSound(AudioListener listener, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        if (!(findPlayer(listener.uuid()) instanceof Player player) || !player.isValid()) {
            return;
        }

        playSound(player, player, sound, category, volume, pitch);
    }

    @Override
    public void playSound(AudioListener listener, EntityReference entityReference, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        if (!(findPlayer(listener.uuid()) instanceof Player player) || !player.isValid()) {
            return;
        }

        Entity entity = findEntity(listener.uuid());
        if (entity == null) {
            return;
        }

        playSound(player, entity, sound, category, volume, pitch);
    }

    private void playSound(Player player, Entity entity, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        SoundCategory bukkitCategory = BukkitConverter.convert(category);

        player.playSound(entity, sound, bukkitCategory, volume, pitch);
    }

    @Override
    public void playSound(AudioListener listener, SoundLocation location, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        World world = Bukkit.getWorld(location.world());
        if (world == null) {
            return;
        }

        if (!(findPlayer(listener.uuid()) instanceof Player player) || !player.isValid()) {
            return;
        }

        Location bukkitLocation = BukkitConverter.convert(location);
        SoundCategory bukkitCategory = BukkitConverter.convert(category);

        player.playSound(bukkitLocation, sound, bukkitCategory, volume, pitch);
    }

    private @Nullable Player findPlayer(UUID uuid) {
        try {
            if (ENTITY_CACHE.get(uuid, () -> Bukkit.getPlayer(uuid)) instanceof Player player) {
                return player;
            } else {
                return null;
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    private @Nullable Entity findEntity(UUID uuid) {
        try {
            return ENTITY_CACHE.get(uuid, () -> Bukkit.getEntity(uuid));
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }
}
