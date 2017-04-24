package no.ntnu.prisonesc.powerups;

import no.ntnu.prisonesc.Player;

/**
 * Created by even on 20.04.17.
 */

public class Wings extends Powerup {

    public Wings(int level) {
        this.level = level;
        this.maxLevel = 2;
        this.basePrice = 0;
        this.priceScale = 50000;
        this.initialCondition = true;
        this.name = "Wings of Eternity";
    }

    @Override
    public void apply(Player player) {
        player.imageSelector.setHasWings(level > 0);
        player.addGliderFactor(0.2f);
    }
}
