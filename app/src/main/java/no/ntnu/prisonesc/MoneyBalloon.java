package no.ntnu.prisonesc;

import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * Created by simen on 20.04.17.
 */

@SuppressWarnings("WeakerAccess")
public class MoneyBalloon extends FlyingObject {
    public static final int VALUE = 10;

    public MoneyBalloon(Point position, @NonNull ImageView image) {
        super(position, image, R.drawable.balloon_money);
    }

    @Override
    void onCollision(Player player) {
        //player.addVel(new Point(-1, 0));
        player.collectMoneyBalloon();
        Audio.play(image.getContext(), Audio.MONEY);
    }
}
