package org.lushplugins.nbsjava.platform.bukkit;

import org.lushplugins.nbsjava.NBSAPI;

public class PacketEventsNBSAPI extends NBSAPI {
    public static final PacketEventsNBSAPI INSTANCE = new PacketEventsNBSAPI();

    public PacketEventsNBSAPI() {
        super(new PacketEventsPlatform());
    }
}
