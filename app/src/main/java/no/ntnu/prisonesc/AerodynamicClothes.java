package no.ntnu.prisonesc;

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
    }

    @Override
    void apply(Player player, GameActivity gameAcitivity) {
        //TODO Hente bildet som skal brukes for aerodynamisk spiller
        gameAcitivity.setDefaultPlayerImage(0);
        player.reduseAirResitance(0.0002f);
    }
}
