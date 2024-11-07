package org.lushplugins.nbsjava.platform.bukkit;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.SoundCategory;
import com.github.retrooper.packetevents.protocol.sound.StaticSound;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntitySoundEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSoundEffect;
import org.lushplugins.nbsjava.platform.AbstractPlatform;
import org.lushplugins.nbsjava.platform.bukkit.utils.PacketEventsConverter;
import org.lushplugins.nbsjava.utils.SoundLocation;

import java.util.UUID;

public class PacketEventsPlatform extends AbstractPlatform {

    @Override
    public void playSound(UUID entityUUID, String sound, org.lushplugins.nbsjava.utils.SoundCategory category, float volume, float pitch) {
        User user = getUser(entityUUID);
        if (user == null) {
            return;
        }

        Sound peSound = new StaticSound(new ResourceLocation(ResourceLocation.normString(sound)), null);
        SoundCategory peCategory = PacketEventsConverter.convert(category);

        PacketWrapper<?> packet = new WrapperPlayServerEntitySoundEffect(peSound, peCategory, user.getEntityId(), volume, pitch);
        user.sendPacket(packet);
    }

    @Override
    public void playSound(UUID entityUUID, SoundLocation location, String sound, org.lushplugins.nbsjava.utils.SoundCategory category, float volume, float pitch) {
        User user = getUser(entityUUID);
        if (user == null) {
            return;
        }

        Vector3i position = PacketEventsConverter.convert(location);
        Sound peSound = new StaticSound(new ResourceLocation(ResourceLocation.normString(sound)), null);
        SoundCategory peCategory = PacketEventsConverter.convert(category);

        PacketWrapper<?> packet = new WrapperPlayServerSoundEffect(peSound, peCategory, position, volume, pitch);
        user.sendPacket(packet);
    }

    private User getUser(UUID entityUUID) {
        return PacketEvents.getAPI().getProtocolManager().getUsers().stream()
            .filter(user -> user.getEntityId() != -1 && user.getUUID() == entityUUID)
            .findFirst()
            .orElse(null);
    }
}
