package no.ntnu.prisonesc;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Henrik on 13.04.2017.
 */
public class PhysicsObjectTest {
    PhysicsObject physics;

    @Before
    public void setUp() throws Exception {
        physics = new PhysicsObject(0.0005, 1, 0, 300, 300, -1);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void addGlider() throws Exception {

    }

    @Test
    public void addGlider2() throws Exception {
        physics.setRot(new OldRotation(450));
        physics.addVel(new Point(physics.addGlider2(true), physics.addGlider2(false)));
        Assert.assertEquals("Hastigheten burde endres like mye  i både x og y retning.", physics.getPos().x, physics.getPos().y);
        this.setUp();
        physics.setRot(new OldRotation(900));
        physics.addVel(new Point(physics.addGlider2(true), physics.addGlider2(false)));
        Assert.assertEquals("Hastigheten i y-retning burde halveres når farten er 45 grader oppover og rotasjonen er på 90 grader.", 150, physics.getVelY());

    }

}