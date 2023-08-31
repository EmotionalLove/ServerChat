package online.calamitycraft.serverchat.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigUtil {

    public static final String CONFIG_FILE_NAME = "chatconfig.yml";

    private Map<String, Object> configMap = new HashMap<>();

    {
        configMap.put("allow-greentext", true);
        configMap.put("allow-joinleave-msgs", true);
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
            for (String s : configMap.keySet()) {
                Object val = parser.get(s);
                if (val == null) continue;
                configMap.put(s, val);
            }
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



}
