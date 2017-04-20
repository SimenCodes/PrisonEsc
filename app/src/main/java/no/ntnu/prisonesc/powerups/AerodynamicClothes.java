package no.ntnu.prisonesc.powerups;

import android.widget.ImageView;

import no.ntnu.prisonesc.Player;
import no.ntnu.prisonesc.R;

/**
 * Created by Henrik on 18.04.2017.
 */

public class AerodynamicClothes extends Powerup {
    public AerodynamicClothes(int level) {
        super(level);
        init();
    }

    public AerodynamicClothes(int level, int maxLevel) {
        super(level, maxLevel);
        init();
    }

    private void init() {
        basePrice = 500;
        initialCondition = true;
        name = "Clothes";
    }

    @Override
    public void apply(Player player, ImageView playerImageView) {
        switch (level) {
            case 0:
                playerImageView.setImageResource(R.drawable.prisoner_1);
                break;
            case 1:
                playerImageView.setImageResource(R.drawable.prisoner_2);
                break;
            case 2:
                playerImageView.setImageResource(R.drawable.prisoner_3);
                break;
        }
        player.reduceAirResistance(0.0002);
    }
}
