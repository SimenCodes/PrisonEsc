package no.ntnu.prisonesc;

import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * Created by Henrik on 02.04.2017.
 */

public abstract class FlyingObject {
    public static final String TAG = "FlyingObject";

    public final int x;//x koordinaten
    public final int y;//y koordinaten
    public final int w;//bredden
    public final int h;//høyden
    @NonNull
    public final ImageView image;

    public FlyingObject(int x, int y, int width, int height, @NonNull ImageView image) {
        this.x = x;
        this.y = y;
        this.w = width;
        this.h = height;
        this.image = image;
    }

    abstract void onCollision(Player player);

    /**
     * Creates a new flying object of random type.
     *
     * @param imageView An imageView that's attached to the correct parent, and NOT attached to another flyingobject
     * @return
     */
    public static FlyingObject create(ImageView imageView, int x, int y) {
        throw new UnsupportedOperationException("We haven't implemented this yet");
    }
}
