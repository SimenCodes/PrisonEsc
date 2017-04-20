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
        super((float) Math.PI * 2f, (float)( Math.toRadians(initialRotation / 10.000)));
        //Log.d(TAG, "OldRotation.res: "+ (float) Math.toRadians(initialRotation / 10));
    }

    public float getRad() {
        return rotation;
    }

    public void setRad(float newRot) {
        super.setRotation(newRot);
    }

    /**
     * Vær obs på at deg her er ganget med 10 for å få bedre presisjon.
     *
     * @return
     */
    public int getDeg() {
        //Log.d(TAG, "getDeg.rotation: "+rotation);
        //Log.d(TAG, "getDeg.rotation: "+Math.toDegrees(rotation));
        return (int) (Math.toDegrees(rotation) * 10.00f);
    }

    public void setDeg(int newRot) {
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
        return new OldRotation(this.rotation + delta);
    }

    public OldRotation rotated(int delta) {
        return new OldRotation(this.getDeg() + delta);
    }

    @Override
    public OldRotation added(Rotation delta) {
        return new OldRotation(this.rotation + delta.rotation);
    }

    @Override
    public OldRotation subtracted(Rotation delta) {
        return new OldRotation(this.rotation - delta.rotation);
    }

    public String toString() {
        return String.valueOf(this.getDeg());
    }
}
