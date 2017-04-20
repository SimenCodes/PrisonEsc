package no.ntnu.prisonesc.powerups;

import android.widget.ImageView;

import no.ntnu.prisonesc.Player;

/**
 * Created by Henrik on 20.04.2017.
 */

public class RocketPower extends Powerup {

    private static final int POWER = 1;

    public RocketPower(int level) {
        this.level = level;
        this.maxLevel = 10;
        this.basePrice = 200;
        this.initialCondition = true;
        this.name = "Rocket Power";
    }

    @Override
    public void apply(Player player, ImageView playerImageView) {
        player.addRocketPower(level * POWER);
    }
}
