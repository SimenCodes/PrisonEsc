package no.ntnu.prisonesc.powerups;

import no.ntnu.prisonesc.Player;

/**
 * Created by Henrik on 18.04.2017.
 */

public class Clothes extends Powerup {

    public Clothes(int level) {
        this.level = level;
        this.maxLevel = 5;
        this.basePrice = 0;
        this.priceScale = 4000;
        this.initialCondition = true;
        this.name = "Clothes";
    }

    @Override
    public void apply(Player player) {
        player.imageSelector.setClothingLevel(level);
        player.reduceAirResistance(0.0002 * level);
        player.addGliderFactor(0.002 * level);
    }
}
