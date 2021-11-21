package engine.map;

import com.google.gson.Gson;
import engine.map.components.BlockComponent;
import engine.map.components.Component;
import engine.map.components.RampComponent;
import engine.math.Vector2f;
import engine.math.Vector3f;
import engine.util.JsonHandler;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapLevel {

    public static List<Component> components = new ArrayList<>();
    private static Vector3f redSpawn, blueSpawn;

    @SuppressWarnings("unchecked")
    public static void loadMap(String map) {
        components.clear();

        Gson gson = new Gson();
        Map<?, ?> mapData = new HashMap<>();
        try {
            Reader reader = Files.newBufferedReader(Paths.get("data/maps/" + map + "/map.json"));
            mapData = gson.fromJson(reader, Map.class);
        } catch(IOException e) {
            e.printStackTrace();
        }

        try {
            List<Map<String, ?>> componentsList = ((Map<String, List<Map<String, ?>>>) mapData.get("layout")).get("components");
            for (Map<String, ?> component : componentsList) {

                String type = (String) component.get("type");
                String materialName = (String) component.get("material");
                boolean visible = (Boolean) component.get("visible");

                if (type.equals("block") || type.equals("ramp")) {

                    Map<String, ?> colliderData = (Map<String, ?>) component.get("colliderData");

                    Vector3f a = JsonHandler.getVector3f(colliderData, "a");
                    Vector3f b = JsonHandler.getVector3f(colliderData, "b");
                    Vector3f c = JsonHandler.getVector3f(colliderData, "c");
                    float height = ((Double) colliderData.get("height")).floatValue(), tiling = ((Double) colliderData.get("tiling")).floatValue();
                    boolean collidable = (Boolean) colliderData.get("collidable");

                    if (type.equals("block")) {

                        BlockComponent newComponent = new BlockComponent(a, b, c, height, tiling, visible, collidable);
                        components.add(newComponent);

                    } else {

                        int direction = ((Double) colliderData.get("direction")).intValue();

                        RampComponent newComponent = new RampComponent(a, b, c, height, direction, tiling, visible, collidable);
                        components.add(newComponent);

                    }
                }
            }

            Map<String, ?> multiplayer = (Map<String, ?>) mapData.get("multiplayer");
            redSpawn = JsonHandler.getVector3f(multiplayer, "redSpawn");
            blueSpawn = JsonHandler.getVector3f(multiplayer, "blueSpawn");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Vector3f getRedSpawn() {
        return redSpawn;
    }

    public static Vector3f getBlueSpawn() {
        return blueSpawn;
    }
}
