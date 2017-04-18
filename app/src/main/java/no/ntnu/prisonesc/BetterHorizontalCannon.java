package no.ntnu.prisonesc;

/**
 * Created by Henrik on 18.04.2017.
 * Gir mer horisontal hastighet ut av kanonen.
 */

public class BetterHorizontalCannon extends Powerup {

    public BetterHorizontalCannon(int level) {
        super(level);
        init();
    }

    public BetterHorizontalCannon(int level, int maxLevel) {
        super(level, maxLevel);
        init();
    }

    private void init() {
        basePrice = 100;
        initialCondition = true;
    }

    @Override
    void apply(Player player, GameActivity gameAcitivity) {
        player.addVel(new Point(level * 15, 0));
    }

}
