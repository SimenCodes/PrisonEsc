package no.ntnu.prisonesc.powerups;

import android.widget.ImageView;

import no.ntnu.prisonesc.Player;
import no.ntnu.prisonesc.Point;

/**
 * Created by Henrik on 18.04.2017.
 */

public class BetterVerticalCannon extends Powerup {
    public BetterVerticalCannon(int level) {
        super(level);
        init();
    }

    public BetterVerticalCannon(int level, int maxLevel) {
        super(level, maxLevel);
        init();
    }

    private void init() {
        basePrice = 100;
        initialCondition = true;
        name = "Cannon Height Power";
    }

    @Override
    public void apply(Player player, ImageView playerImageView) {
        player.addVel(new Point(0, level * 15));
    }
}
