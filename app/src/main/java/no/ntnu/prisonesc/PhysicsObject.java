package no.ntnu.prisonesc;

/**
 * Created by Henrik on 02.04.2017.
 */

public class PhysicsObject {
    private int accX;
    private int accY;
    private int velX;
    private int velY;
    private int posX;
    private int posY;

    public Point getPos() {
        return new Point(posX, posY);
    }

}
