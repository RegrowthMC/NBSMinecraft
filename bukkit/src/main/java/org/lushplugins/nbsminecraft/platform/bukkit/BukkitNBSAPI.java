package org.lushplugins.nbsminecraft.platform.bukkit;

import org.lushplugins.nbsminecraft.NBSAPI;

public class BukkitNBSAPI extends NBSAPI {
    public static final BukkitNBSAPI INSTANCE = new BukkitNBSAPI();

    public BukkitNBSAPI() {
        super(new BukkitPlatform());
    }
}
