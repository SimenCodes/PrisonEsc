package no.ntnu.prisonesc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        this.powerups.add(new MockPowerup(0));
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
}
