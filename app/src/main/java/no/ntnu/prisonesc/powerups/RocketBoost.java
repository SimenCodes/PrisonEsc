package no.ntnu.prisonesc.powerups;

import android.widget.ImageView;

import no.ntnu.prisonesc.Player;

/**
 * Created by Henrik on 18.04.2017.
 */

public class RocketBoost extends Powerup {

    public RocketBoost(int level) {
        this.level = level;
        this.maxLevel = 2;
        this.basePrice = 5000;
        this.initialCondition = false;
        this.name = "Rocket";
    }

    @Override
    public void apply(Player player, ImageView playerImageView) {
        //TODO Hente bildet som skal brukes når vi har en aktiv rakett
        int placeholderForImage = 0;
        playerImageView.setImageResource(placeholderForImage);
        player.addAccleration(level * 2, level);
    }
}
