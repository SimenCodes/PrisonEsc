package no.ntnu.prisonesc.powerups;

import android.support.annotation.NonNull;

import no.ntnu.prisonesc.GameActivity;
import no.ntnu.prisonesc.Player;

/**
 * Powerup interface
 */


public abstract class Powerup {

    protected int level;
    @NonNull
    protected int basePrice;
    @NonNull
    protected boolean initialCondition;
    protected int maxLevel;

    @NonNull
    protected String name;

    public Powerup(int level) {
        this(level, 10);
    }

    public Powerup(int level, int maxLevel) {
        this.level = level;
        this.maxLevel = maxLevel;
    }

    public String getName(){
        return this.name;
    }

    public abstract void apply(Player player, GameActivity gameActivity);

    public int getPrice(int levelsAheadOfCurrent) {
        return this.basePrice + 500 * (this.level + levelsAheadOfCurrent);
    }

    public boolean isInitialCondition() {
        return this.initialCondition;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int n) {
        if (n <= maxLevel) {
            this.level = n;
        }
    }

    public int getMaxLevel() {
        return this.maxLevel;
    }

    public boolean isBought() {
        return this.level > 0;
    }

    public void buy() {
        if (this.level <= this.maxLevel) {
            this.level++;
        } else {
            throw new IllegalStateException("Allready max Level");
        }
    }
}
