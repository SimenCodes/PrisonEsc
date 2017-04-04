package no.ntnu.prisonesc;

import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

/**
 * Created by simen on 04.04.17.
 */

class Balloon extends FlyingObject {
    public Balloon(int x, int y, int width, int height, @NonNull ImageView image) {
        super(x, y, width, height, image);
        image.setImageResource(R.mipmap.ic_launcher_round);
    }

    @Override
    void onCollision(Player player) {
        Log.d(TAG, "onCollision: Hit a player!!");
    }
}
