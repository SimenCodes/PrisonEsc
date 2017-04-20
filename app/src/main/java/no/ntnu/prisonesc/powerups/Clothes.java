package no.ntnu.prisonesc.powerups;

import no.ntnu.prisonesc.Player;

/**
 * Created by Henrik on 18.04.2017.
 */

public class Clothes extends Powerup {

    public Clothes(int level) {
        this.level = level;
        this.maxLevel = 3;
        this.basePrice = 1000;
        this.priceScale = 4000;
        this.initialCondition = true;
        this.name = "Clothes";
    }

    @Override
    public void apply(Player player) {
        player.imageSelector.setClothingLevel(level);
        player.reduceAirResistance(0.0002);
    }
}
