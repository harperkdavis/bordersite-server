package engine.map;

import com.google.gson.Gson;
import engine.map.components.BlockComponent;
import engine.map.components.Component;
import engine.map.components.RampComponent;
import engine.math.Vector3f;

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
    private static final String MAP = "atnitz";

    @SuppressWarnings("unchecked")
    public static void loadMap() {
        components.clear();

        Gson gson = new Gson();
        Map<?, ?> layoutMap = new HashMap<>();
        try {
            Reader reader = Files.newBufferedReader(Paths.get("resources/maps/" + MAP + "/layout.json"));
            layoutMap = gson.fromJson(reader, Map.class);
        } catch(IOException e) {
            e.printStackTrace();
        }

        try {
            List<Map<String, ?>> componentsList = (List<Map<String, ?>>) layoutMap.get("components");
            for (Map<String, ?> component : componentsList) {
                String type = (String) component.get("type");
                ArrayList<Double> doubleVal = (ArrayList<Double>) component.get("values");
                ArrayList<Float> val = new ArrayList<>();
                for (Double d : doubleVal) {
                    val.add(d.floatValue());
                }
                if (type.equals("block")) {

                    Vector3f a = new Vector3f(val.get(0), val.get(1), val.get(2));
                    Vector3f b = new Vector3f(val.get(3), val.get(4), val.get(5));
                    Vector3f c = new Vector3f(val.get(6), val.get(7), val.get(8));

                    float height = val.get(9), tiling = val.get(10);
                    boolean mesh = (val.get(11) == 1), collision = (val.get(12) == 1);

                    BlockComponent newComponent = new BlockComponent(a, b, c, height, tiling, mesh, collision);
                    components.add(newComponent);

                } else if (type.equals("ramp")) {

                    Vector3f a = new Vector3f(val.get(0), val.get(1), val.get(2));
                    Vector3f b = new Vector3f(val.get(3), val.get(4), val.get(5));
                    Vector3f c = new Vector3f(val.get(6), val.get(7), val.get(8));

                    float height = val.get(9), tiling = val.get(10);
                    boolean mesh = (val.get(11) == 1), collision = (val.get(12) == 1);
                    int direction = val.get(13).intValue();

                    RampComponent newComponent = new RampComponent(a, b, c, height, direction, tiling, mesh, collision);
                    components.add(newComponent);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<?, ?> multiplayerMap = new HashMap<>();
        try {
            Reader reader = Files.newBufferedReader(Paths.get("resources/maps/" + MAP + "/multiplayer.json"));
            multiplayerMap = gson.fromJson(reader, Map.class);
        } catch(IOException e) {
            e.printStackTrace();
        }

        ArrayList<Double> redSpawnArray = (ArrayList<Double>) multiplayerMap.get("red_spawn");
        ArrayList<Double> blueSpawnArray = (ArrayList<Double>) multiplayerMap.get("blue_spawn");

        redSpawn = new Vector3f(redSpawnArray.get(0).floatValue(), redSpawnArray.get(1).floatValue(), redSpawnArray.get(2).floatValue());
        blueSpawn = new Vector3f(blueSpawnArray.get(0).floatValue(), blueSpawnArray.get(1).floatValue(), blueSpawnArray.get(2).floatValue());

    }

    public static Vector3f getRedSpawn() {
        return redSpawn;
    }

    public static Vector3f getBlueSpawn() {
        return blueSpawn;
    }
}
