package org.lushplugins.nbsjava;

import cz.koca2000.nbs4j.Song;
import org.lushplugins.nbsjava.platform.AbstractPlatform;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class NBSAPI {
    private static final ScheduledExecutorService threads = Executors.newScheduledThreadPool(1);

    private final AbstractPlatform platform;

    public NBSAPI(AbstractPlatform platform) {
        this.platform = platform;
    }

    public Song readNBSFile(File file) {
        try {
            return Song.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Song readNBSInputStream(InputStream inputStream) {
        return Song.fromStream(inputStream);
    }
}
