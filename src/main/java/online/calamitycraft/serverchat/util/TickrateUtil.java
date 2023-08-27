package online.calamitycraft.serverchat.util;

import java.util.LinkedList;
import java.util.Set;

public class TickrateUtil {

    private long tick = -1L;
    private long prevMilli = -1L;
    private final LinkedList<Float> averageTickrates = new LinkedList<>();

    public float getTps() {
        return Math.min(((float) 1000 / tick), 20f);
    }

    public void measure() {
        tick = System.currentTimeMillis() - prevMilli;
        prevMilli = System.currentTimeMillis();
        record(getTps());
    }

    private void record(float tps) {
        averageTickrates.offer(tps);
        if (averageTickrates.size() >= 1200) {
            averageTickrates.remove();
        }
    }

    public double getAverageTps() {
        double totalTps = 0d;
        for (Float averageTickrate : averageTickrates) {
            totalTps += averageTickrate;
        }
        return totalTps / averageTickrates.size();
    }
}
