package no.ntnu.prisonesc;

import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.util.Log;
import android.widget.ImageView;

import junit.framework.Assert;

import org.junit.Test;

import static no.ntnu.prisonesc.ScrollerView.TAG;

/**
 * Created by Henrik on 02.04.2017.
 */
public class GameActivityTest {
    @Test
    public void isCollision() throws Exception {
        FlyingObject FO1 = new FlyingObject(5, 5, 10, 10, new ImageView(InstrumentationRegistry.getTargetContext())) {
            @Override
            void onColission(Player player) {
                Log.d(TAG, "onColission: test");
            }
        };
        FlyingObject FO2 = new FlyingObject(11, 11, 10, 10, new ImageView(InstrumentationRegistry.getTargetContext())) {
            @Override
            void onColission(Player player) {
                Log.d(TAG, "onColission: test");
            }
        };
        FlyingObject FO3 = new FlyingObject(5, 6, 10, 10, new ImageView(InstrumentationRegistry.getTargetContext())) {
            @Override
            void onColission(Player player) {
                Log.d(TAG, "onColission: test");
            }
        };
        Looper.prepare();
        Player player = new Player(InstrumentationRegistry.getTargetContext().getResources().getDrawable(R.mipmap.ic_launcher, null), 0.2, 0, 15, 0, 0);
        GameActivity test = new GameActivity();
        Assert.assertTrue("Tests when the upper right corner of the player intersects with the lower left of the enemy.", test.isCollision(FO1, player));
        Assert.assertFalse("Tests when there is no intersection", test.isCollision(FO2, player));
        player.tick();
        Assert.assertTrue("Tests when the lower right corner of player intersects wiht the enemy", test.isCollision(FO3, player));


    }

}