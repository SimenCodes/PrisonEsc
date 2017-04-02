package no.ntnu.prisonesc;

import android.graphics.drawable.Drawable;

/**
 * Created by Henrik on 02.04.2017.
 */

public class Player {
    public final Point size;
    private final PhysicsObject physics;
    private Drawable image;

    public Player(Drawable image, double drag, int posY, int velX, int velY, int accY) {
        physics = new PhysicsObject(drag, posY, velX, velY, accY);
        this.image = image;
        this.size = new Point(10, 10);//Her har jeg satt størelsen på player, den må settes på en mer fornuftig måte.
    }

    public void tick() {
        physics.tick();
    }

    public Point getPos() {
        return physics.getPos();
    }

    public Point getSize() {
        return this.size;
    }

    public void setRot(int rot) {

    }
}
