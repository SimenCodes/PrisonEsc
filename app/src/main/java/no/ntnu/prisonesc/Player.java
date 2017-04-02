package no.ntnu.prisonesc;

import android.graphics.drawable.Drawable;

/**
 * Created by Henrik on 02.04.2017.
 */

public class Player {
    private final PhysicsObject physics;
    int rotation;
    private Drawable image;

    private Player() {
        physics = new PhysicsObject();
    }

    public Point getPos() {
        return physics.getPos();
    }
}
