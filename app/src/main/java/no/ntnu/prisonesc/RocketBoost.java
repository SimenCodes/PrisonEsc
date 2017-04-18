package no.ntnu.prisonesc;

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
    }

    @Override
    void apply(Player player, GameActivity gameAcitivity) {
        //TODO Hente bildet som skal brukes når vi har en aktiv rakett
        int placeholderForImage = 0;
        gameAcitivity.changePlayerImage(placeholderForImage);
        player.addAccleration(level * 2, level);
    }
}
