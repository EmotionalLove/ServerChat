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

    private static final char[] smallChars = new char[]{'l', 'j', ',', '.', '\'', '\"', '[', '{', ']', '}', '!'};
    private final static int largeCharWidth = 14;
    private final static int normalCharWidth = 11;
    private final static int smallCharWidth = 6;
    public final static int maxWidth = 633;

    /**
     * This method will analyse `String s`, and always return an array with a length of 2
     * This method is used to determine whether the given String will fit in the vanilla Minecraft chat UI without wrapping.
     * @param s The string to be analysed
     * @return [0] will be the length in pixels of the given String, [1] will be the character the String needs to be split at to avoid wrapping.
     */
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
            if (Character.isUpperCase(c)) {
                count += (largeCharWidth - normalCharWidth);
            }
            if (count >= maxWidth) {
                wrapPosition = i - 1;
            }
        }
        return new int[]{count, wrapPosition};
    }

    /**
     * This method will take the given String, and automatically split and preserve Chat Colors.
     * This method will help you avoid client-side word wrapping, which resets the Chat Colors on every line.
     * Splitting a message into multiple lines can help you avoid this.
     * @param s The string to split
     * @return a list, in order, of the lines you need to send to the player.
     */
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
