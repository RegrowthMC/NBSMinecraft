package org.lushplugins.nbsminecraft.platform.neoforge.utils;

import net.minecraft.world.entity.player.Player;
import org.lushplugins.nbsminecraft.utils.AudioListener;

public class NeoForgeAudioListener extends AudioListener {

    public NeoForgeAudioListener(Player player, float volume) {
        super(player.getId(), player.getUUID(), volume);
    }

    public NeoForgeAudioListener(Player player) {
        super(player.getId(), player.getUUID());
    }
}
