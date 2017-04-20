package no.ntnu.prisonesc;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by Henrik on 02.04.2017.
 */

@SuppressWarnings("WeakerAccess")
public abstract class FlyingObject implements Circular {
    public static final String TAG = "FlyingObject";

    public final Point position;
    @NonNull
    public final ImageView image;
    protected int width, height;

    public FlyingObject(Point position, @NonNull ImageView image, @DrawableRes int imageResource) {
        this.position = position;
        this.image = image;
        updateImage(imageResource);
        Log.d(TAG, "FlyingObject() created with: position = [" + position + "], width = [" + width + "], height = [" + height + "]");
    }

    protected void updateImage(@DrawableRes int imageResource) {
        image.setImageResource(imageResource);
        width = image.getWidth() / Util.pixelSize(image.getContext());
        height = image.getHeight() / Util.pixelSize(image.getContext());
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
    public Point getCenter() {
        return this.position.move(new Point(width / 2, height / 2));
    }
}
