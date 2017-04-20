package no.ntnu.prisonesc;

import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * Created by Henrik on 04.04.2017.
 */
//Bare lagde en implementasjon av Flyingobject for å kunne teste litt.
public class Balloon extends FlyingObject {

    public Balloon(Point position, @NonNull ImageView image) {
        super(position, image, getImageResource());
    }

    private static int getImageResource() {
        if (Math.random() > .5) return R.drawable.balloon_1;
        else if (Math.random() > .2) return R.drawable.balloon_2;
        else return R.drawable.balloon_dude;
    }

    @Override
    void onCollision(Player player) {
        player.addVel(new Point(-1, 5));
        player.addAccleration(5, 1);
    }
}
