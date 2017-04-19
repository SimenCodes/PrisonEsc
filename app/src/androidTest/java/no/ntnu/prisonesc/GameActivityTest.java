package no.ntnu.prisonesc;

import android.os.Looper;
import android.support.test.InstrumentationRegistry;
import android.util.Log;
import android.widget.ImageView;

import org.junit.Test;

/**
 * Created by Henrik on 02.04.2017.
 */
public class GameActivityTest {
    @Test
    public void isCollision() throws Exception {
        FlyingObject FO1 = new FlyingObject(new Point(5, 5), 10, 10, new ImageView(InstrumentationRegistry.getTargetContext())) {
            @Override
            void onCollision(Player player) {
                Log.d(TAG, "onCollision: test");
            }
        };
        FlyingObject FO2 = new FlyingObject(new Point(11, 11), 10, 10, new ImageView(InstrumentationRegistry.getTargetContext())) {
            @Override
            void onCollision(Player player) {
                Log.d(TAG, "onCollision: test");
            }
        };
        FlyingObject FO3 = new FlyingObject(new Point(5, 6), 10, 10, new ImageView(InstrumentationRegistry.getTargetContext())) {
            @Override
            void onCollision(Player player) {
                Log.d(TAG, "onCollision: test");
            }
        };
        Looper.prepare();

        Player player = new Player(0.2, 0.0, 15, 0, 0, -1);
        GameActivity test = new GameActivity();
        //Assert.assertTrue("Tests when the upper right corner of the player intersects with the lower left of the enemy.", test.isCollision(FO1, player));
        //Assert.assertFalse("Tests when there is no intersection", test.isCollision(FO2, player));
        player.tick();
        //Assert.assertTrue("Tests when the lower right corner of player intersects wiht the enemy", test.isCollision(FO3, player));


    }

}