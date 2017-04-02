package no.ntnu.prisonesc;

import android.content.Context;
import android.support.v4.util.ArraySet;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Map;
import java.util.Set;

/**
 * This class handles everything in the game that scrolls.
 */
@SuppressWarnings("deprecation")
public class ScrollerView extends FrameLayout {
    public static final String TAG = "ScrollerView";

    public static final int HITTABLE_OBJECT_COUNT = 2;
    public static final int CLOUD_COUNT = 5;

    Map<ImageView, Double> backgroundObjects = new ArrayMap<>();
    Set<FlyingObject> hittableObjects;
    Point pos = new Point(0, 0);

    ImageView[] ground = new ImageView[2];
    AbsoluteLayout backgroundContainer, hittableContainer;

    private Set<ImageView> recycledImages = new ArraySet<>();

    public ScrollerView(Context context) {
        super(context);
        this.hittableObjects = new ArraySet<>();
        init();
    }

    public ScrollerView(Context context, Set<FlyingObject> hittableObjects) {
        super(context);
        this.hittableObjects = hittableObjects;
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.scroller_layout, this, true);
        ground[0] = (ImageView) findViewById(R.id.ground_1);
        ground[1] = (ImageView) findViewById(R.id.ground_2);
        backgroundContainer = (AbsoluteLayout) findViewById(R.id.background);
        hittableContainer = (AbsoluteLayout) findViewById(R.id.hittable);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ground[1].setTranslationX(ground[0].getWidth());
                for (int i = 0; i < CLOUD_COUNT; i++) {
                    ImageView imageView = new ImageView(backgroundContainer.getContext());
                    imageView.setTranslationX((float) (getWidth() * Math.random()));
                    imageView.setTranslationY((float) ((getHeight() - ground[0].getHeight()) * Math.random()));
                    imageView.setImageResource(android.R.drawable.ic_menu_gallery);
                    backgroundContainer.addView(imageView);
                    backgroundObjects.put(imageView, Math.random());
                }
            }
        }, 100);
    }

    /**
     * Call this every clock tick
     *
     * @param p The new position to draw
     */
    public void tick(Point p) {
        Point diff = pos.diff(p);
        pos = p;
        for (ImageView ground : this.ground) {
            if (pos.y > 100) {
                ground.setTranslationY(ground.getHeight());
            } else if (ground.getTranslationX() < -ground.getWidth()) {
                ground.setTranslationX(ground.getWidth());
            } else {
                ground.setTranslationX(ground.getTranslationX() - diff.x);
                ground.setTranslationY(pos.y);
            }
        }

        for (ImageView image : backgroundObjects.keySet()) {
            if (!move(diff, image, backgroundObjects.get(image))) {
                if (Math.random() > 0.5) {
                    // Init somewhere around left or right edge
                    image.setTranslationX((float) (getWidth() * Math.random()));
                    image.setTranslationY(diff.y >= 0 ? -image.getHeight() : getHeight());
                } else {
                    // Init somewhere along top or bottom
                    image.setTranslationX(diff.x >= 0 ? getWidth() : -getWidth() - image.getWidth());
                    image.setTranslationY((float) (Math.random() * (2 * getHeight() - ground[0].getY())));
                }
                backgroundObjects.put(image, Math.random());
            }
        }

        for (FlyingObject flying : hittableObjects) {
            if (!move(diff, flying.image, 1)) {
                hittableObjects.remove(flying);
                recycledImages.add(flying.image);
            }
        }
    }

    /**
     * Moves the given image view
     *
     * @param diff       how far it should be moved
     * @param image      the imageview to move
     * @param multiplier defaults to 1
     * @return false if the object is now offscreen. If so, it will not be moved and should be recycled
     */
    private boolean move(Point diff, ImageView image, double multiplier) {
        Log.d(TAG, "move() called with: diff = [" + diff + "], image = [" + image + "], multiplier = [" + multiplier + "]");
        float y = image.getTranslationY() + diff.y;
        float x = (float) (image.getTranslationX() - diff.x * multiplier);
        if (x < -image.getWidth()) return false;
        if (y < -image.getHeight()) return false;
        if (y > getHeight()) return false;

        image.setTranslationY(y);
        image.setTranslationX(x);

        return true;
    }
}
