package org.lushplugins.nbsminecraft.platform.fabric.utils;

import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.NotNull;
import org.lushplugins.nbsminecraft.utils.AudioListener;

public class FabricAudioListener extends AudioListener {

    public FabricAudioListener(PlayerEntity player, float volume) {
        super(player.getId(), player.getUuid(), volume);
    }

    public FabricAudioListener(@NotNull PlayerEntity player) {
        super(player.getId(), player.getUuid());
    }
}
