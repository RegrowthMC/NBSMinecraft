package org.lushplugins.nbsminecraft.platform.packetevents;

import org.lushplugins.nbsminecraft.NBSAPI;

public class PacketEventsNBSAPI extends NBSAPI {
    public static final PacketEventsNBSAPI INSTANCE = new PacketEventsNBSAPI();

    public PacketEventsNBSAPI() {
        super(new PacketEventsPlatform());
    }
}
