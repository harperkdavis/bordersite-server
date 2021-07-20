package engine.math;

import java.util.Objects;

public class Vector3f {

    private float x, y, z;

    public static Vector3f zero() { return new Vector3f(0, 0, 0); }
    public static Vector3f one() { return new Vector3f(1, 1, 1); }
    public static Vector3f oneX() { return new Vector3f(1, 0, 0); }
    public static Vector3f oneY() { return new Vector3f(0, 1, 0); }
    public static Vector3f oneZ() { return new Vector3f(0, 0, 1); }

    public Vector3f(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f(Vector3f other) {
        this.x = other.getX();
        this.y = other.getY();
        this.z = other.getZ();
    }

    public Vector3f(Vector2f other, float z) {
        this.x = other.getX();
        this.y = other.getY();
        this.z = z;
    }

    public Vector3f() {
        this(0, 0, 0);
    }

    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3f add(Vector3f a) {
        x += a.x;
        y += a.y;
        z += a.z;
        return this;
    }

    public Vector3f add(float x, float y, float z) {
        this.x += x;
        this.y += y;
        this.z += z;
        return this;
    }

    public Vector3f plus(Vector3f a) {
        return new Vector3f(x + a.x, y + a.y, z + a.z);
    }

    public Vector3f plus(float x, float y, float z) {
        return new Vector3f(this.x + x, this.y + y, this.z + z);
    }

    public static Vector3f add(Vector3f a, Vector3f b) {
        return new Vector3f(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public Vector3f subtract(Vector3f a) {
        x -= a.x;
        y -= a.y;
        z -= a.z;
        return this;
    }

    public Vector3f subtract(float x, float y, float z) {
        this.x -= x;
        this.y -= y;
        this.z -= z;
        return this;
    }

    public Vector3f minus(Vector3f a) {
        return new Vector3f(x - a.x, y - a.y, z - a.z);
    }

    public Vector3f minus(float x, float y, float z) {
        return new Vector3f(this.x - x, this.y - y, this.z - z);
    }

    public static Vector3f subtract(Vector3f a, Vector3f b) {
        return new Vector3f(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public Vector3f multiply(Vector3f a) {
        x *= a.x;
        y *= a.y;
        z *= a.z;
        return this;
    }

    public Vector3f multiply(float x, float y, float z) {
        this.x *= x;
        this.y *= y;
        this.z *= z;
        return this;
    }

    public Vector3f multiply(float a) {
        this.x *= a;
        this.y *= a;
        this.z *= a;
        return this;
    }

    public Vector3f times(Vector3f a) {
        return new Vector3f(x * a.x, y * a.y, z * a.z);
    }

    public Vector3f times(float x, float y, float z) {
        return new Vector3f(this.x * x, this.y * y, this.z * z);
    }

    public Vector3f times(float a) {
        return new Vector3f(this.x * a, this.y * a, this.z * a);
    }

    public static Vector3f multiply(Vector3f a, Vector3f b) {
        return new Vector3f(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    public Vector3f divide(Vector3f a) {
        x /= a.x;
        y /= a.y;
        z /= a.z;
        return this;
    }

    public Vector3f divide(float x, float y, float z) {
        this.x /= x;
        this.y /= y;
        this.z /= z;
        return this;
    }

    public Vector3f divide(float a) {
        this.x /= a;
        this.y /= a;
        this.z /= a;
        return this;
    }

    public Vector3f over(Vector3f a) {
        return new Vector3f(x / a.x, y / a.y, z / a.z);
    }

    public Vector3f over(float x, float y, float z) {
        return new Vector3f(this.x / x, this.y / y, this.z / z);
    }

    public Vector3f over(float a) {
        return new Vector3f(this.x / a, this.y / a, this.z / a);
    }

    public static Vector3f divide(Vector3f a, Vector3f b) {
        return new Vector3f(a.x / b.x, a.y / b.y, a.z / b.z);
    }

    public float magnitude() {
        return (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    public Vector3f normalized() {
        return new Vector3f(this).divide(magnitude());
    }

    public Vector3f normalize() {
        divide(magnitude());
        return this;
    }

    public static Vector3f cross(Vector3f a, Vector3f b) {
        return new Vector3f(a.getY() * b.getZ() - a.getZ() * b.getY(), a.getZ() * b.getX() - a.getX() * b.getZ(), a.getX() * b.getY() - a.getY() * b.getX());
    }

    public float dot(Vector3f other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public static float dot(Vector3f a, Vector3f b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }

    public Vector3f copy() {
        return new Vector3f(x, y, z);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3f vector3F = (Vector3f) o;
        return Float.compare(vector3F.x, x) == 0 &&
                Float.compare(vector3F.y, y) == 0 &&
                Float.compare(vector3F.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public String toString() {
        return "{x: " + x + ", y: " + y + ", z: " + z + "}";
    }
}
