package no.ntnu.prisonesc;

import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * Created by Henrik on 04.04.2017.
 */
//Bare lagde en implementasjon av Flyingobject for å kunne teste litt.
public class Balloon extends FlyingObject {

    public Balloon(int x, int y, int width, int height, @NonNull ImageView image) {
        super(x, y, width, height, image);
        image.setImageResource(R.mipmap.ic_launcher_round);
    }

    @Override
    void onCollision(Player player) {
        player.addVel(new Point(0, 10));
    }
}
