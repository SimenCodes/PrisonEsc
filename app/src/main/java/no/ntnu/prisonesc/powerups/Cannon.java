package no.ntnu.prisonesc.powerups;

import android.widget.ImageView;

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
        this.priceScale = 5000;
        this.initialCondition = false;
        this.name = "Cannon";
    }

    @Override
    public void apply(Player player, ImageView playerImageView) {
        player.addVel(new Point(100 * level, 100 * level));
    }
}
