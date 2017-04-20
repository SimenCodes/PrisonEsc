package no.ntnu.prisonesc.powerups;

import no.ntnu.prisonesc.GameActivity;
import no.ntnu.prisonesc.Player;

/**
 * Created by Henrik on 18.04.2017.
 */

public class RocketFuel extends Powerup {

    public static final int FUEL_GREATNESS = 10; // Probably not a good name

    public RocketFuel(int level) {
        this.level = level;
        this.maxLevel = 10;
        this.basePrice = 0;
        this.priceScale = 1000;
        this.initialCondition = true;
        this.name = "Rocketfuel (This one can melt steel beams)";
    }

    @Override
    public void apply(Player player) {
        player.addRocketDuration(getFuel());
    }

    private int getFuel() {
        return level * FUEL_GREATNESS + 20;
    }

    public int getDuration() {
        return getFuel() * GameActivity.GAME_FRAME_RATE;
    }
}
