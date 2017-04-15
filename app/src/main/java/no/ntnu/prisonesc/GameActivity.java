package no.ntnu.prisonesc;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GameActivity extends AppCompatActivity implements Runnable, SensorEventListener {
    private static final String TAG = "GameActivity";

    ScrollerView scrollerView;
    Handler handler = new Handler();
    Player player;
    ImageView playerImage;
    TextView scoreText;


    float readMeter;
    SensorManager sensorManager;
    Sensor shake;

    /**
     * Note: this points to the same object as {@link ScrollerView#hittableObjects}.
     * Things are also added to and removed from the set there.
     * We do this for performance reasons.
     */
    List<FlyingObject> flyingObjects = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher, null);
        SaveData shopData = SaveData.getData(getApplicationContext());
        playerImage = (ImageView) findViewById(R.id.playerImage);
        scoreText = (TextView) findViewById(R.id.scoreText);

        //Basevalues:
        double drag = 0.0005;
        double gliderFactor = 0.2;
        int posY = 0;
        int velX = 400;
        int velY = 400;
        int accY = -1;//Må være negativ fordi gravitasjonen går nedover.
        //end BaseValues
        //Lager player med basevalusene
        player = new Player(drag, gliderFactor, posY, velX, velY, accY);
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
        for (int i = flyingObjects.size() - 1; i >= 0; i--) {
            // Vi må loope baklengs for a java ikke skal bli sur når vi sletter ting.
            // TODO: Plutselig går alt på trynet fordi alle physicsobjektene blir overbevist
            // om at de treffer spilleren. Samtidig får vi ENORM velocity (2139859919,60029), som igjen gir ugyldig posisjon pga overflow.
            FlyingObject flying = flyingObjects.get(i);
            if (isCollision(flying, player)) {
                flying.onCollision(player);
                scrollerView.removeFlyingObject(flying);
            }
        }
        //Log.d(TAG, "run.getVelY: " + player.getVelY());


        //START plaser bildet av player på skjerm
        playerImage.setRotation(player.getRot().getDeg() / 10 + 90);//+90 for å få det i det formatet som trengs, /10 for å få mer presise verdier. getDeg fordi det er en OldRotation.
        //Det etterf;lgende er for [ plasere height;yden. Det er en funksjon som skal ende om med en faktor som ganges med height;yden p[ skjermen.
        /*Dette er komentert ut for å kunne implementeres i del 5.
        double height = scrollerView.getHeight();
        Log.d(TAG, "run.getRot(): " + player.getRot());
        double playerPos = player.getVelY() / 10.00 + 1;//divisoren m[ tilpasses
        Log.d(TAG, "run.playerPos: " + playerPos);
        if (playerPos < 0) {
            playerPos = 0;
        } else if (playerPos > 2) {
            playerPos = 2;
        }
        playerPos = playerPos / 2.00000;
        double scaled = playerPos * 0.6 + 0.2;// for å få det inn på skjermen.
        Log.d(TAG, "run.scaled: " + scaled);
        playerImage.setTranslationY((float) (scaled * height));
        Log.d(TAG, "run.res: " + (scaled * height));*/
        //END plasere bildet av player på skjermen

        scrollerView.tick(player.getPos());
        handler.postDelayed(this, 16);

        scoreText.setText(String.valueOf(player.getPos().x));

    }

    /**
     * Tar inn indata fra akslerometeret og konverterer det til grader
     *
     * @param readValue
     * @return
     */
    public OldRotation calculateRotation(float readValue) {
        int res = (int) (readValue * 90);//For å få det til grader gange 10 for å få en mer presis verdi
        if (res < -900)//For å begrense utslaget til maks
            res = -900;
        else if (res > 900)
            res = 900;
        Log.d(TAG, "calculateRotation.res: "+res);
        return new OldRotation(res + 900);//For at vi skal få et positivt tall mellom 0 og 180
    }

    /**
     * for å spare litt prosessering sjekker jeg bare de fremste hjørnene til player.
     * Håper at det ikke gjør så mye at han treffer med beinene og at han går mest fremover.
     *
     * @return true hvis det er en kollisjon
     */
    public boolean isCollision(FlyingObject enemy, Player player) {
        Point cp = player.getPos().move(player.getSize());//Hjørnet oppe til høyre til player
        Point cf = new Point(enemy.position.x + enemy.width, enemy.position.y + enemy.height);//Hjørnet oppe til høyre til enemy
        if ((cp.x > enemy.position.x && cp.y > enemy.position.y) && (cf.x > player.getPos().x && cf.y > player.getPos().y)) {//Må testen
            return true;
        } else
            return player.getPos().x + player.size.x > enemy.position.x && player.getPos().y < enemy.position.y + enemy.height;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        readMeter = event.values[1];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
