package no.ntnu.prisonesc.powerups;

import android.support.annotation.NonNull;

import no.ntnu.prisonesc.Player;

/**
 * Powerup interface
 */


public abstract class Powerup {

    protected int level;
    protected int maxLevel;

    @NonNull
    protected int priceScale;

    @NonNull
    protected int basePrice;

    @NonNull
    protected boolean initialCondition;

    @NonNull
    protected String name;

    public String getName() {
        return this.name;
    }

    public int getPrice(int levelsAheadOfCurrent) {
        return this.basePrice + priceScale * (this.level + levelsAheadOfCurrent);
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int n) {
        if (n < maxLevel) {
            this.level = n;
        }
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public boolean isBought() {
        return this.level > 0;
    }

    public boolean isInitialCondition() {
        return this.initialCondition;
    }

    public void buy() {
        if (this.level < this.maxLevel) {
            this.level++;
        } else {
            throw new IllegalStateException("Allready max Level");
        }
    }

    public abstract void apply(Player player);
}
