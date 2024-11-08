package org.lushplugins.nbsjava.platform.bukkit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.nbsjava.platform.AbstractPlatform;
import org.lushplugins.nbsjava.platform.bukkit.utils.BukkitConverter;
import org.lushplugins.nbsjava.utils.AudioListener;
import org.lushplugins.nbsjava.utils.EntityReference;
import org.lushplugins.nbsjava.utils.SoundLocation;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class BukkitPlatform extends AbstractPlatform {
    private final Cache<UUID, Entity> ENTITY_CACHE = CacheBuilder.newBuilder()
        .expireAfterAccess(60, TimeUnit.SECONDS)
        .build();

    @Override
    public void playSound(AudioListener listener, String sound, org.lushplugins.nbsjava.utils.SoundCategory category, float volume, float pitch) {
        if (!(findEntity(listener.uuid()) instanceof Player player)) {
            return;
        }

        playSound(player, player, sound, category, volume, pitch);
    }

    @Override
    public void playSound(AudioListener listener, EntityReference entityReference, String sound, org.lushplugins.nbsjava.utils.SoundCategory category, float volume, float pitch) {
        if (!(findEntity(listener.uuid()) instanceof Player player)) {
            return;
        }

        Entity entity = findEntity(listener.uuid());
        if (entity == null) {
            return;
        }

        playSound(player, entity, sound, category, volume, pitch);
    }

    private void playSound(Player player, Entity entity, String sound, org.lushplugins.nbsjava.utils.SoundCategory category, float volume, float pitch) {
        SoundCategory bukkitCategory = BukkitConverter.convert(category);

        player.playSound(entity, sound, bukkitCategory, volume, pitch);
    }

    @Override
    public void playSound(AudioListener listener, SoundLocation location, String sound, org.lushplugins.nbsjava.utils.SoundCategory category, float volume, float pitch) {
        World world = Bukkit.getWorld(location.world());
        if (world == null) {
            return;
        }

        if (!(Bukkit.getEntity(listener.uuid()) instanceof Player player)) {
            return;
        }

        Location bukkitLocation = BukkitConverter.convert(location);
        SoundCategory bukkitCategory = BukkitConverter.convert(category);

        player.playSound(bukkitLocation, sound, bukkitCategory, volume, pitch);
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
