package no.ntnu.prisonesc.powerups;

import no.ntnu.prisonesc.GameActivity;
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
    }

    @Override
    public void apply(Player player, GameActivity gameAcitivity) {
        player.addVel(new Point(0, level * 15));
    }
}
