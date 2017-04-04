package no.ntnu.prisonesc;

import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * Created by Henrik on 04.04.2017.
 */
//Bare lagde en implementasjon av Flyingobject for å kunne teste litt.
public class Ballon extends FlyingObject {

    public Ballon(int x, int y, int widht, int height, @NonNull ImageView image) {
        super(x, y, widht, height, image);
    }

    @Override
    void onCollision(Player player) {
        player.addVel(new Point(0, 10));
    }
}
