package no.ntnu.prisonesc;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Henrik on 15.04.2017.
 */
public class OldRotationTest {
    OldRotation rotRad;
    OldRotation rotInt;
    OldRotation test;

    @Before
    public void setUp() throws Exception {
        rotRad = new OldRotation(1.578234f);
        rotInt = new OldRotation(899);
        test = new OldRotation();
    }

    @Test
    public void test() throws  Exception{
        Assert.assertEquals("At tom konstruktør gir en initialrotasjon på 0",test.getDeg(),0);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void getRad() throws Exception {
        Assert.assertEquals("At konstruktør med radianer gir riktig tall",rotRad.getRad(),1.578234f);
        Assert.assertEquals("At tom konstruktør gir en initialrotasjon på 0",test.getRad(),0.0f);
    }

    @Test
    public void setRad() throws Exception {

    }

    @Test
    public void getDeg() throws Exception {
        Assert.assertEquals("At konstruktør med grader gir riktig tall",rotInt.getDeg(),899);
        Assert.assertEquals("At tom konstruktør gir en initialrotasjon på 0",test.getDeg(),0);
    }

    @Test
    public void setDeg() throws Exception {

    }

    @Test
    public void rotated() throws Exception {

    }

    @Test
    public void added() throws Exception {

    }

    @Test
    public void subtracted() throws Exception {

    }

}