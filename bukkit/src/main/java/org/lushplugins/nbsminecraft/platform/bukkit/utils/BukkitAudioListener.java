package org.lushplugins.nbsminecraft.platform.bukkit.utils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.nbsminecraft.utils.AudioListener;

public class BukkitAudioListener extends AudioListener {

    public BukkitAudioListener(Player player, float volume) {
        super(player.getEntityId(), player.getUniqueId(), volume);
    }

    public BukkitAudioListener(@NotNull Player player) {
        super(player.getEntityId(), player.getUniqueId());
    }
}
