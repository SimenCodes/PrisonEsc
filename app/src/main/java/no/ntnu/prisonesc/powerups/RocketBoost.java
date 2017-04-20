package no.ntnu.prisonesc.powerups;

import android.widget.ImageView;

import no.ntnu.prisonesc.Player;

/**
 * Created by Henrik on 18.04.2017.
 */

public class RocketBoost extends Powerup {
    public RocketBoost(int level) {
        super(level);
        init();
    }

    public RocketBoost(int level, int maxLevel) {
        super(level, maxLevel);
        init();
    }

    private void init() {
        basePrice = 400;
        initialCondition = false;
        name = "Rocket";
    }

    @Override
    public void apply(Player player, ImageView playerImageView) {
        //TODO Hente bildet som skal brukes når vi har en aktiv rakett
        int placeholderForImage = 0;
        playerImageView.setImageResource(placeholderForImage);
        player.addAccleration(level * 2, level);
    }
}
