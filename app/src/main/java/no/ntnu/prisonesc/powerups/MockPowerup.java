package no.ntnu.prisonesc.powerups;

import android.widget.ImageView;

import no.ntnu.prisonesc.Player;
import no.ntnu.prisonesc.Point;

/**
 * Mock powerup to test the saving, Can be used to test powerups in general
 */

public class MockPowerup extends Powerup {

    private int level;
    private int basePrice;
    private boolean initialCondition;
    private int maxLevel;

    public MockPowerup(int level) {
        super(level);
    }
    public MockPowerup(int level, int maxLevel){
        super(level, maxLevel);
        //Harkodet for nå, Kan lages slik at den følger konstruktøren, men blir mye mer å lagre!
        this.basePrice = 100;
        this.initialCondition = true;
    }

    @Override
    public void apply(Player player, ImageView playerImageView) {
        player.addVel(new Point(50, 150));
        // denne er individuel for alle de forskellige powerupene
    }

}
