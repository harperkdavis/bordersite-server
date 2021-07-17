package engine.math;

import main.Main;

public class Mathf {

    public static float lerp(float a, float b, float f) {
        return a + f * (b - a);
    }

    public static float lerpdt(float a, float b, float f) {
        return lerp(a, b, (float) (1 - Math.pow(f / 10000.0f, Main.getDeltaTime())));
    }

    public static float clamp(float x, float min, float max) {
        return Math.max(Math.min(x, max), min);
    }

    public static boolean ccw(float ax, float ay, float bx, float by, float cx, float cy) {
        return (cy-ay) * (bx-ax) > (by-ay) * (cx-ax);
    }

    public static boolean intersect(float ax, float ay, float bx, float by, float cx, float cy, float dx, float dy) {
        return ccw(ax, ay, cx, cy, dx, dy) != ccw(bx, by, cx, cy, dx, dy) && ccw(ax, ay,bx, by, cx, cy) != ccw(ax, ay,bx, by, dx, dy);
    }

    public static Vector2f intersectPoint(float ax, float ay, float bx, float by, float cx, float cy, float dx, float dy) {
        float a1 = by - ay;
        float b1 = ax - bx;
        float c1 = a1 * (ax) + b1 * (ay);

        float a2 = dy - cy;
        float b2 = cx - dx;
        float c2 = a2 * (cx) + b2 * (cy);

        float determinant = a1 * b2 - a2 * b1;

        return new Vector2f((b2 * c1 - b1 * c2) / determinant, (a1 * c2 - a2 * c1) / determinant);
    }

    public static float distance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - y1, 2) + Math.pow(x2 - y2, 2));
    }

    public static boolean lineCircle(float x1, float y1, float x2, float y2, float cx, float cy, float r) {
        boolean inside1 = pointCircle(x1, y1, cx, cy, r);
        boolean inside2 = pointCircle(x2, y2, cx, cy, r);
        if (inside1 || inside2) return true;

        float distX = x1 - x2;
        float distY = y1 - y2;
        float len = (float) Math.sqrt((distX*distX) + (distY*distY));

        float dot = (((cx - x1)*(x2 - x1)) + ((cy - y1)*(y2 - y1))) / (len * len);

        float closestX = x1 + (dot * (x2 - x1));
        float closestY = y1 + (dot * (y2 - y1));

        boolean onSegment = linePoint(x1, y1, x2, y2, closestX, closestY);
        if (!onSegment) return false;

        distX = closestX - cx;
        distY = closestY - cy;
        float distance = (float) Math.sqrt((distX * distX) + (distY * distY));

        if (distance <= r) {
            return true;
        }
        return false;
    }

    public static boolean pointCircle(float px, float py, float cx, float cy, float r) {
        return distance(px, py, cx, cy) < r;
    }

    public static boolean linePoint(float x1, float y1, float x2, float y2, float px, float py) {
        float d1 = distance(px, py, x1, y1);
        float d2 = distance(px, py, x2, y2);

        float lineLen = distance(x1, y1, x2, y2);

        float buffer = 0.1f;

        if (d1+d2 >= lineLen-buffer && d1+d2 <= lineLen+buffer) {
            return true;
        }
        return false;
    }

    public static float pointLine(float x, float y, float x1, float y1, float x2, float y2) {
        float a = x - x1;
        float b = y - y1;
        float c = x2 - x1;
        float d = y2 - y1;
        float e = -d;
        float f = c;

        float dot = a * e + b * f;
        float len_sq = e * e + f * f;

        return  Math.abs(dot) / (float) Math.sqrt(len_sq);
    }

    public static boolean pointTrapezoid(Vector2f p, Vector2f a, Vector2f b, Vector2f c, Vector2f d) {
        return (Vector2f.cross(Vector2f.subtract(p, a), Vector2f.subtract(b, a)) * Vector2f.cross(Vector2f.subtract(p, d), Vector2f.subtract(c, d)) < 0
                && Vector2f.cross(Vector2f.subtract(p, a), Vector2f.subtract(d, a)) * Vector2f.cross(Vector2f.subtract(p, b), Vector2f.subtract(c, b)) < 0);
    }

    private static float side(Vector2f p1, Vector2f p2, Vector2f p) {
        return (p2.getY() - p1.getY()) * (p.getX() - p1.getX()) + (-p2.getX() + p1.getX()) * (p.getY() - p1.getY());
    }

    public static boolean pointTriangle(Vector2f p, Vector2f p1, Vector2f p2, Vector2f p3) {
        boolean checkSide1 = side(p1, p2, p) >= 0;
        boolean checkSide2 = side(p2, p3, p) >= 0;
        boolean checkSide3 = side(p3, p1, p) >= 0;
        return checkSide1 && checkSide2 && checkSide3;
    }
}
