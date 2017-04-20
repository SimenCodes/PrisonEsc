package no.ntnu.prisonesc.powerups;

import no.ntnu.prisonesc.Player;
import no.ntnu.prisonesc.Point;

/**
 * Created by even on 20.04.17.
 */

public class CannonPower extends Powerup {

    private static final int POWER = 15;

    public CannonPower(int level) {
        this.level = level;
        this.maxLevel = 10;
        this.basePrice = 500;
        this.priceScale = 500;
        this.initialCondition = true;
        this.name = "Cannon power";
    }

    @Override
    public void apply(Player player) {
        player.addVel(new Point(level * POWER, level * POWER));
    }
}
