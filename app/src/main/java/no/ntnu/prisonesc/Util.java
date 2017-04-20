package no.ntnu.prisonesc;

import android.content.Context;

/**
 * Created by simen on 20.04.17.
 */

public class Util {
    private static int pixelSize = 0;

    public static int pixelSize(Context context) {
        if (pixelSize == 0)
            pixelSize = context.getResources().getDimensionPixelSize(R.dimen.world_scale_factor);
        return pixelSize;
    }
}
