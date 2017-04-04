package no.ntnu.prisonesc;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.util.ArraySet;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Collection;
import java.util.Set;

public class GameActivity extends AppCompatActivity implements Runnable, SensorEventListener {
    private static final String TAG = "GameActivity";

    Point position = new Point(0, 0);

    ScrollerView scrollerView;
    Handler handler = new Handler();
    Player player;
    ImageView playerImage;


    float readMeter;
    SensorManager sensorManager;
    Sensor shake;

    /**
     * Note: this points to the same object as {@link ScrollerView#hittableObjects}.
     * Things are also added to and removed from the set there.
     * We do this for performance reasons.
     */
    Set<FlyingObject> flyingObjects = new ArraySet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher, null);
        SaveData shopData = SaveData.getData(getApplicationContext());
        playerImage = (ImageView) findViewById(R.id.playerImage);
        //Basevalues:
        double drag = 0.2;
        int posY = 5;
        int velX = 2;
        int velY = 2;
        int accY = -10;//Må være negativ fordi gravitasjonen går nedover.
        //end BaseValues
        //Lager player med basevalusene
        player = new Player(drag, posY, velX, velY, accY);
        //legger til poweruppsene
        Collection<Powerup> powerups = shopData.getBoughtPowerups();
        for (Powerup e : powerups) {
            if (e.isInitialCondition()) {
                e.apply(player);
            }
        }


        ViewGroup layoutRoot = (ViewGroup) findViewById(R.id.layout_root);
        scrollerView = new ScrollerView(this, flyingObjects);
        layoutRoot.addView(scrollerView, 0);

        layoutRoot.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        //For å håndtere akslerometeret:
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shake = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.d(TAG, "onCreate: " + shake);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        handler.post(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, shake, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(this);

        sensorManager.unregisterListener(this);
    }

    /**
     * This is the tick
     */
    @Override
    public void run() {
        player.setRot(calculateRotation(readMeter));

        player.tick();
        for (FlyingObject e : flyingObjects) {
            if (isCollision(e, player))
                e.onCollision(player);
        }

        playerImage.setRotation(player.getRot() / 10 + 90);//+90 for å få det i det formatet som trengs, /10 for å få mer presise verdier
        float height = scrollerView.getHeight();
        float pos = player.getVelY() / 100 + 1;
        if (pos < 0) {
            pos = 0;
        } else if (pos > 2) {
            pos = 2;
        }
        pos = pos / 2;
        //playerImage.setTranslationY(pos * height);

        position = player.getPos();
        if (position.y < 0) position = new Point(position.x, 0);
        scrollerView.tick(position);
        handler.postDelayed(this, 16);
    }

    /**
     * Tar inn indata fra akslerometeret og konverterer det til grader
     * @param readValue
     * @return
     */
    public int calculateRotation(float readValue) {
        int res = (int) (readValue * 90);//For å få det til grader gange 10 for å få en mer presis verdi
        if (res < -900)//For å begrense utslaget til maks
            res = -900;
        else if (res > 900)
            res = 900;
        return res + 900;//For at vi skal få et positivt tall mellom 0 og 180
    }

    /**
     * for å spare litt prosessering sjekker jeg bare de fremste hjørnene til player.
     * @return true hvis det er en kollisjon
     */
    public boolean isCollision(FlyingObject enemy, Player player) {
        Point cp = player.getPos().move(player.getSize());//Hjørnet oppe til høyre til player
        Point cf = new Point(enemy.x + enemy.w, enemy.y + enemy.h);//Hjørnet oppe til høyre til enemy
        if ((cp.x > enemy.x && cp.y > enemy.y) && (cf.x > player.getPos().x && cf.y > player.getPos().y)) {//Må testen
            return true;
        } else
            return player.getPos().x + player.size.x > enemy.x && player.getPos().y < enemy.y + enemy.h;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        readMeter = event.values[1];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
