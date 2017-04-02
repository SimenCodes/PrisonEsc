package no.ntnu.prisonesc;

import android.content.Context;
import android.support.v4.util.ArraySet;
import android.util.ArrayMap;
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

    // A temporary place to store offscreen imageviews for reuse
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

        // We wait a little moment so the view gets width and height (we should have used a tree listener, but meh…)
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ground[1].setTranslationX(ground[0].getWidth());
                for (int i = 0; i < CLOUD_COUNT; i++) {
                    ImageView imageView = new ImageView(backgroundContainer.getContext());
                    imageView.setTranslationX((float) (getWidth() * Math.random()));
                    imageView.setTranslationY((float) ((getHeight() - ground[0].getHeight()) * Math.random()));
                    if (i % 2 == 0) imageView.setImageResource(R.drawable.cloud_1);
                    else imageView.setImageResource(R.drawable.cloud_2);
                    backgroundContainer.addView(imageView);
                    backgroundObjects.put(imageView, Math.random());
                }
            }
        }, 100);
    }

    /**
     * Call this every clock tick. It will do all the scrolling
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
                    // Init somewhere around top or bottom edge
                    image.setTranslationX((float) (getWidth() * Math.random()));
                    image.setTranslationY(diff.y >= 0 ? -image.getHeight() : getHeight());
                } else {
                    // Init somewhere around left or right edge
                    image.setTranslationX(diff.x >= 0 ? getWidth() : -getWidth() - image.getWidth());
                    if (pos.y < 100) {
                        // Avoid clouds too close to the ground
                        image.setTranslationY((float) (Math.random() * (getHeight() / 2)));
                    } else {
                        image.setTranslationY((float) (Math.random() * getHeight()));
                    }
                }
                backgroundObjects.put(image, Math.random());
            }
        }

        for (FlyingObject flying : hittableObjects) {
            if (!move(diff, flying.image, 1)) {
                // This object is offscreen, so let's trash it and recycle the view.
                hittableObjects.remove(flying);
                recycledImages.add(flying.image);
            }
        }
    }

    /**
     * Moves the given image view
     *
     * @param diff       how far it should be moved in either direction
     * @param image      the imageview to move
     * @param multiplier defaults to 1
     * @return false if the object will go offscreen. If so, it will not be moved and should be recycled
     */
    @SuppressWarnings("RedundantIfStatement")
    private boolean move(Point diff, ImageView image, double multiplier) {
        float y = image.getTranslationY() + diff.y;
        float x = (float) (image.getTranslationX() - diff.x * multiplier);

        image.setTranslationY(y);
        image.setTranslationX(x);

        if (x < -image.getWidth()) return false;
        if (y < -image.getHeight()) return false;
        if (y > getHeight()) return false;
        return true;
    }
}
