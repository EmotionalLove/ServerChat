package online.calamitycraft.serverchat.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Threads {

    public static ExecutorService deathMessageExecutor;

    public static void init() {
        deathMessageExecutor = Executors.newSingleThreadExecutor();
    }
}
