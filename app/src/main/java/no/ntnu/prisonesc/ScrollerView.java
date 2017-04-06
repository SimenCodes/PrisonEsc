package no.ntnu.prisonesc;

import android.content.Context;
import android.support.v4.util.ArraySet;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;

import static no.ntnu.prisonesc.R.id.hittable;

/**
 * This class handles everything in the game that scrolls.
 */
@SuppressWarnings("deprecation")
public class ScrollerView extends FrameLayout {
    public static final String TAG = "ScrollerView";

    public static final int MAX_HITTABLE_OBJECT_COUNT = 5;
    public static final int CLOUD_COUNT = 5;
    public static final double HITTABLE_PROBABILITY = 7;

    Map<ImageView, Double> backgroundObjects = new ArrayMap<>();
    Set<FlyingObject> hittableObjects;
    Point pos = new Point(0, 0);
    Random random = new Random();

    ImageView[] ground = new ImageView[2];
    AbsoluteLayout backgroundContainer, hittableContainer;

    // A temporary place to store offscreen imageviews for reuse
    private Queue<ImageView> recycledImages = new ArrayBlockingQueue<>(MAX_HITTABLE_OBJECT_COUNT);

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
        hittableContainer = (AbsoluteLayout) findViewById(hittable);

        // We wait a little moment so the view gets width and height (we should have used a tree listener, but meh…)
        postDelayed(new Runnable() {
            @Override
            public void run() {
                ground[1].setTranslationX(ground[0].getWidth());
                for (int i = 0; i < CLOUD_COUNT; i++) {
                    ImageView imageView = createAndAttachImageView(backgroundContainer);
                    imageView.setTranslationX(getWidth() * random.nextFloat());
                    imageView.setTranslationY((getHeight() - ground[0].getHeight()) * random.nextFloat());
                    if (i % 2 == 0) imageView.setImageResource(R.drawable.cloud_1);
                    else imageView.setImageResource(R.drawable.cloud_2);
                    backgroundObjects.put(imageView, random.nextDouble());
                }
            }
        }, 100);
    }

    /**
     * Call this every clock tick. It will do all the scrolling.
     * It's long with mane magic numbers, but you'll make it!
     *
     * @param p The new position to draw
     */
    public void tick(Point p) {
        Point diff = pos.diff(p);
        pos = p;

        final int screenWidth = getWidth(), screenHeight = getHeight();

        // Step 1: move ground if it's going to be visible
        for (ImageView ground : this.ground) {
            if (pos.y > ground.getHeight()) {
                ground.setTranslationY(ground.getHeight());
            } else if (ground.getTranslationX() < -ground.getWidth()) {
                ground.setTranslationX(ground.getWidth());
            } else {
                ground.setTranslationX(ground.getTranslationX() - diff.x);
                ground.setTranslationY(pos.y);
            }
        }

        // Step 2: move clouds/background
        for (ImageView image : backgroundObjects.keySet()) {
            if (!move(diff, image, backgroundObjects.get(image))) {
                if (random.nextBoolean()) {
                    // Init somewhere around top or bottom edge
                    image.setTranslationX(screenWidth * random.nextFloat());
                    image.setTranslationY(diff.y >= 0 ? -image.getHeight() : screenHeight);
                } else {
                    // Init somewhere around left or right edge
                    image.setTranslationX(diff.x >= 0 ? screenWidth : -image.getWidth());
                    if (pos.y < 100) {
                        // Avoid clouds too close to the ground
                        image.setTranslationY(random.nextFloat() * (screenHeight / 2));
                    } else {
                        image.setTranslationY(random.nextFloat() * screenHeight);
                    }
                }
                backgroundObjects.put(image, random.nextDouble());
            }
        }

        // Step 3: move hittable objects
        for (FlyingObject flying : hittableObjects) {
            if (!move(diff, flying.image, 1)) {
                // This object is offscreen, so let's trash it and recycle the view.
                Log.d(TAG, "tick: Deleted flyingObject");
                hittableObjects.remove(flying);
                recycledImages.add(flying.image);
            }
        }

        // Step 4: create new hittable objects if needed
        if (hittableObjects.size() == 0 || (hittableObjects.size() <= MAX_HITTABLE_OBJECT_COUNT && random.nextInt(11) > HITTABLE_PROBABILITY)) {
            final ImageView image = recycledImages.isEmpty() ? createAndAttachImageView(hittableContainer) : recycledImages.poll();
            int x, y;
            switch (random.nextInt(3)) {
                case 0:
                    // Init somewhere around left or right edge
                    x = (diff.x >= 0 ? screenWidth : -image.getWidth());
                    if (pos.y < 100) {
                        // Avoid clouds too close to the ground
                        y = (int) (random.nextFloat() * screenHeight / 2);
                    } else {
                        y = (int) (random.nextFloat() * screenHeight);
                    }
                    break;
                case 1:
                    // Init somewhere around top edge
                    y = -image.getHeight();
                    x = (int) (screenWidth * random.nextFloat());
                    break;
                case 2:
                    x = (int) (screenWidth * random.nextFloat());
                    if (pos.y > ground[0].getHeight()) {
                        // Init somewhere around bottom edge (unless we are on the ground)
                        y = screenHeight;
                    } else {
                        // Fallback to top edge
                        y = -image.getHeight();
                    }
                    break;
                default:
                    Log.wtf(TAG, "Got invalid value from random number generator");
                    return;
            }
            Log.d(TAG, "tick: Created flyingobject at " + x + "," + y);
            hittableObjects.add(FlyingObject.create(image, x, y));
        }
    }

    /**
     * Removes the given flying object from the screen. The image view might be recycled.
     *
     * @param flying Something that files
     * @return true if the flying object was onscreen, false otherwise
     */
    public boolean removeFlyingObject(FlyingObject flying) {
        if (!hittableObjects.remove(flying)) return false;
        Log.d(TAG, "removeFlyingObject() called with: flying = [" + flying + "]");
        flying.image.setTranslationX(-1000f);
        flying.image.setTranslationY(-1000f);
        recycledImages.add(flying.image);
        return true;
    }

    /**
     * Creates a new image view, and adds it to the given container.
     * Please recycle your imageviews when possible.
     *
     * @param parent A non-null viewGroup
     * @return a brand new imageView.
     */
    private ImageView createAndAttachImageView(ViewGroup parent) {
        ImageView imageView = new ImageView(parent.getContext());
        parent.addView(imageView);
        return imageView;
    }

    /**
     * Moves the given image view
     *
     * @param diff       how far it should be moved in either direction
     * @param image      the imageview to move
     * @param multiplier defaults to 1
     * @return false if the object will go offscreen. If so, it should be recycled
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
