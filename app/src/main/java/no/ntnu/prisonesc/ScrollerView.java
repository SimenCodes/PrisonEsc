package no.ntnu.prisonesc;

import android.content.Context;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

import static no.ntnu.prisonesc.R.id.hittable;

/**
 * This class handles everything in the game that scrolls.
 */
@SuppressWarnings("deprecation")
public class ScrollerView extends FrameLayout {
    public static final String TAG = "ScrollerView";

    private static final int MAX_HITTABLE_OBJECT_COUNT = 5;
    private static final double HITTABLE_PROBABILITY = 9;
    private static final boolean PERSPECTIVE_ENABLED = true; // disable cloud/backgroundObject perspective when set to false
    private static final int CLOUD_COUNT = 5;

    private Map<ImageView, Double> backgroundObjects = new ArrayMap<>();
    private List<FlyingObject> hittableObjects;
    private Point screenScaledPlayerPos = new Point(0, 0);
    private Random random = new Random();
    private boolean isReadyForTick = false;
    /**
     * One unit in our world (hereby called World Unit "wu") is one density-independent pixel.
     * This means that on a 5" FHD phone, we see 640wu in X direction and 360wu in Y direction.
     */
    private int scaleFactor;
    private ImageView[] ground = new ImageView[2];
    private AbsoluteLayout backgroundContainer, hittableContainer;
    // A temporary place to store offscreen imageviews for reuse
    private Queue<ImageView> recycledImages = new ArrayBlockingQueue<>(MAX_HITTABLE_OBJECT_COUNT);

    {
        scaleFactor = getResources().getDimensionPixelSize(R.dimen.world_scale_factor);
        Log.d(TAG, "instance initializer: scaleFactor=" + scaleFactor);
    }

    public ScrollerView(Context context) {
        super(context);
        this.hittableObjects = new ArrayList<>();
        init();
    }

    public ScrollerView(Context context, List<FlyingObject> hittableObjects) {
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
    }

    public void delayedInit() {
        if (isReadyForTick) return;
        Log.d(TAG, "delayedInit() called");
        ground[1].setTranslationX(ground[0].getWidth());
        for (int i = 0; i < CLOUD_COUNT; i++) {
            ImageView imageView = createAndAttachImageView(backgroundContainer);
            imageView.setTranslationX(getWidth() * random.nextFloat());
            imageView.setTranslationY((getHeight() - ground[0].getHeight()) * random.nextFloat());
            if (i % 2 == 0) imageView.setImageResource(R.drawable.cloud_1);
            else imageView.setImageResource(R.drawable.cloud_2);
            backgroundObjects.put(imageView, random.nextDouble());
        }
        isReadyForTick = true;
    }

    /**
     * Call this every clock tick. It will do all the scrolling.
     * It's long with mane magic numbers, but you'll make it!
     *
     * @param playerPos The new position to draw
     */
    public void tick(Point playerPos) {
        if (!isReadyForTick) return;
        Point diff = new Point(playerPos.x * scaleFactor - screenScaledPlayerPos.x, playerPos.y * scaleFactor - screenScaledPlayerPos.y);
        screenScaledPlayerPos = new Point(playerPos.x * scaleFactor, playerPos.y * scaleFactor);

        final int screenWidth = getWidth(), screenHeight = getHeight();

        // Step 1: move ground if it's going to be visible
        for (ImageView ground : this.ground) {
            if (screenScaledPlayerPos.y > ground.getHeight()) {
                ground.setTranslationY(ground.getHeight());
            } else {
                ground.setTranslationX(ground.getTranslationX() - diff.x);
                ground.setTranslationY(screenScaledPlayerPos.y);
                if (ground.getTranslationX() < -ground.getWidth()) {
                    ground.setTranslationX(2 * ground.getWidth() + ground.getTranslationX());
                }
            }
        }

        // Step 2: move clouds/background
        for (ImageView image : backgroundObjects.keySet()) {
            if (!move(diff, image, backgroundObjects.get(image))) {
                if (random.nextBoolean()) {
                    // Init somewhere around top or bottom edge
                    image.setTranslationX(screenWidth * random.nextFloat());
                    if (screenScaledPlayerPos.y > ground[0].getHeight() * 4 && diff.y < 0) {
                        image.setTranslationY(screenHeight);
                    } else {
                        image.setTranslationY(-image.getHeight());
                    }
                } else {
                    // Init somewhere around left or right edge
                    image.setTranslationX(diff.x >= 0 ? screenWidth : -image.getWidth());
                    if (screenScaledPlayerPos.y < ground[0].getHeight() * 10) {
                        // Avoid clouds too close to the ground
                        image.setTranslationY(random.nextFloat() * (screenHeight / 2));
                    } else {
                        image.setTranslationY(random.nextFloat() * screenHeight);
                    }
                }
                final double closeness = random.nextDouble();
                image.setZ((float) closeness);
                backgroundObjects.put(image, closeness);
            }
        }

        // Step 3: move hittable objects
        for (int i = hittableObjects.size() - 1; i >= 0; i--) {
            FlyingObject flying = hittableObjects.get(i);
            if (!move(diff, flying.image, 1)) {
                // This object is offscreen, so let's trash it and recycle the view.
                hittableObjects.remove(flying);
                Log.d(TAG, "tick.flying.getPos: " + flying.getCenter() + " : " + playerPos);
                try {
                    recycledImages.add(flying.image);
                } catch (IllegalStateException e) {
                    Log.w(TAG, "tick: " + e);
                    hittableContainer.removeView(flying.image);
                }
            }
        }

        // Step 4: createFlyingObject new hittable objects if needed
        if (hittableObjects.size() == 0 || (hittableObjects.size() < MAX_HITTABLE_OBJECT_COUNT && random.nextInt(11) > HITTABLE_PROBABILITY)) {
            // disable hittable objects by commenting out this block
            final ImageView image = recycledImages.isEmpty() ? createAndAttachImageView(hittableContainer) : recycledImages.poll();
            int x, y;
            switch (random.nextInt(3)) {
                case 0:
                    // Init somewhere around left or right edge
                    x = (diff.x >= 0 ? screenWidth : -image.getWidth());
                    if (screenScaledPlayerPos.y < 100) {
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
                    if (screenScaledPlayerPos.y > ground[0].getHeight()) {
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
            image.setTranslationX(x);
            image.setTranslationY(y);
            hittableObjects.add(createFlyingObject(image, fromScreenCoordinates(playerPos, x, y)));
        }
    }

    /**
     * Maps the given screenX and screenY screen coordinates to the corresponding point in the world
     *
     * @param screenX
     * @param screenY
     * @return
     */
    private Point fromScreenCoordinates(Point playerPos, int screenX, int screenY) {
        final int playerScreenX = getWidth() / (2 * scaleFactor);
        final int playerScreenY = getHeight() / (2 * scaleFactor);
        // Think of it as vectors; we start with a vector to the player,
        // then add a vector from the player to topleft
        // and finally add the vector from topleft to the desired position.
        return new Point(
                playerPos.x - playerScreenX + screenX / scaleFactor,
                playerPos.y - playerScreenY + screenY / scaleFactor
        );
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
        try {
            recycledImages.add(flying.image);
        } catch (IllegalStateException e) {
            Log.w(TAG, "removeFlyingObject: " + e);
            hittableContainer.removeView(flying.image);
        }
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
     * Creates a new flying object of random type.
     *
     * @param imageView An imageView that's attached to the correct parent, and NOT attached to another flyingobject
     * @param p         Position
     * @return The Flying Object®
     */
    private FlyingObject createFlyingObject(ImageView imageView, Point p) {
        if (random.nextBoolean())
            return new Balloon(p, imageView);
        else return new MoneyBalloon(p, imageView);
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
        if (!PERSPECTIVE_ENABLED) multiplier = 1;
        float y = (float) (image.getTranslationY() + diff.y * multiplier);
        float x = (float) (image.getTranslationX() - diff.x * multiplier);

        image.setTranslationY(y);
        image.setTranslationX(x);

        if (x < -image.getWidth()) return false;
        if (y < -image.getHeight()) return false;
        if (y > getHeight()) return false;
        return true;
    }
}
