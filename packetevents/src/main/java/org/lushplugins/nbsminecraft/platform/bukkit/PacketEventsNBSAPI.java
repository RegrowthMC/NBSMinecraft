package org.lushplugins.nbsminecraft.platform.bukkit;

import org.lushplugins.nbsminecraft.NBSAPI;

public class PacketEventsNBSAPI extends NBSAPI {
    public static final PacketEventsNBSAPI INSTANCE = new PacketEventsNBSAPI();

    public PacketEventsNBSAPI() {
        super(new PacketEventsPlatform());
    }
}
