package no.ntnu.prisonesc;

/**
 * Created by Henrik on 02.04.2017.
 */

public class Player {
    public final Point size;
    private final PhysicsObject physics;


    public Player(double drag, int posY, int velX, int velY, int accY) {
        physics = new PhysicsObject(drag, posY, velX, velY, accY);

        this.size = new Point(10, 10);//Her har jeg satt størelsen på player, den må settes på en mer fornuftig måte.
    }

    public void tick() {
        //Log.d(TAG, "tick: " + (physics.getRot() / 10 + 90));

        physics.tick();
    }

    public Point getPos() {
        return physics.getPos();
    }

    public Point getSize() {
        return this.size;
    }

    public int getRot() {
        return physics.getRot();
    }

    public void setRot(int rot) {
        physics.setRot(rot);
    }

    public int getVelY() {
        return physics.getVelY();
    }

    public void addVel(Point p) {
        physics.addVel(p);
    }
}
