package engine.util;

import engine.math.Vector3f;

import java.util.Map;

public class JsonHandler {

    public static String getString(Map<?, ?> map, String key) {
        return (String) map.get(key);
    }

    public static int getInt(Map<?, ?> map, String key) {
        return (Integer) map.get(key);
    }

    public static float getFloat(Map<?, ?> map, String key) {
        return (Float) map.get(key);
    }

    @SuppressWarnings("unchecked")
    public static Vector3f getVector3f(Map<String, ?> map, String path) {
        Map<String, Double> vectorMap = ((Map<String, Map<String, Double>>) map).get(path);
        return new Vector3f(vectorMap.get("x").floatValue(), vectorMap.get("y").floatValue(), vectorMap.get("z").floatValue());
    }

    public static Vector3f getFlipVector3f(Map<String, ?> map, String path) {
        Map<String, Double> vectorMap = ((Map<String, Map<String, Double>>) map).get(path);
        return new Vector3f(-vectorMap.get("x").floatValue(), vectorMap.get("y").floatValue(), -vectorMap.get("z").floatValue());
    }
}
