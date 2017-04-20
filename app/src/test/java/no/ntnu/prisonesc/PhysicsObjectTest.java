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
        physics = new PhysicsObject(0.0005, 1, 0, 0, 300, -1);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void addGlider() throws Exception {

    }

    @Test
    public void addGlider2() throws Exception {
        physics.setRot(new OldRotation(900));
        Assert.assertEquals("Når vi går rett opp skal vi ikke legge til noe", 0, physics.addGlider2(true));
        Assert.assertEquals("Når vi går rett opp skal vi ikke legge til noe", 0, physics.addGlider2(false));
        physics.addVel(new Point(300, 0));
        physics.setRot(new OldRotation(900));
        Assert.assertEquals("Når vi går på skrå oppover skal vi fortsatt ikke legge til noe", 0, physics.addGlider2(true));
        Assert.assertEquals("Når vi går på skrå oppover skal vi fortsatt ikke legge til noe", 0, physics.addGlider2(false));
        physics.addVel(new Point(-300, -600));
        physics.setRot(new OldRotation(900));
        Assert.assertEquals("Når vi går rett ned skal vi legge til noe proposjonalt med hastigheten(Gliderfactor er 1)", 0, physics.addGlider2(true));
        Assert.assertEquals("Når vi går rett ned skal vi legge til noe proposjonalt med hastigheten(Gliderfactor er 1)", 300, physics.addGlider2(false));
        physics.addVel(new Point(300, 0));
        physics.setRot(new OldRotation(900));
        //Assert.assertEquals("Når vi går på skrå nedover skal det legges til litt rett opp");
    }

}