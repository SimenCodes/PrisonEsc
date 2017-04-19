package no.ntnu.prisonesc;

/**
 * Created by Henrik on 02.04.2017.
 */

@SuppressWarnings("WeakerAccess")
public class Player implements Circular {
    public final Point size;
    private final PhysicsObject physics;


    public Player(double drag, double gliderFactor, int posY, int velX, int velY, int accY, Point size) {
        physics = new PhysicsObject(drag, gliderFactor, posY, velX, velY, accY);
        this.size = size;
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

    public OldRotation getRot() {
        return physics.getRot();
    }

    public void setRot(OldRotation rot) {
        physics.setRot(rot);
    }

    public int getVelY() {
        return physics.getVelY();
    }

    public void addVel(Point p) {
        physics.addVel(p);
    }

    public void reduceAirResistance(double amount) {
        physics.reduceAirResistance(amount);
    }

    public void addAccleration(int force, int duration) {
        int accX = (int) (Math.cos(physics.getRot().getRad()) * force);
        int accY = (int) (Math.sin(physics.getRot().getRad()) * force);
        physics.setAcc(new Point(accX, accY), duration);
    }

    @Override
    public int getRadius() {
        if (size.x > size.y)
            return size.x / 2;
        else
            return size.y / 2;
    }

    @Override
    public Point getCenter() {
        return physics.getPos().move(size.x / 2, size.y / 2);
    }
}
