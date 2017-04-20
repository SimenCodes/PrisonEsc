package no.ntnu.prisonesc;

import android.support.annotation.DrawableRes;
import android.util.Log;

/**
 * Created by even on 20.04.17.
 */

public class PlayerImageSelector {
    private static final String TAG = "PlayerImageSelector";

    private boolean isFlying = false, hasRocket = false, hasWings = false;

    private int clothingLevel = 0;

    public void setFlying(boolean flying) {
        isFlying = flying;
    }

    public void setHasRocket(boolean hasRocket) {
        this.hasRocket = hasRocket;
    }

    public void setHasWings(boolean hasWings) {
        this.hasWings = hasWings;
    }

    public void setClothingLevel(int clothingLevel) {
        this.clothingLevel = clothingLevel;
    }

    @DrawableRes
    public int getImageResource() {
        if (isFlying) {
            if (hasRocket) {
                switch (clothingLevel) {
                    case 0:
                        return R.drawable.prisoner_1_rocket;
                    case 1:
                        return R.drawable.prisoner_2_rocket;
                    case 2:
                        return R.drawable.prisoner_3_rocket;
                    case 3:
                        return R.drawable.prisoner_4_rocket;
                    case 4:
                        return R.drawable.prisoner_5_rocket;
                }
            } else if (hasWings) {
                switch (clothingLevel) {
                    case 0:
                        return R.drawable.prisoner_1_flying_wings;
                    case 1:
                        return R.drawable.prisoner_2_flying_wings;
                    case 2:
                        return R.drawable.prisoner_3_flying_wings;
                    case 3:
                        return R.drawable.prisoner_4_flying_wings;
                    case 4:
                        return R.drawable.prisoner_5_flying_wings;
                }
            } else {
                switch (clothingLevel) {
                    case 0:
                        return R.drawable.prisoner_1_flying;
                    case 1:
                        return R.drawable.prisoner_2_flying;
                    case 2:
                        return R.drawable.prisoner_3_flying;
                    case 3:
                        return R.drawable.prisoner_4_flying;
                    case 4:
                        return R.drawable.prisoner_5_flying;
                }
            }
        } else {
            if (hasWings) {
                switch (clothingLevel) {
                    case 0:
                        return R.drawable.prisoner_1_face_wings;
                    case 1:
                        return R.drawable.prisoner_2_face_wings;
                    case 2:
                        return R.drawable.prisoner_3_face_wings;
                    case 3:
                        return R.drawable.prisoner_4_face_wings;
                    case 4:
                        return R.drawable.prisoner_5_face_wings;
                }
            } else {
                switch (clothingLevel) {
                    case 0:
                        return R.drawable.prisoner_1_face;
                    case 1:
                        return R.drawable.prisoner_2_face;
                    case 2:
                        return R.drawable.prisoner_3_face;
                    case 3:
                        return R.drawable.prisoner_4_face;
                    case 4:
                        return R.drawable.prisoner_5_face;
                }
            }
        }
        Log.e(TAG, "getImageResource: Can't find the right image!!!");
        return R.mipmap.ic_launcher;
    }
}
