package no.ntnu.prisonesc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.ntnu.prisonesc.powerups.AerodynamicClothes;
import no.ntnu.prisonesc.powerups.BetterHorizontalCannon;
import no.ntnu.prisonesc.powerups.BetterVerticalCannon;
import no.ntnu.prisonesc.powerups.MockPowerup;
import no.ntnu.prisonesc.powerups.Powerup;

/**
 * SaveData handle the saving of progress in the application
 */

public class SaveData {

    public static SaveData saveData;
    private SharedPreferences data;

    private String moneyString = "money";

    private int money;
    private List<Powerup> powerups;
    {
        //ADD new Powerups to this list for saving purposes
        this.powerups = new ArrayList<>();
        this.powerups.add(new AerodynamicClothes(0, 3));
        this.powerups.add(new BetterVerticalCannon(0));
        this.powerups.add(new BetterHorizontalCannon(0));
    }

    private SaveData(Context context) {
        data = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            //load data from the preferences
            money = data.getInt(moneyString, 0);
            String prefix = "p";
            for (int i = 0; i < this.powerups.size(); i++) {
                String tmp = prefix + i;
                int n = data.getInt(tmp, 0);
                this.powerups.get(i).setLevel(n);
            }
        } catch (ClassCastException e) {
            //cant load because it is the first time. Create from default
            money = 0;
            // Nothing for the powerups, because they are created with the default values before the constructor
        }
    }


    /**
     * Get the current savedata from the users phone. Only one can be made.
     *
     * @param context android context
     * @return instance of SaveData
     */
    public static SaveData getData(Context context) {
        if (saveData == null) {
            saveData = new SaveData(context);
        }
        return saveData;
    }

    public int getMoney() {
        return money;
    }

    public Collection<Powerup> getPowerups(){ return powerups;}

    /**
     * Update savedata with updated data
     */
    private void updateData() {
        SharedPreferences.Editor editor = data.edit();
        editor.putInt(moneyString, money);
        String prefix = "p";
        for (int i = 0; i < this.powerups.size(); i++) {
            String key = prefix + i;
            editor.putInt(key, this.powerups.get(i).getLevel());
        }
        editor.apply();
    }

    public Collection<Powerup> getBoughtPowerups() {
        ArrayList<Powerup> boughtPowerups = new ArrayList<>();
        for (int i = 0; i < this.powerups.size(); i++) {
            if(this.powerups.get(i).isBought()){
                boughtPowerups.add(this.powerups.get(i));
            }
        }
        return boughtPowerups;
    }

    public Collection<Powerup> getInitialConditionPowerups(){
        ArrayList<Powerup> initialCondition = new ArrayList<>();
        for (int i = 0; i < this.powerups.size(); i++){
            if (this.powerups.get(i).isInitialCondition()){
                initialCondition.add(this.powerups.get(i));
            }
        }
        return initialCondition;
    }

    public void updatePowerups(Collection<Powerup> newPowerups){
        List<Powerup> newPowerupList = new ArrayList<>(newPowerups);
        for (int i = 0; i < this.powerups.size(); i++) {
            powerups.get(i).setLevel(newPowerupList.get(i).getLevel());
        }
        updateData();
    }

    /**
     * add/remove money to the users account
     *
     * @param add money to add/remove
     */
    public void addMoney(int add) {
        if (money + add < 0) {
            throw new OutOfFundsException(-(money + add));
        }
        money += add;
        updateData();
    }

    public void doPurchase(int price) throws OutOfFundsException{
        if (money - price < 0) {
            throw new OutOfFundsException(price - money);
        }
        money -= price;
        updateData();
    }

    /**
     * Throws whenever the user tries to buy something they can't afford.
     * Vi vil vel ikke at folk skal havne i luksusfellen etter å ha spilt det her?
     */
    public class OutOfFundsException extends RuntimeException {
        public OutOfFundsException(int difference) {
            super("You need " + difference + " more coins to buy this");
        }
    }
}
