package no.ntnu.prisonesc;

import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * Created by Henrik on 02.04.2017.
 */

public abstract class FlyingObject {

    public final int x;//x koordinaten
    public final int y;//y koordinaten
    public final int w;// bredden
    public final int h;//høyden
    @NonNull
    public final ImageView image;

    public FlyingObject(int x, int y, int widht, int height, ImageView image) {
        this.x = x;
        this.y = y;
        this.w = widht;
        this.h = height;
        this.image = image;
    }

    abstract void onColission(Player player);

    public void delete() {

    }
}
