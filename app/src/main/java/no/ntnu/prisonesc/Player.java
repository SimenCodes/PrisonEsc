package no.ntnu.prisonesc;

/**
 * Created by Henrik on 02.04.2017.
 */

@SuppressWarnings("WeakerAccess")
public class Player implements Circular {
    private final PhysicsObject physics;
    private Point size;
    private int moneyBalloonCount = 0;


    public Player(double drag, double gliderFactor, int posY, int velX, int velY, float accY, Point size) {
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

    public void setSize(Point size) {
        if (size.x <= 0 || size.y <= 0)
            throw new IllegalArgumentException("Player can't have negative size");
        this.size = size;
    }

    public OldRotation getRot() {
        return physics.getRot();
    }

    public void setRot(OldRotation rot) {
        physics.setRot(rot);
    }

    public float getVelY() {
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
            return (int) (size.x / 2);
        else
            return (int) (size.y / 2);
    }

    @Override
    public Point getCenter() {
        return physics.getPos().move(size.x / 2, size.y / 2);
    }

    public void addRocketDuration(int p) {
        physics.addRocketDurration(p);
    }

    public void addStartHeight(float height) {
        physics.addStartHeight(height);
    }

    public void addGliderFactor(float amount) {
        physics.addGliderFactor(amount);
    }

    public void addRocketBoost(Point power) {
        physics.addRocketBoost(power);
    }

    public void collectMoneyBalloon() {
        moneyBalloonCount++;
    }

    public int getMoneyBalloonCount() {
        return moneyBalloonCount;
    }
}
