package no.ntnu.prisonesc;

import android.graphics.drawable.Drawable;

/**
 * Created by Henrik on 02.04.2017.
 */

public class Player {
    private final PhysicsObject physics;

    private Drawable image;

    private Player(Drawable image, int drag, int posY, int velX, int velY, int accY) {
        physics = new PhysicsObject(drag, posY, velX, velY, accY);
        this.image = image;
    }

    public void tick() {
        physics.tick();
    }

    public Point getPos() {
        return physics.getPos();
    }
}
