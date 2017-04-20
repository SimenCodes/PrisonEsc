package no.ntnu.prisonesc.powerups;

import no.ntnu.prisonesc.Player;

/**
 * Created by even on 20.04.17.
 */

public class StartAltitude extends Powerup {
    private static final int HEIGHTFACTOR = 15;

    public StartAltitude(int level) {
        this.level = level;
        this.maxLevel = 20;
        this.basePrice = -500;
        this.priceScale = 700;
        this.initialCondition = true;
        this.name = "Cell floor";
    }

    @Override
    public void apply(Player player) {
        player.addStartHeight(HEIGHTFACTOR * level);
    }
}
