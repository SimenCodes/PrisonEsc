package no.ntnu.prisonesc;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.util.ArraySet;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.widget.AbsoluteLayout;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Map;
import java.util.Set;

/**
 * Created by simen on 02.04.17.
 */

@SuppressWarnings("deprecation")
public class ScrollerView extends FrameLayout {
    public static final int HITTABLE_OBJECT_COUNT = 2;

    Map<Drawable, Integer> backgroundObjects = new ArrayMap<>();
    Set<FlyingObject> hittableObjects;
    Point pos = new Point(0, 0);

    ImageView[] ground = new ImageView[2];
    AbsoluteLayout backgroundContainer, hittableContainer;

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
    }

    public void tick(Point p) {
        Point diff = pos.diff(p);

    }
}
