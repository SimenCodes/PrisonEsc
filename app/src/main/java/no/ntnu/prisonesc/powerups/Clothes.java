package no.ntnu.prisonesc.powerups;

import android.widget.ImageView;

import no.ntnu.prisonesc.Player;
import no.ntnu.prisonesc.R;

/**
 * Created by Henrik on 18.04.2017.
 */

public class Clothes extends Powerup {

    public Clothes(int level) {
        this.level = level;
        this.maxLevel = 3;
        this.basePrice = 1000;
        this.priceScale = 4000;
        this.initialCondition = true;
        this.name = "Clothes";
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
