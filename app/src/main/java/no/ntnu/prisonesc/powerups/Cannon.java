package no.ntnu.prisonesc.powerups;

import no.ntnu.prisonesc.Player;
import no.ntnu.prisonesc.Point;

/**
 * Created by even on 20.04.17.
 */

public class Cannon extends Powerup {

    public Cannon(int level) {
        this.level = level;
        this.maxLevel = 2;
        this.basePrice = 0;
        this.priceScale = 500;
        this.initialCondition = true;
        this.name = "Cannon";
    }

    @Override
    public void apply(Player player) {
        player.addVel(new Point(100 * level, 100 * level));
    }
}
