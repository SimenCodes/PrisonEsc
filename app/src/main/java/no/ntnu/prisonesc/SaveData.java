package no.ntnu.prisonesc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collection;

/**
 * SaveData handle the saving of progress in the application
 */

public class SaveData {

    public static SaveData saveData;
    private SharedPreferences data;

    private String moneyString = "money";

    private int money;

    private SaveData(Context context) {
        data = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            //load data from the preferences
            money = data.getInt(moneyString, 0);
        } catch (ClassCastException e) {
            //cant load because it is the first time. Create from default
            money = 0;
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

    /**
     * Update savedata with updated data
     */
    private void updateData() {
        SharedPreferences.Editor editor = data.edit();
        editor.putInt(moneyString, money);
        //TODO add stuff to save;
    }

    public Collection<Powerup> getBoughtPowerups() {
        //TODO actualy return a list of bought powerups
        return new ArrayList<Powerup>();
    }
}
