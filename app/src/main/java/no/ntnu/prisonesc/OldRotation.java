package no.ntnu.prisonesc;

/**
 * Created by Henrik on 14.04.2017.
 */

public class OldRotation extends Rotation {
    public OldRotation() {
        super((float) Math.PI * 2, 0.0f);
    }

    public OldRotation(float initialRotation) {
        super((float) Math.PI * 2f, initialRotation);
    }

    public OldRotation(int initialRotation) {
        super((float) Math.PI * 2f, (float) Math.toRadians(initialRotation / 10));
    }

    public float getRad() {
        return rotation;
    }

    public void setRad(float newRot) {
        super.setRotation(newRot);
    }

    /**
     * Vær obs på at deg her er ganget med 10 for å få bere presisjon.
     *
     * @return
     */
    public int getDeg() {
        return (int) (Math.toDegrees(rotation) * 10);
    }

    public void serDeg(int newRot) {
        super.setRotation((float) Math.toRadians(newRot / 10));
    }

    /**
     * For å få en OldRotation ut når det trengs.
     *
     * @param delta
     * @return
     */
    @Override
    public OldRotation rotated(float delta) {
        return (OldRotation) super.rotated(delta);
    }

    @Override
    public OldRotation added(Rotation delta) {
        return (OldRotation) super.added(delta);
    }

    @Override
    public OldRotation subtracted(Rotation delta) {
        return (OldRotation) super.subtracted(delta);
    }
}
