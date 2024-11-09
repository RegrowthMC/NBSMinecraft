package org.lushplugins.nbsminecraft.platform.allay;

import org.allaymc.api.entity.interfaces.EntityPlayer;
import org.allaymc.api.server.Server;
import org.cloudburstmc.math.vector.Vector3f;
import org.cloudburstmc.protocol.bedrock.packet.PlaySoundPacket;
import org.joml.Vector3fc;
import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.EntityReference;
import org.lushplugins.nbsminecraft.utils.SoundCategory;
import org.lushplugins.nbsminecraft.utils.SoundLocation;
import org.lushplugins.nbsminecraft.platform.allay.utils.AllayConverter;

public class AllayPlatform extends AbstractPlatform {

    @Override
    public void playSound(AudioListener listener, String sound, SoundCategory category, float volume, float pitch) {
        EntityPlayer player = Server.getInstance().getOnlinePlayers().get(listener.uuid());
        if (player == null) {
            return;
        }

        Vector3fc vector = player.getLocation();
        Vector3f position = Vector3f.from(vector.x(), vector.y(), vector.z());

        playSound(player, position, sound, volume, pitch);
    }

    @Override
    public void playSound(AudioListener listener, EntityReference entityReference, String sound, SoundCategory category, float volume, float pitch) {
        EntityPlayer player = Server.getInstance().getOnlinePlayers().get(listener.uuid());
        if (player == null) {
            return;
        }

        // TODO: get entity here

        Vector3fc vector = player.getLocation();
        Vector3f position = Vector3f.from(vector.x(), vector.y(), vector.z());

        playSound(player, position, sound, volume, pitch);
    }

    @Override
    public void playSound(AudioListener listener, SoundLocation location, String sound, SoundCategory category, float volume, float pitch) {
        EntityPlayer player = Server.getInstance().getOnlinePlayers().get(listener.uuid());
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
}
