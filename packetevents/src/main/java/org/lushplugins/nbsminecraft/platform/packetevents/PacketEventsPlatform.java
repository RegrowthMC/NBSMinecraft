package org.lushplugins.nbsminecraft.platform.packetevents;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import com.github.retrooper.packetevents.protocol.player.User;
import com.github.retrooper.packetevents.protocol.sound.Sound;
import com.github.retrooper.packetevents.protocol.sound.SoundCategory;
import com.github.retrooper.packetevents.protocol.sound.StaticSound;
import com.github.retrooper.packetevents.resources.ResourceLocation;
import com.github.retrooper.packetevents.util.Vector3i;
import com.github.retrooper.packetevents.wrapper.PacketWrapper;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntitySoundEffect;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSoundEffect;
import org.jetbrains.annotations.Nullable;
import org.lushplugins.nbsminecraft.platform.AbstractPlatform;
import org.lushplugins.nbsminecraft.platform.packetevents.utils.PacketEventsConverter;
import org.lushplugins.nbsminecraft.utils.AudioListener;
import org.lushplugins.nbsminecraft.utils.EntityReference;
import org.lushplugins.nbsminecraft.utils.SoundLocation;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PacketEventsPlatform extends AbstractPlatform {
    public static final PacketEventsPlatform INSTANCE = new PacketEventsPlatform();

    private final Cache<UUID, User> userCache = Caffeine.newBuilder()
        .expireAfterAccess(5, TimeUnit.SECONDS)
        .build();

    @Override
    public void playSound(AudioListener listener, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        playSound(listener, listener, sound, category, volume, pitch);
    }

    @Override
    public void playSound(AudioListener listener, EntityReference entityReference, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        User user = findUser(listener);
        if (user == null) {
            return;
        }

        Sound peSound = new StaticSound(new ResourceLocation(ResourceLocation.normString(sound)), null);
        SoundCategory peCategory = PacketEventsConverter.convert(category);

        PacketWrapper<?> packet = new WrapperPlayServerEntitySoundEffect(peSound, peCategory, entityReference.entityId(), volume, pitch);
        user.sendPacket(packet);
    }

    @Override
    public void playSound(AudioListener listener, SoundLocation location, String sound, org.lushplugins.nbsminecraft.utils.SoundCategory category, float volume, float pitch) {
        User user = findUser(listener);
        if (user == null) {
            return;
        }

        Vector3i position = PacketEventsConverter.convert(location);
        Sound peSound = new StaticSound(new ResourceLocation(ResourceLocation.normString(sound)), null);
        SoundCategory peCategory = PacketEventsConverter.convert(category);

        PacketWrapper<?> packet = new WrapperPlayServerSoundEffect(peSound, peCategory, position, volume, pitch);
        user.sendPacket(packet);
    }

    private @Nullable User findUser(AudioListener listener) {
        User user = userCache.getIfPresent(listener.uuid());
        if (user == null || !ChannelHelper.isOpen(user.getChannel())) {
            user = PacketEvents.getAPI().getProtocolManager().getUsers().stream()
                .filter(foundUser -> foundUser.getEntityId() == listener.entityId() && foundUser.getUUID() == listener.uuid())
                .findFirst()
                .orElse(null);

            if (user != null) {
                userCache.put(listener.uuid(), user);
            }
        }

        return user;
    }

    @Override
    public void invalidateIfCached(AudioListener listener) {
        userCache.invalidate(listener.uuid());
    }
}
