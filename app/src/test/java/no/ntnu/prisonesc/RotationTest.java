package no.ntnu.prisonesc;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;


/**
 * Created by simen on 14.04.17.
 */
public class RotationTest {
    private Rotation rotation;

    @Before
    public void setUp() throws Exception {
        rotation = new Rotation();
    }

    @Test
    public void init() throws Exception {
        assertEquals(0f, rotation.getRotation());
    }


    @Test
    public void set90Rotation() {
        rotation.setRotation(90);
        assertEquals(90f, rotation.getRotation());
    }


    @Test
    public void set360Rotation() {
        rotation.setRotation(360);
        assertEquals(0f, rotation.getRotation());
    }


    @Test
    public void set700Rotation() {
        rotation.setRotation(700);
        assertEquals(340f, rotation.getRotation());
    }


    @Test
    public void set720Rotation() {
        rotation.setRotation(720);
        assertEquals(0f, rotation.getRotation());
    }


    @Test
    public void setMinus90Rotation() {
        rotation.setRotation(-90);
        assertEquals(270f, rotation.getRotation());
    }


    @Test
    public void setMinus360Rotation() {
        rotation.setRotation(-360);
        assertEquals(0f, rotation.getRotation());
    }


    @Test
    public void setMinus700Rotation() {
        rotation.setRotation(-700);
        assertEquals(20f, rotation.getRotation());
    }


    @Test
    public void setMinus720Rotation() {
        rotation.setRotation(-720);
        assertEquals(0f, rotation.getRotation());
    }


    @Test
    public void rotate() throws Exception {
        rotation.setRotation(360);
        rotation.rotate(1);
        assertEquals(1f, rotation.getRotation());
    }


    @Test
    public void rotateMany() throws Exception {
        rotation.setRotation(360);
        for (int i = 1; i < 360; i++) {
            rotation.rotate(1);
            assertEquals((float) i, rotation.getRotation());
        }
        rotation.rotate(1);
        assertEquals(0f, rotation.getRotation());

        for (int i = 359; i > 0; i--) {
            rotation.rotate(-1);
            assertEquals((float) i, rotation.getRotation());
        }
    }

}