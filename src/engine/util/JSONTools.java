package engine.util;

import java.util.Map;

public class JSONTools {

    public static String getString(Map<?, ?> map, String key) {
        return (String) map.get(key);
    }

    public static int getInt(Map<?, ?> map, String key) {
        return (Integer) map.get(key);
    }

    public static float getFloat(Map<?, ?> map, String key) {
        return (Float) map.get(key);
    }

}
