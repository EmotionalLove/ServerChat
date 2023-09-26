package online.calamitycraft.serverchat.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Threads {

    public static ExecutorService deathMessageExecutor;
    public static ExecutorService announcementExecutor;

    public static void init() {
        deathMessageExecutor = Executors.newSingleThreadExecutor();
        announcementExecutor = Executors.newSingleThreadExecutor();
    }
}
