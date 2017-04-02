package no.ntnu.prisonesc;

/**
 * Created by even on 02.04.17.
 */

public interface Powerup {

    void apply(Player);

    int getPrice();

    boolean isInitialCondition();
}
