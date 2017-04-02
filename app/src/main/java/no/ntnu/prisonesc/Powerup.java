package no.ntnu.prisonesc;

/**
 * Powerup interface
 */

public interface Powerup {

    void apply(Player player);

    int getPrice();

    boolean isInitialCondition();
}
