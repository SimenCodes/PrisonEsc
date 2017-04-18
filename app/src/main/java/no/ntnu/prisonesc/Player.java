package no.ntnu.prisonesc;

import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Henrik on 02.04.2017.
 */

public class Player {
    public final Point size;
    private final PhysicsObject physics;


    public Player(double drag, double gliderFactor, int posY, int velX, int velY, int accY) {
        physics = new PhysicsObject(drag, gliderFactor, posY, velX, velY, accY);

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

    public OldRotation getRot() {
        return physics.getRot();
    }

    public void setRot(OldRotation rot) {
        Log.d(TAG, "setRot.rot: "+rot.getDeg());
        physics.setRot(rot);
    }

    public int getVelY() {
        return physics.getVelY();
    }

    public void addVel(Point p) {
        physics.addVel(p);
    }

    public void reduseAirResitance(float amount) {
        physics.reduseAirResistance(amount);
    }

    public void addAccleration(int force, int duration) {
        int accX = (int) (Math.cos(physics.getRot().getRad()) * force);
        int accY = (int) (Math.sin(physics.getRot().getRad()) * force);
        physics.setAcc(new Point(accX, accY), duration);
    }

}
