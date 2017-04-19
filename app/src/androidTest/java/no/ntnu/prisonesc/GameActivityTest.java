package no.ntnu.prisonesc;

import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.util.Log;
import android.widget.ImageView;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Henrik on 02.04.2017.
 */
public class GameActivityTest {
    @Test
    public void isCollision() throws Exception {
        FlyingObject FO1 = new FlyingObject(new Point(5, 5), 20, 10, new ImageView(InstrumentationRegistry.getTargetContext())) {
            @Override
            void onCollision(Player player) {
                Log.d(TAG, "onCollision: test FO1");
            }
        };
        FlyingObject FO2 = new FlyingObject(new Point(100, 105), 30, 30, new ImageView(InstrumentationRegistry.getTargetContext())) {
            @Override
            void onCollision(Player player) {
                Log.d(TAG, "onCollision: test FO2");
            }
        };
        FlyingObject FO3 = new FlyingObject(new Point(10, 5), 10, 10, new ImageView(InstrumentationRegistry.getTargetContext())) {
            @Override
            void onCollision(Player player) {
                Log.d(TAG, "onCollision: test FO3");
            }
        };
        FlyingObject FO4 = new FlyingObject(new Point(5, 15), 10, 10, new ImageView(InstrumentationRegistry.getTargetContext())) {
            @Override
            void onCollision(Player player) {
                Log.d(TAG, "onCollision: test FO3");
            }
        };
        FlyingObject FO5 = new FlyingObject(new Point(8, 11), 10, 10, new ImageView(InstrumentationRegistry.getTargetContext())) {
            @Override
            void onCollision(Player player) {
                Log.d(TAG, "onCollision: test FO3");
            }
        };
        Looper.prepare();

        Player player = new Player(0.2, 0.0, 5, 100, 100, -1);
        GameActivity test = new GameActivity();
        Assert.assertTrue("Tests when there is intersection", test.isCollision(FO1, player));
        Assert.assertFalse("Tests when there is no intersection", test.isCollision(FO2, player));
        Assert.assertFalse("TestBorderX", test.isCollision(FO3, player));
        Assert.assertFalse("TestBorderY. dist: " + FO4.getCenter().dist(player.getCenter()), test.isCollision(FO4, player));
        Assert.assertFalse("TestBorderOther", test.isCollision(FO5, player));
    }

}