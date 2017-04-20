package no.ntnu.prisonesc.powerups;

import android.widget.ImageView;

import no.ntnu.prisonesc.Player;

/**
 * Created by even on 20.04.17.
 */

public class StartAltitude extends Powerup {

    public StartAltitude(int level) {
        this.level = level;
        this.maxLevel = 20;
        this.basePrice = 100;
        this.initialCondition = true;
        this.name = "Cell floor";
    }

    @Override
    public void apply(Player player, ImageView playerImageView) {
        //TODO
    }
}
