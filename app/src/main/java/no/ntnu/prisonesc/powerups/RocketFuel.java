package no.ntnu.prisonesc.powerups;

import no.ntnu.prisonesc.Player;

/**
 * Created by Henrik on 18.04.2017.
 */

public class RocketFuel extends Powerup {

    public RocketFuel(int level) {
        this.level = level;
        this.maxLevel = 10;
        this.basePrice = 1000;
        this.priceScale = 1000;
        this.initialCondition = true;
        this.name = "Rocketfuel (This one can melt steel beams)";
    }

    @Override
    public void apply(Player player) {
        player.addRocketDuration(level * 5);
    }
}
