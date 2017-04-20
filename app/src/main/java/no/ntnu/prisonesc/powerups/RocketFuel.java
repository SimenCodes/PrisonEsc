package no.ntnu.prisonesc.powerups;

import android.widget.ImageView;

import no.ntnu.prisonesc.Player;

/**
 * Created by Henrik on 18.04.2017.
 */

public class RocketFuel extends Powerup {

    public RocketFuel(int level) {
        this.level = level;
        this.maxLevel = 10;
        this.basePrice = 1000;
        this.initialCondition = false;
        this.name = "Rocketfuel (This one can melt steal beams)";
    }

    @Override
    public void apply(Player player, ImageView playerImageView) {
        //TODO Hente bildet som skal brukes når vi har en aktiv rakett
        int placeholderForImage = 0;
        playerImageView.setImageResource(placeholderForImage);
        player.addAccleration(level * 2, level);
    }
}
