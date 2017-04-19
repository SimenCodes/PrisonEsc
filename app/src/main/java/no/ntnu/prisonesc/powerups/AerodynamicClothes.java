package no.ntnu.prisonesc.powerups;

import no.ntnu.prisonesc.GameActivity;
import no.ntnu.prisonesc.Player;

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
    public void apply(Player player, GameActivity gameAcitivity) {
        //TODO Hente bildet som skal brukes for aerodynamisk spiller
        gameAcitivity.setPlayerImage(0);
        player.reduceAirResistance(0.0002);
    }
}
