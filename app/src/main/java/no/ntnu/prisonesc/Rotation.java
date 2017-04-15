package no.ntnu.prisonesc;

/**
 * Something that rotates, or the rotation of such a thing.
 * {@link #getRotation} will always return a float in the [0..fullCircle> range.
 */
public class Rotation {
    protected final float fullCircle;
    protected float rotation;

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
        final float modulo = rotation % fullCircle;
        if (rotation >= 0)
            this.rotation = modulo;
        else if (modulo != 0)
            this.rotation = fullCircle + modulo;
        else this.rotation = 0;
    }

    public void rotate(float delta) {
        setRotation(rotation + delta);
    }

    public void add(Rotation delta) {
        setRotation(rotation + delta.rotation);
    }

    public void subtract(Rotation delta) {
        setRotation(rotation - delta.rotation);
    }

    /**
     * Samme som rotate, men returnerer resultatet.
     *
     * @param delta
     * @return
     */
    public Rotation rotated(float delta) {
        return new Rotation(this.fullCircle, rotation + delta);
    }

    public Rotation added(Rotation delta) {
        return new Rotation(this.fullCircle, rotation + delta.rotation);
    }

    public Rotation subtracted(Rotation delta) {
        return new Rotation(this.fullCircle, rotation - delta.rotation);
    }
}
