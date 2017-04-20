package no.ntnu.prisonesc.powerups;

import android.widget.ImageView;

import no.ntnu.prisonesc.Player;

/**
 * Created by even on 20.04.17.
 */

public class Wings extends Powerup {

    public Wings(int level) {
        this.level = level;
        this.maxLevel = 2;
        this.basePrice = 0;
        this.priceScale = 10000;
        this.initialCondition = true;
        this.name = "Wings";
    }

    @Override
    public void apply(Player player, ImageView playerImageView) {
        //TODO Legge inn håndtering av bilde.
        player.addGliderFactor(0.5f);
    }
}
