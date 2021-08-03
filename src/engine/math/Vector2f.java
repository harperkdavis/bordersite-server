package engine.math;

public class Vector2f {

    public float x, y;

    public static Vector2f zero() { return new Vector2f(0, 0); }
    public static Vector2f one() { return new Vector2f(1, 1); }
    public static Vector2f oneX() { return new Vector2f(1, 0); }
    public static Vector2f oneY() { return new Vector2f(0, 1); }

    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f(Vector2f other) {
        this.x = other.getX();
        this.y = other.getY();
    }

    public Vector2f() {
        this(0, 0);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2f add(Vector2f a) {
        x += a.x;
        y += a.y;
        return this;
    }

    public Vector2f add(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    public Vector2f plus(Vector2f a) {
        return new Vector2f(x + a.x, y + a.y);
    }

    public Vector2f plus(float x, float y) {
        return new Vector2f(this.x + x, this.y + y);
    }

    public static Vector2f add(Vector2f a, Vector2f b) {
        return new Vector2f(a.x + b.x, a.y + b.y);
    }

    public Vector2f subtract(Vector2f a) {
        x -= a.x;
        y -= a.y;
        return this;
    }

    public Vector2f subtract(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    public Vector2f minus(Vector2f a) {
        return new Vector2f(x - a.x, y - a.y);
    }

    public Vector2f minus(float x, float y) {
        return new Vector2f(this.x - x, this.y - y);
    }

    public static Vector2f subtract(Vector2f a, Vector2f b) {
        return new Vector2f(a.x - b.x, a.y - b.y);
    }

    public Vector2f multiply(Vector2f a) {
        x *= a.x;
        y *= a.y;
        return this;
    }

    public Vector2f multiply(float x, float y) {
        this.x *= x;
        this.y *= y;
        return this;
    }

    public Vector2f multiply(float a) {
        this.x *= a;
        this.y *= a;
        return this;
    }

    public Vector2f times(Vector2f a) {
        return new Vector2f(x * a.x, y * a.y);
    }

    public Vector2f times(float x, float y) {
        return new Vector2f(this.x * x, this.y * y);
    }

    public Vector2f times(float a) {
        return new Vector2f(this.x * a, this.y * a);
    }

    public static Vector2f multiply(Vector2f a, Vector2f b) {
        return new Vector2f(a.x * b.x, a.y * b.y);
    }

    public Vector2f divide(Vector2f a) {
        x /= a.x;
        y /= a.y;
        return this;
    }

    public Vector2f divide(float x, float y) {
        this.x /= x;
        this.y /= y;
        return this;
    }

    public Vector2f divide(float a) {
        this.x /= a;
        this.y /= a;
        return this;
    }

    public Vector2f over(Vector2f a) {
        return new Vector2f(x / a.x, y / a.y);
    }

    public Vector2f over(float x, float y) {
        return new Vector2f(this.x / x, this.y / y);
    }

    public Vector2f over(float a) {
        return new Vector2f(this.x / a, this.y / a);
    }

    public static Vector2f divide(Vector2f a, Vector2f b) {
        return new Vector2f(a.x / b.x, a.y / b.y);
    }

    public float magnitude() {
        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Vector2f normalized() {
        return new Vector2f(this).divide(magnitude());
    }

    public Vector2f normalize() {
        divide(magnitude());
        return this;
    }

    public float dot(Vector2f other) {
        return x * other.x + y * other.y;
    }

    public static float distance(Vector2f a, Vector2f b) {
        return (float) Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public static float dot(Vector2f a, Vector2f b) {
        return a.x * b.x + a.y * b.y;
    }

    public static float cross(Vector2f a, Vector2f b) {
        return a.x * b.y - a.y * b.x;
    }

    public static Vector2f toPolar(Vector2f origin, Vector2f vector) {
        Vector2f distanceVector = Vector2f.subtract(origin, vector);
        double angle = Math.atan2(distanceVector.getX(), distanceVector.getY());
        double distance = Math.sqrt(Math.pow(distanceVector.getX(), 2) + Math.pow(distanceVector.getY(), 2));
        return new Vector2f((float) angle, (float) distance);
    }

    public static Vector2f toCartesian(Vector2f origin, Vector2f vector) {
        return Vector2f.add(origin, new Vector2f((float) Math.sin(vector.getX()) * vector.getY(), (float) Math.cos(vector.getX()) * vector.getY()));
    }
}
