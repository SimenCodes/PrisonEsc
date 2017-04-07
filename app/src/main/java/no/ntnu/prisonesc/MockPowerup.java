package no.ntnu.prisonesc;

/**
 * Mock powerup to test the saving, Can be used to test powerups in general
 */

public class MockPowerup implements Powerup {

    private int level;
    private int basePrice;
    private boolean initialCondition;
    private int maxLevel;

    public MockPowerup(int level) {
        new MockPowerup(level, 10);
    }
    public MockPowerup(int level, int maxLevel){
        this.level = level;
        this.maxLevel = maxLevel;
        //Harkodet for nå, Kan lages slik at den følger konstruktøren, men blir mye mer å lagre!
        this.basePrice = 100;
    }

    @Override
    public void apply(Player player) {
        //TODO HENRIK Aplly this to player
        // denne er individuel for alle de forskellige powerupene
    }

    @Override
    public int getPrice() {
        return this.basePrice + 500 * (this.level + 1);
    }

    @Override
    public boolean isInitialCondition() {
        return this.initialCondition;
    }

    @Override
    public int getLevel() {
        return this.level;
    }

    @Override
    public boolean isBought(){
        return this.level > 0;
    }

    @Override
    public void setLevel(int n) {
        if (n < maxLevel){
            this.level = n;
        }
    }

    public void buy(){
        if(this.level < this.maxLevel){
            this.level++;
        } else{
            throw new IllegalStateException("Allready max Level");
        }
    }
}
