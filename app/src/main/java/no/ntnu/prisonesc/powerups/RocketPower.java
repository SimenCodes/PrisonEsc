package no.ntnu.prisonesc.powerups;

import android.widget.ImageView;

import no.ntnu.prisonesc.Player;
import no.ntnu.prisonesc.Point;

/**
 * Created by Henrik on 20.04.2017.
 */

public class RocketPower extends Powerup {

    private static final int POWER = 1;

    public RocketPower(int level) {
        this.level = level;
        this.maxLevel = 10;
        this.basePrice = 200;
        this.priceScale = 600;
        this.initialCondition = false;
        this.name = "Rocket Power";
    }

    @Override
    public void apply(Player player, ImageView playerImageView) {
        player.addRocketBoost(new Point((float) Math.cos(player.getRot().getRad()) * level * POWER, (float) Math.sin(player.getRot().getRad()) * level * POWER));
    }
}
