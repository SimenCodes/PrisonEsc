package no.ntnu.prisonesc;

/**
 * Powerup interface
 */

public interface Powerup {

    void apply(Player player);

    int getPrice();

    boolean isInitialCondition();

    int getLevel();

    boolean isBought();

    void setLevel(int n);
}
