package no.ntnu.prisonesc.powerups;

import android.widget.ImageView;

import no.ntnu.prisonesc.Player;
import no.ntnu.prisonesc.Point;

/**
 * Created by Henrik on 18.04.2017.
 * Gir mer horisontal hastighet ut av kanonen.
 */

public class BetterHorizontalCannon extends Powerup {

    public BetterHorizontalCannon(int level) {
        super(level);
        init();
    }

    public BetterHorizontalCannon(int level, int maxLevel) {
        super(level, maxLevel);
        init();
    }

    private void init() {
        basePrice = 100;
        initialCondition = true;
        name = "Cannon power";
    }

    @Override
    public void apply(Player player, ImageView playerImageView) {
        player.addVel(new Point(level * 15, 0));
    }

}
