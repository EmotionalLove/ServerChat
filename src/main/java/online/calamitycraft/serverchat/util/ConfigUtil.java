package online.calamitycraft.serverchat.util;

import net.minecraft.core.block.Block;
import net.minecraft.core.item.Item;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class ConfigUtil {

    public static final String CONFIG_FILE_NAME = "chatconfig.yml";
    public static final Cache CACHE = new Cache();

    private Map<String, Object> configMap = new HashMap<>();

    {
        configMap.put("allow-greentext", true);
        configMap.put("allow-joinleave-msgs", true);
        configMap.put("obscure-kick-reason", true);
        configMap.put("defer-entity-updates", false);
        configMap.put("clump-radius-x", 3);
        configMap.put("clump-radius-y", 2);
        configMap.put("clump-radius-z", 3);
        configMap.put("illegal-items", new int[]
                {Block.bedrock.id, //
                        Block.fluidWaterFlowing.id,
                        Block.fluidLavaFlowing.id,
                        Block.fluidWaterStill.id,
                        Block.fluidLavaStill.id,
                        Block.pistonHead.id,
                        Block.portalNether.id,
                        Block.portalParadise.id,
                        Block.bed.id,
                        Block.cropsWheat.id});
    }

    public ConfigUtil() throws IOException {
        File file = new File(CONFIG_FILE_NAME);
        boolean success;
        boolean newFile = false;
        if (!file.exists()) {
            success = file.createNewFile();
            newFile = true;
        } else success = true;
        if (!success) throw new IOException("Config file doesn't exist nor could be generated");
        YMLParser parser = new YMLParser(file);
        if (newFile) {
            configMap.forEach(parser::set);
            parser.save();
        } else {
            Set<String> notLoaded = new HashSet<>();
            for (String s : configMap.keySet()) {
                if (!parser.exists(s)) {
                    notLoaded.add(s);
                    continue;
                }
                Object val = parser.get(s);
                if (val == null) continue;
                configMap.put(s, val);
            }
            if (notLoaded.isEmpty()) return;
            for (String s : notLoaded) {
                parser.set(s, configMap.get(s));
            }
            parser.save();
        }
    }

    /**
     * TODO Make this less ghetto
     */
    public boolean isGreentextAllowed(boolean def) {
        Object val = configMap.getOrDefault("allow-greentext", def);
        if (val instanceof Boolean) return (boolean) val;
        else return def;
    }

    public boolean allowJoinLeaveMessages(boolean def) {
        Object val = configMap.getOrDefault("allow-joinleave-msgs", def);
        if (val instanceof Boolean) return (boolean) val;
        else return def;
    }

    public boolean obscureKickReason(boolean def) {
        Object val = configMap.getOrDefault("obscure-kick-reason", def);
        if (val instanceof Boolean) return (boolean) val;
        else return def;
    }


    public float getClumpX(float def) {
        if (CACHE.isClumpCached()) return CACHE.getClumpX();
        Object val = configMap.getOrDefault("clump-radius-x", def);
        if (val instanceof Number) {
            CACHE.setClumpX(((Number) val).floatValue());
            return ((Number) val).floatValue();
        } else {
            CACHE.setClumpX(def);
            return def;
        }
    }

    public float getClumpY(float def) {
        if (CACHE.isClumpCached()) return CACHE.getClumpY();
        Object val = configMap.getOrDefault("clump-radius-y", def);
        if ((val instanceof Float) || (val instanceof Integer) || (val instanceof Double)) {
            CACHE.setClumpY(((Number) val).floatValue());
            return ((Number) val).floatValue();
        } else {
            CACHE.setClumpY(def);
            return def;
        }
    }

    public float getClumpZ(float def) {
        if (CACHE.isClumpCached()) return CACHE.getClumpZ();
        Object val = configMap.getOrDefault("clump-radius-z", def);
        if ((val instanceof Float) || (val instanceof Integer) || (val instanceof Double)) {
            CACHE.setClumpZ(((Number) val).floatValue());
            return ((Number) val).floatValue();
        } else {
            CACHE.setClumpZ(def);
            return def;
        }
    }

    public int[] getIllegalArr(int[] def) {
        if (CACHE.isIllegalArrCached()) return CACHE.getIllegalArr();
        Object val = configMap.getOrDefault("illegal-items", def);
        if (val instanceof ArrayList<?>) {
            ArrayList<?> arrs = (ArrayList<?>) val;
            int[] arr = new int[arrs.size()];
            for (int i = 0; i < arrs.size(); i++) {
                arr[i] = (int) arrs.get(i);
            }
            CACHE.setIllegalArr(arr);
            return arr;
        } else {
            return def;
        }
    }

    public static class Cache {
        private float clumpX = Float.MIN_VALUE;
        private float clumpY = Float.MIN_VALUE;
        private float clumpZ = Float.MIN_VALUE;
        private int[] illegalArr = new int[]{-1};

        protected Cache() {

        }

        protected void setClumpX(float clumpX) {
            this.clumpX = clumpX;
        }

        protected void setClumpY(float clumpY) {
            this.clumpY = clumpY;
        }

        protected void setClumpZ(float clumpZ) {
            this.clumpZ = clumpZ;
        }

        public float getClumpX() {
            return clumpX;
        }

        public float getClumpY() {
            return clumpY;
        }

        public float getClumpZ() {
            return clumpZ;
        }

        public boolean isClumpCached() {
            return getClumpX() != Float.MIN_VALUE && getClumpY() != Float.MIN_VALUE && getClumpZ() != Float.MIN_VALUE;
        }

        protected void setIllegalArr(int[] illegalArr) {
            this.illegalArr = illegalArr;
        }

        public int[] getIllegalArr() {
            return illegalArr;
        }

        public boolean isIllegalArrCached() {
            return illegalArr[0] != -1;
        }
    }

}
