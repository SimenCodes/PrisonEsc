package no.ntnu.prisonesc;

import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * Created by Henrik on 02.04.2017.
 */

public abstract class FlyingObject implements Collidable {
    public static final String TAG = "FlyingObject";

    public final Point position;
    public final int width, height;

    @NonNull
    public final ImageView image;

    public FlyingObject(Point position, int width, int height, @NonNull ImageView image) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.image = image;
    }

    /**
     * Creates a new flying object of random type.
     *
     * @param imageView An imageView that's attached to the correct parent, and NOT attached to another flyingobject
     * @return
     */
    public static FlyingObject create(ImageView imageView, Point p) {
        return new Balloon(p, 100, 100, imageView); // TODO Dimensions as resources, maybe in scrollview
    }


    abstract void onCollision(Player player);

    @Override
    public int getRadius() {
        if (width > height)
            return width / 2;
        else
            return height / 2;
    }

    @Override
    public Point getPosition() {
        return this.position.move(new Point(width / 2, height / 2));
    }
}
