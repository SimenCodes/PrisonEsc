package no.ntnu.prisonesc;

/**
 * Created by Henrik on 18.04.2017.
 */

public class BetterVerticalCannon extends Powerup {
    public BetterVerticalCannon(int level) {
        super(level);
        init();
    }

    public BetterVerticalCannon(int level, int maxLevel) {
        super(level, maxLevel);
        init();
    }

    private void init() {
        basePrice = 100;
        initialCondition = true;
    }

    @Override
    void apply(Player player) {
        player.addVel(new Point(0, level * 15));
    }
}
