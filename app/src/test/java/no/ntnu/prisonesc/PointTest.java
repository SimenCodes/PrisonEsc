package no.ntnu.prisonesc;

import junit.framework.Assert;

import org.junit.Test;

/**
 * Created by Henrik on 19.04.2017.
 */
public class PointTest {
    @Test
    public void dist() throws Exception {
        Point p1 = new Point(5, 5);
        Point p2 = new Point(0, 5);
        Assert.assertEquals("Skal bli 5", 5.0, p1.dist(p2));
        Point p0 = new Point(0, 0);
        Point p3 = new Point(3, 4);
        Assert.assertEquals("Skal bli 5*En gang til", 5.0, p0.dist(p3));
    }

}