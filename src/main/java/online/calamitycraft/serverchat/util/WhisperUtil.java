package online.calamitycraft.serverchat.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.FontRenderer;
import net.minecraft.core.net.command.TextFormatting;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WhisperUtil {

    private Map<String, String> replyMap = new HashMap<>();
    private Map<String, String> lastMap = new HashMap<>();

    private static char[] smallChars = new char[]{'l', 'j', ',', '.', '\'', '\"', '[', '{', ']', '}', '!'};
    private final static int normalCharWidth = 11;
    private final static int smallCharWidth = 6;
    public final static int maxWidth = 633;

    public static int[] getStringWidth(String s) {
        int count = 0;
        int wrapPosition = -1;
        for (int i = 0; i < s.toCharArray().length; i++) {
            char c = s.toCharArray()[i];
            if (c == '\247') continue;
            boolean flag = false;
            for (char smallChar : smallChars) {
                if (c == smallChar) {
                    flag = true;
                    break;
                }
            }
            count += flag ? smallCharWidth : normalCharWidth;
            if (count >= maxWidth) {
                wrapPosition = i - 1;
            }
        }
        return new int[]{count, wrapPosition};
    }

    public static List<String> slice(String s) {
        int count = 0;
        int wrapPosition = 0;
        String lastColour = "\247r";
        List<String> list = new ArrayList<>();
        if (getStringWidth(s)[1] == -1) {
            list.add(s);
            return list; // does not need slicing
        }
        char[] arr = s.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            char c = arr[i];
            if (i + 1 < arr.length) {
                String col = c + String.valueOf(arr[i + 1]);
                if (col.matches("§\\S")) {
                    lastColour = col;
                    continue;
                }
            }
            boolean flag = false;
            for (char smallChar : smallChars) {
                if (c == smallChar) {
                    flag = true;
                    break;
                }
            }
            count += flag ? smallCharWidth : normalCharWidth;
            if (count >= maxWidth) {
                list.add(lastColour + s.substring(wrapPosition, i - 1));
                wrapPosition = i - 1;
                count = 0;
            }
        }
        if (s.length() > wrapPosition) list.add(lastColour + s.substring(wrapPosition));
        return list;
    }

}
