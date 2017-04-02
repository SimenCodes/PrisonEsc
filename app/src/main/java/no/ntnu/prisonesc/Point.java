package no.ntnu.prisonesc;

/**
 * Created by Henrik on 02.04.2017.
 */

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

    public Point diff(Point p) {
        return new Point(p.x - this.x, p.y - this.y);
    }

    @Override
    public String toString() {
        return x + " " + y;
    }
}
