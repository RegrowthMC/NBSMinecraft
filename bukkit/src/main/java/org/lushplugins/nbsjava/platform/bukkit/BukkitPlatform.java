package org.lushplugins.nbsjava.platform.bukkit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.lushplugins.nbsjava.platform.AbstractPlatform;
import org.lushplugins.nbsjava.platform.bukkit.utils.BukkitConverter;
import org.lushplugins.nbsjava.utils.SoundLocation;

import java.util.concurrent.TimeUnit;

public class BukkitPlatform extends AbstractPlatform {
    private final Cache<Integer, Entity> ENTITY_CACHE = CacheBuilder.newBuilder()
        .expireAfterAccess(3, TimeUnit.MINUTES)
        .build();

    @Override
    public void playSound(int viewerId, String sound, org.lushplugins.nbsjava.utils.SoundCategory category, float volume, float pitch) {
        // TODO: Get player from entityId
        if (!(Bukkit.getEntity(entityUUID) instanceof Player player)) {
            return;
        }

        SoundCategory bukkitCategory = BukkitConverter.convert(category);

        player.playSound(player, sound, bukkitCategory, volume, pitch);
    }

    @Override
    public void playSound(int viewerId, SoundLocation location, String sound, org.lushplugins.nbsjava.utils.SoundCategory category, float volume, float pitch) {
        World world = Bukkit.getWorld(location.world());
        if (world == null) {
            return;
        }

        // TODO: Get player from entityId
        if (!(Bukkit.getEntity(entityUUID) instanceof Player player)) {
            return;
        }

        Location bukkitLocation = BukkitConverter.convert(location);
        SoundCategory bukkitCategory = BukkitConverter.convert(category);

        player.playSound(bukkitLocation, sound, bukkitCategory, volume, pitch);
    }
}
