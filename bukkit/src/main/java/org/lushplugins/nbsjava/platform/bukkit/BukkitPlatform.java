package org.lushplugins.nbsjava.platform.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.lushplugins.nbsjava.platform.AbstractPlatform;
import org.lushplugins.nbsjava.platform.bukkit.utils.BukkitConverter;
import org.lushplugins.nbsjava.utils.SoundLocation;

import java.util.UUID;

public class BukkitPlatform extends AbstractPlatform {

    @Override
    public void playSound(UUID entityUUID, String sound, org.lushplugins.nbsjava.utils.SoundCategory category, float volume, float pitch) {
        if (!(Bukkit.getEntity(entityUUID) instanceof Player player)) {
            return;
        }

        SoundCategory bukkitCategory = BukkitConverter.convert(category);

        player.playSound(player, sound, bukkitCategory, volume, pitch);
    }

    @Override
    public void playSound(UUID entityUUID, SoundLocation location, String sound, org.lushplugins.nbsjava.utils.SoundCategory category, float volume, float pitch) {
        World world = Bukkit.getWorld(location.world());
        if (world == null) {
            return;
        }

        if (!(Bukkit.getEntity(entityUUID) instanceof Player player)) {
            return;
        }

        Location bukkitLocation = BukkitConverter.convert(location);
        SoundCategory bukkitCategory = BukkitConverter.convert(category);

        player.playSound(bukkitLocation, sound, bukkitCategory, volume, pitch);
    }
}
