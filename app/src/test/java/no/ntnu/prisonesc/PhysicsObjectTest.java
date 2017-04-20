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
        Assert.assertEquals("Når vi går på skrå nedover skal det legges til litt rett opp", 0, physics.addGlider2(true));
        Assert.assertEquals("Når vi går på skrå nedover skal det legges til litt rett opp", 299, physics.addGlider2(false));
        physics.addVel(new Point(0, 0));
        physics.setRot(new OldRotation(1350));
        Assert.assertEquals("Når vi ser oppover og går nedover skal legges til litt", -299, physics.addGlider2(true));
        Assert.assertEquals("Når vi ser oppover og går nedover skal legges til litt", 300, physics.addGlider2(false));
        physics.addVel(new Point(0, 300));
        physics.setRot(new OldRotation(1350));
        Assert.assertEquals("Når vi går rett fram skal vi få litt fart oppover.", -149, physics.addGlider2(true));
        Assert.assertEquals("Når vi går rett fram skal vi få litt fart oppover.", 149, physics.addGlider2(false));
        physics.addVel(new Point(0, 0));
        physics.setRot(new OldRotation(450));
        Assert.assertEquals("Når vi går rett fram skal vi ikke få noe fart", 0, physics.addGlider2(true));
        Assert.assertEquals("Når vi går rett fram skal vi ikke få noe fart", 0, physics.addGlider2(false));
        physics.addVel(new Point(0, 0));
        physics.setRot(new OldRotation(0));
        Assert.assertEquals("Når vi går rett fram og ser rett ned skal vi ikke få noe fart", 0, physics.addGlider2(true));
        Assert.assertEquals("Når vi går rett fram og ser rett ned skal vi ikke få noe fart", 0, physics.addGlider2(false));
        physics.addVel(new Point(-300, -300));
        physics.setRot(new OldRotation(450));
        Assert.assertEquals("Når vi går rett ned skal vi få litt fart i x og litt i y", 149, physics.addGlider2(true));
        Assert.assertEquals("Når vi går rett ned skal vi få litt fart i x og litt i y", 150, physics.addGlider2(false));
    }

}