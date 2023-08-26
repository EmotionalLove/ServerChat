package online.calamitycraft.serverchat.util;

import java.util.LinkedList;
import java.util.Set;

public class TickrateUtil {

    private static long tick = -1L;
    private static long prevMilli = -1L;
    private static final LinkedList<Float> averageTickrates = new LinkedList<>();

    public static float getTps() {
        return Math.min(((float) 1000 / tick), 20f);
    }

    public static void measure() {
        tick = System.currentTimeMillis() - prevMilli;
        prevMilli = System.currentTimeMillis();
        record(getTps());
    }

    private static void record(float tps) {
        averageTickrates.offer(tps);
        if (averageTickrates.size() >= 1200) {
            averageTickrates.remove();
        }
    }

    public static double getAverageTps() {
        double totalTps = 0d;
        for (Float averageTickrate : averageTickrates) {
            totalTps += averageTickrate;
        }
        return totalTps / averageTickrates.size();
    }
}
