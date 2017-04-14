package no.ntnu.prisonesc;

/**
 * Something that rotates, or the rotation of such a thing.
 * {@link #getRotation} will always return a float in the [0..fullCircle> range.
 */
public class Rotation {
    private final float fullCircle;
    private float rotation;

    /**
     * @param fullCircle      Probably 360 if you're a deg guy, or Math.PI if you're rad.
     * @param initialRotation Guess…
     */
    public Rotation(float fullCircle, float initialRotation) {
        this.fullCircle = fullCircle;
        setRotation(initialRotation);
    }

    /**
     * For the lazy among us. A full circle is 360deg like in primary school.
     */
    public Rotation() {
        fullCircle = 360;
        rotation = 0;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        if (rotation >= 0)
            this.rotation = rotation % fullCircle;
        else
            this.rotation = fullCircle - (rotation % fullCircle);
    }

    public void rotate(float delta) {
        setRotation(rotation + delta);
    }
}
