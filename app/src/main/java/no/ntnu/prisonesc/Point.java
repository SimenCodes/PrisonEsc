package no.ntnu.prisonesc;

/**
 * Created by Henrik on 02.04.2017.
 */

@SuppressWarnings("WeakerAccess")
public class Point {
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point move(Point p) {
        return new Point(this.x + p.x, this.y + p.y);
    }

    public Point move(int x, int y) {
        return new Point(this.x + x, this.y + y);
    }

    public Point diff(Point p) {
        return new Point(p.x - this.x, p.y - this.y);
    }

    public double dist(Point p) {
        Point dif = diff(p);
        return Math.sqrt(Math.pow(dif.x, 2) + Math.pow(dif.y, 2));
    }

    @Override
    public String toString() {
        return x + " " + y;
    }
}
