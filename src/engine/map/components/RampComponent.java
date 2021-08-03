package engine.map.components;

import engine.map.collision.Collision;
import engine.game.PlayerHandler;
import engine.math.Mathf;
import engine.math.Vector2f;
import engine.math.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class RampComponent implements Component {

    private Vector3f stl, str, sbl, sbr, ntl, ntr, nbl, nbr;
    private Vector3f tnor, bnor, lnor, rnor, nnor, frontnormal, leftnormal, rightnormal;
    private Vector2f tl, tr, bl, br,
            etl, etr, ebl, ebr,
            setl, setr, sebl, sebr,
            etopl, etopr, ebtml, ebtmr,
            topl, topr, btml, btmr, intopl, intopr,
            stopl, stopr, sbotl, sbotr;

    private int direction;
    private float crossMagnitude;
    private float height, slope;
    private float tiling = 2.0f;
    private boolean mesh = true, collision = true;

    /*
            Z-
        TL-----TR
        | \     |           0
    X-  |   \   |  X+     3   1
        |     \ |           2
        BL-----BR
            Z+
     */

    public RampComponent(Vector3f a, Vector3f b, Vector3f c, float height, int direction, float tiling, boolean mesh, boolean collision) {
        this(a, b, c, height, direction, tiling);
        this.collision = collision;
        this.mesh = mesh;
    }

    public RampComponent(Vector3f a, Vector3f b, Vector3f c, float height, int direction, float tiling) {
        this(a, b, c, height, direction);
        this.tiling = tiling;
    }

    public RampComponent(Vector3f a, Vector3f b, Vector3f c, float height, int direction) {
        this.stl = a;
        this.sbr = c;
        this.sbl = b;

        this.sbr.setY(stl.getY());
        this.sbl.setY(stl.getY());

        Vector3f midpoint = Vector3f.add(a, c).divide(2);
        this.str = Vector3f.add(sbl, Vector3f.subtract(midpoint, sbl).multiply(2));

        for (int i = 0; i < direction; i++) {
            Vector3f temp = stl.copy();
            stl = sbl.copy();
            sbl = sbr.copy();
            sbr = str.copy();
            str = temp;
        }
        
        this.height = height;

        this.ntl = stl.plus(0, height, 0);
        this.ntr = str.plus(0, height, 0);
        this.nbr = sbr.plus(0, 0, 0);
        this.nbl = sbl.plus(0, 0, 0);

        tl = new Vector2f(stl.getX(), stl.getZ());
        tr = new Vector2f(str.getX(), str.getZ());
        bl = new Vector2f(sbl.getX(), sbl.getZ());
        br = new Vector2f(sbr.getX(), sbr.getZ());

        float radius = PlayerHandler.PLAYER_RADIUS;
        calculateSideNormals();

        etl = getExtended(bl, tl, tr, lnor, tnor, radius);
        etr = getExtended(tl, tr, br, tnor, rnor, radius);
        ebr = getExtended(tr, br, bl, rnor, bnor, radius);
        ebl = getExtended(br, bl, tl, bnor, lnor, radius);

        etopl = new Vector2f(etl);
        etopr = new Vector2f(etr);
        ebtmr = new Vector2f(ebr);
        ebtml = new Vector2f(ebl);
        topl = new Vector2f(tl);
        topr = new Vector2f(tr);
        btmr = new Vector2f(br);
        btml = new Vector2f(bl);

        calculateTopNormal();

        slope = Vector3f.dot(Vector3f.oneY(), nnor.normalized());

        leftnormal = lnor;
        frontnormal = tnor;
        rightnormal = rnor;

        intopl = new Vector2f(etopl.getX() - frontnormal.getX() * radius * 2, etopl.getY() - frontnormal.getZ() * radius * 2);
        intopr = new Vector2f(etopr.getX() - frontnormal.getX() * radius * 2, etopr.getY() - frontnormal.getZ() * radius * 2);

        setl = getExtended(bl, tl, tr, lnor, tnor, radius - 0.1f);
        setr = getExtended(tl, tr, br, tnor, rnor, radius - 0.1f);
        sebr = getExtended(tr, br, bl, rnor, bnor, radius - 0.1f);
        sebl = getExtended(br, bl, tl, bnor, lnor, radius - 0.1f);

        stopl = Mathf.intersectPoint(setl.getX(), setl.getY(), sebl.getX(), sebl.getY(), topl.getX(), topl.getY(), topr.getX(), topr.getY());
        stopr = Mathf.intersectPoint(setr.getX(), setr.getY(), sebr.getX(), sebr.getY(), topl.getX(), topl.getY(), topr.getX(), topr.getY());

        sbotl = Mathf.intersectPoint(setl.getX(), setl.getY(), sebl.getX(), sebl.getY(), btml.getX(), btml.getY(), btmr.getX(), btmr.getY());
        sbotr = Mathf.intersectPoint(setr.getX(), setr.getY(), sebr.getX(), sebr.getY(), btml.getX(), btml.getY(), btmr.getX(), btmr.getY());

        crossMagnitude = Mathf.pointLine(ebtml.getX(), ebtml.getY(), topl.getX(), topl.getY(), topr.getX(), topr.getY());

    }

    private Vector2f getExtended(Vector2f a, Vector2f b, Vector2f c, Vector3f abnor, Vector3f bcnor, float extention) {
        Vector2f eab = new Vector2f(a.getX() + abnor.getX() * extention, a.getY() + abnor.getZ() * extention);
        Vector2f eba = new Vector2f(b.getX() + abnor.getX() * extention, b.getY() + abnor.getZ() * extention);

        Vector2f ebc = new Vector2f(b.getX() + bcnor.getX() * extention, b.getY() + bcnor.getZ() * extention);
        Vector2f ecb = new Vector2f(c.getX() + bcnor.getX() * extention, c.getY() + bcnor.getZ() * extention);

        return Mathf.intersectPoint(eab.getX(), eab.getY(), eba.getX(), eba.getY(), ebc.getX(), ebc.getY(), ecb.getX(), ecb.getY());
    }

    private void calculateSideNormals() {
        Vector3f nsnor = getSideNormal(stl, str);
        Vector3f lrnor = getSideNormal(str, sbr);
        tnor = nsnor.times(-1);
        bnor = nsnor;
        rnor = lrnor.times(-1);
        lnor = lrnor;
    }

    private void calculateTopNormal() {
        nnor = getTopNormal(ntl, nbl, nbr).normalize();
    }

    private Vector3f getTopNormal(Vector3f a, Vector3f b, Vector3f c) {
        Vector3f sa = Vector3f.subtract(b, a), sb = Vector3f.subtract(c, a);
        return Vector3f.cross(sa, sb).normalize();
    }

    private Vector3f getSideNormal(Vector3f a, Vector3f b) {
        Vector3f c = Vector3f.add(a, Vector3f.oneY());
        Vector3f sa = Vector3f.subtract(b, a), sb = Vector3f.subtract(c, a);
        return Vector3f.cross(sa, sb).normalize();
    }

    @Override
    public Collision getCollision(Vector3f previous, Vector3f position, Vector3f velocity, float height, boolean isGrounded) {
        if (!collision) {
            return new Collision(position, velocity, false);
        }

        float radius = PlayerHandler.PLAYER_RADIUS;

        Vector3f newPosition = new Vector3f(position);
        Vector3f newVelocity = new Vector3f(velocity);

        getHeightAt(newPosition);

        if ((position.getY() - radius <= stl.getY() + this.height && position.getY() + height + radius >= stl.getY())) {
            newPosition = sideCollision(newPosition, topl, topr, etopr, etopl, frontnormal, false);
            newPosition = sideCollision(newPosition, btml, topl, etopl, ebtml, leftnormal, true);
            newPosition = sideCollision(newPosition, btmr, topr, etopr, ebtmr, rightnormal, true);
        }

        if (newPosition.equals(position)) {
            if (isWithinRampBounds(new Vector2f(newPosition.getX(), newPosition.getZ()))) {
                if (newPosition.getY() > stl.getY() - radius) {
                    if (newPosition.getY() < getHeightAt(position) + radius + 0.05f) {
                        Vector2f prev = new Vector2f(previous.getX(), previous.getZ()), pos = new Vector2f(newPosition.getX(), newPosition.getZ());
                        Vector2f dist = Vector2f.subtract(pos, prev);
                        Vector3f newerPosition = new Vector3f(prev.getX() + dist.getX(), newPosition.getY(), prev.getY() + dist.getY());
                        return new Collision(new Vector3f(newerPosition.getX(), getHeightAt(newerPosition) + radius, newerPosition.getZ()), velocity, true);
                    }
                }
            }
        }

        return new Collision(newPosition, velocity, isGrounded(newPosition));
    }

    @Override
    public Vector3f getRaycast(Vector3f start, Vector3f end) {

        Vector3f tlr = raycastEdge(start, end, topl, topr);
        Vector3f tbl = raycastSlantEdge(start, end, btml, topl);
        Vector3f tbr = raycastSlantEdge(start, end, btmr, topr);

        float updown = Math.abs(end.getY() - start.getY());
        float downdist = ((Math.abs(stl.getY() - start.getY()) / updown) + (1 - (Math.abs(stl.getY() - end.getY())/ updown))) / 2;
        Vector3f down = Vector3f.lerp(start, end, downdist);

        List<Vector3f> intersects = new ArrayList<>();
        if (Math.abs(down.getY() - stl.getY()) < 0.01f && (Mathf.pointTriangle(new Vector2f(down.getX(), down.getZ()), tl, bl, br) || Mathf.pointTriangle(new Vector2f(down.getX(), down.getZ()), tl, br, tr))) {
            intersects.add(down);
        }
        if (tlr != null) {
            intersects.add(tlr);
        }
        if (tbl != null) {
            intersects.add(tbl);
        }
        if (tbr != null) {
            intersects.add(tbr);
        }

        Vector3f intersect = null;
        float minDistance = Float.MAX_VALUE;
        for (Vector3f inter : intersects) {
            float distance = (float) Math.sqrt((Math.pow(inter.getX() - start.getX(), 2) + Math.pow(inter.getY() - start.getY(), 2) + Math.pow(inter.getZ() - start.getZ(), 2)));
            if (distance < minDistance) {
                intersect = inter.copy();
                minDistance = distance;
            }
        }
        return intersect;
    }

    private Vector3f raycastEdge(Vector3f s, Vector3f e, Vector2f a, Vector2f b) {
        if (!Mathf.intersect(s.getX(), s.getZ(), e.getX(), e.getZ(), a.getX(), a.getY(), b.getX(), b.getY())) {
            return null;
        }
        Vector2f intersect = Mathf.intersectPoint(s.getX(), s.getZ(), e.getX(), e.getZ(), a.getX(), a.getY(), b.getX(), b.getY());
        float maxDistance = Mathf.distance(s.getX(), s.getZ(), e.getX(), e.getZ());
        float distance = Mathf.distance(s.getX(), s.getZ(), intersect.getX(), intersect.getY());
        float y = Mathf.lerp(s.getY(), e.getY(), distance / maxDistance);
        if (y > stl.getY() && y < ntl.getY()) {
            return new Vector3f(intersect.getX(), y, intersect.getY());
        }
        return null;
    }

    private Vector3f raycastSlantEdge(Vector3f s, Vector3f e, Vector2f a, Vector2f b) {
        if (!Mathf.intersect(s.getX(), s.getZ(), e.getX(), e.getZ(), a.getX(), a.getY(), b.getX(), b.getY())) {
            return null;
        }
        Vector2f intersect = Mathf.intersectPoint(s.getX(), s.getZ(), e.getX(), e.getZ(), a.getX(), a.getY(), b.getX(), b.getY());
        float maxDistance = Mathf.distance(s.getX(), s.getZ(), e.getX(), e.getZ());
        float distance = Mathf.distance(s.getX(), s.getZ(), intersect.getX(), intersect.getY());
        float y = Mathf.lerp(s.getY(), e.getY(), distance / maxDistance);

        float sideDistance = Mathf.distance(a.getX(), a.getY(), b.getX(), b.getY());
        float sideA = Mathf.distance(a.getX(), a.getY(), intersect.getX(), intersect.getY());
        float sideB = Mathf.distance(b.getX(), b.getY(), intersect.getX(), intersect.getY());
        float up = (sideA / sideDistance + (1 - sideB / sideDistance)) / 2;
        float heightAt = Mathf.lerp(stl.getY(), ntl.getY(), up);
        if (y > stl.getY() && y < heightAt) {
            return new Vector3f(intersect.getX(), y, intersect.getY());
        }
        return null;
    }

    private boolean isWithinBounds(Vector2f position) {
        return Mathf.pointTriangle(position, setl, sebl, sebr) || Mathf.pointTriangle(position, setl, sebr, setr);
    }

    private boolean isWithinRampBounds(Vector2f position) {
        return Mathf.pointTriangle(position, stopl, sebl, sebr) || Mathf.pointTriangle(position, stopl, sebr, stopr);
    }

    @Override
    public boolean isGrounded(Vector3f pos) {
        if (isWithinBounds(new Vector2f(pos.getX(), pos.getZ()))) {
            if (pos.getY() > stl.getY()) {
                if (pos.getY() - 0.1f < getHeightAt(pos)) {
                    return true;
                }
            }
        }
        return false;
    }

    public float getHeightAt(Vector3f pos) {
        float distance1 = Mathf.pointLine(pos.getX(), pos.getZ(), intopl.getX(), intopl.getY(), intopr.getX(), intopr.getY());
        float distance2 = Mathf.pointLine(pos.getX(), pos.getZ(), ebtml.getX(), ebtml.getY(), ebtmr.getX(), ebtmr.getY());
        float distance = ((distance2 / crossMagnitude) + (1 - distance1 / crossMagnitude)) / 2;
    return Math.min(Mathf.lerp(stl.getY(), stl.getY() + height + PlayerHandler.PLAYER_RADIUS, distance), stl.getY() + height + 0.1f);
    }

    private Vector3f sideCollision(Vector3f pos, Vector2f a, Vector2f b, Vector2f c, Vector2f d, Vector3f normal, boolean checkHeight) {
        if (Mathf.pointTrapezoid(new Vector2f(pos.getX(), pos.getZ()), a, b, c, d) && (!checkHeight || (pos.getY() < getHeightAt(pos) + PlayerHandler.PLAYER_RADIUS && pos.getY() < stl.getY() + height))) {
            float distance = Mathf.pointLine(pos.getX(), pos.getZ(), c.getX(), c.getY(), d.getX(), d.getY()) * 1.05f;
            return new Vector3f(pos.getX() + normal.getX() * distance, pos.getY(), pos.getZ() + normal.getZ() * distance);
        }
        return pos;
    }



}
