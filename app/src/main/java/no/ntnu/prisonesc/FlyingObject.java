package no.ntnu.prisonesc;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

/**
 * Created by Henrik on 02.04.2017.
 */

public abstract class FlyingObject {

    public final int x;//x koordinaten
    public final int y;//y koordinaten
    public final int w;//bredden
    public final int h;//høyden
    @NonNull
    public final Drawable image;

    public FlyingObject(int x, int y, int widht, int height, Drawable image) {
        this.x = x;
        this.y = y;
        this.w = widht;
        this.h = height;
        this.image = image;
    }

    abstract void onColission(Player player);

}
