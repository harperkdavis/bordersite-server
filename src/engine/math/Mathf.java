package engine.math;

import main.Main;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
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

    public static List<Vector2f> circleLinePoints(Vector2f pointA, Vector2f pointB, Vector2f center, float radius) {
        float baX = pointB.getX() - pointA.getX();
        float baY = pointB.getY() - pointA.getY();
        float caX = center.getX() - pointA.getX();
        float caY = center.getY() - pointA.getY();

        float a = baX * baX + baY * baY;
        float bBy2 = baX * caX + baY * caY;
        float c = caX * caX + caY * caY - radius * radius;

        float pBy2 = bBy2 / a;
        float q = c / a;

        float disc = pBy2 * pBy2 - q;
        if (disc < 0) {
            return Collections.emptyList();
        }

        float tmpSqrt = (float) Math.sqrt(disc);
        float abScalingFactor1 = -pBy2 + tmpSqrt;
        float abScalingFactor2 = -pBy2 - tmpSqrt;

        Vector2f p1 = new Vector2f(pointA.getX() - baX * abScalingFactor1, pointA.getY() - baY * abScalingFactor1);
        if (disc == 0) {
            return Collections.singletonList(p1);
        }
        Vector2f p2 = new Vector2f(pointA.getX() - baX * abScalingFactor2, pointA.getY() - baY * abScalingFactor2);
        return Arrays.asList(p1, p2);
    }

    public static Vector2f circleLineAverage(Vector2f pointA, Vector2f pointB, Vector2f center, float radius) {
        List<Vector2f> points = circleLinePoints(pointA, pointB, center, radius);
        if (points.size() == 0) {
            return null;
        } else if (points.size() == 1) {
            return points.get(0);
        }
        return new Vector2f((points.get(0).getX() + points.get(1).getX()) / 2.0f, (points.get(0).getY() + points.get(1).getY()) / 2.0f);
    }

    public static Vector3f rayCylinder(Vector3f pointA, Vector3f pointB, Vector3f position, float radius, float height) {
        Vector2f a2 = new Vector2f(pointA.getX(), pointA.getZ());
        Vector2f b2 = new Vector2f(pointB.getX(), pointB.getZ());
        Vector2f intersectPoint = circleLineAverage(a2, b2, new Vector2f(position.getX(), position.getZ()), radius);

        if (intersectPoint == null) {
            return null;
        }

        float maxDistance = Vector2f.distance(a2, b2);
        float distanceA = Vector2f.distance(a2, intersectPoint);
        float distanceB = Vector2f.distance(b2, intersectPoint);

        float distance = ((distanceA / maxDistance) + (1 - (distanceB / maxDistance))) / 2;
        float intersectHeight = lerp(pointA.getY(), pointB.getY(), distance);

        if (intersectHeight < position.getY() || intersectHeight > position.getY() + height) {
            return null;
        }

        return new Vector3f(intersectPoint.getX(), intersectHeight, intersectPoint.getY());
    }

}
