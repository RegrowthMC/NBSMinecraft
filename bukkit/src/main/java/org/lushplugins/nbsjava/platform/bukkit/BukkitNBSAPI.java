package org.lushplugins.nbsjava.platform.bukkit;

import org.lushplugins.nbsjava.NBSAPI;

public class BukkitNBSAPI extends NBSAPI {
    public static final BukkitNBSAPI INSTANCE = new BukkitNBSAPI();

    public BukkitNBSAPI() {
        super(new BukkitPlatform());
    }
}
