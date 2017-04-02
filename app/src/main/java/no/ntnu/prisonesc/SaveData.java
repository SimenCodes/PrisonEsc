package no.ntnu.prisonesc;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SaveData handle the saving of progress in the application
 */

public class SaveData {

    public static SaveData saveData;
    private SharedPreferences data;

    public SaveData(Context context){
        data = PreferenceManager.getDefaultSharedPreferences(context);
        try{
            //load data from the preferences
        } catch (ClassCastException e){
            //cant load because it is the first time. Create from default
        }
    }


    /**
     * Get the current savedata from the users phone. Only one can be made.
     * @param context android context
     * @return instance of SaveData
     */
    public static SaveData getData(Context context){
        if(saveData == null){
            saveData = new SaveData(context);
        }
        return saveData;
    }


    /**
     * Update savedata with updated data
     */
    private void updateData(){
        SharedPreferences.Editor editor = data.edit();
        //TODO add stuff to save;
    }

    public Collection<Powerup> getBoughtPowerups(){
        return new ArrayList<Powerup>();
    }

}
