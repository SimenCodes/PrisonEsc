package no.ntnu.prisonesc;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.ntnu.prisonesc.powerups.Powerup;

public class GameActivity extends AppCompatActivity implements Runnable, SensorEventListener, View.OnTouchListener {
    private static final String TAG = "GameActivity";
    private static final int MONEYRATE = 10;
    private static final int END_GAME_DELAY = 2000;
    private static final int LONG_TOUTCH = 500;
    ImageView playerImageView;
    ImageView splatImageView;
    ScrollerView scrollerView;
    Handler handler = new Handler();
    Player player;
    TextView scoreText;
    SaveData shopData;


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


        shopData = SaveData.getData(getApplicationContext());
        playerImageView = (ImageView) findViewById(R.id.playerImage);
        splatImageView = (ImageView) findViewById(R.id.splatImageView);
        scoreText = (TextView) findViewById(R.id.scoreText);

        //Basevalues:
        double drag = 0.0028;
        double gliderFactor = 0;
        int posY = 300;
        int velX = 5;
        int velY = 2;
        float accY = -0.3f;//Må være negativ fordi gravitasjonen går nedover.
        Point size = new Point(100, 30);
        //end BaseValues
        //Lager player med basevalusene
        player = new Player(drag, gliderFactor, posY, velX, velY, accY, size);
        //legger til poweruppsene
        Collection<Powerup> powerups = shopData.getBoughtPowerups();
        for (Powerup e : powerups) {
            if (e.isInitialCondition()) {
                e.apply(player, this.playerImageView);
            }
        }

        ViewGroup layoutRoot = (ViewGroup) findViewById(R.id.layout_root);
        scrollerView = new ScrollerView(this, flyingObjects);
        layoutRoot.addView(scrollerView, 0);

        layoutRoot.setOnTouchListener(this);
        playerImageView.setOnTouchListener(this);

        layoutRoot.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        // Android-magi for å få beskjed med en gang vi vet hvor stor PlayerImageView er.
        ViewTreeObserver viewTreeObserver = playerImageView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Log.d(TAG, "onGlobalLayout() called");
                    playerImageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    updatePlayerSize();
                    scrollerView.delayedInit();
                }
            });
        }


        //For å håndtere akslerometeret:
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        shake = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        handler.post(this);
    }

    protected void updatePlayerSize() {
        player.setSize(new Point(
                playerImageView.getWidth() / Util.pixelSize(playerImageView.getContext()),
                playerImageView.getHeight() / Util.pixelSize(playerImageView.getContext())
        ));
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
        Log.d(TAG, "run.player.getRot: " + player.getRot().getDeg());
        player.tick();
        /*Kolisjonskode som ikke funker helt.
        //Log.d(TAG, "run.flyingObjects.size: "+flyingObjects.size());
        for (int i = flyingObjects.size() - 1; i >= 0; i--) {
            // Vi må loope baklengs for a java ikke skal bli sur når vi sletter ting.
            FlyingObject flying = flyingObjects.get(i);
            if (isCollision(flying, player)) {
                flying.onCollision(player);
                scrollerView.removeFlyingObject(flying);
                Log.d(TAG, "run: Vi har en kollisjon");
            } else
                Log.d(TAG, "run: player@" + player.getCenter() + " => dist=" + flying.getCenter().dist(player.getCenter()));
        }
        //Log.d(TAG, "run.getVelY: " + player.getVelY());
        */
        //Log.d(TAG, "run.flyingObjects.size: "+ flyingObjects.size());
        if (flyingObjects.size() > 0) {

            float flyingScreenRad = 0;
            final float playerX = playerImageView.getX();
            final float playerY = playerImageView.getY();
            final Point playerScreenCornerPos = new Point(playerImageView.getX(), playerImageView.getY());
            final int playerWidth = playerImageView.getWidth();
            final int playerHeight = playerImageView.getHeight();
            final Point playerScreenSize = new Point(playerImageView.getWidth(), playerImageView.getHeight());
            final float playerScreenRad = Math.max(playerScreenSize.x / 2, playerScreenSize.y / 2);
            final Point playerScreenPos = playerScreenCornerPos.move(-playerScreenSize.x / 2, -playerScreenSize.y / 2);
            for (int i = flyingObjects.size() - 1; i >= 0; i--) {
                // Vi må loope baklengs for a java ikke skal bli sur når vi sletter ting.
                FlyingObject flying = flyingObjects.get(i);
                final Point flyingScreenCornerPos = new Point(flying.image.getTranslationX(), flying.image.getTranslationY());
                final Point flyingScreenSize = new Point(flying.image.getWidth(), flying.image.getHeight());
                flyingScreenRad = Math.max(flyingScreenSize.x / 2, flyingScreenSize.y / 2);
                final Point flyingScreenPos = flyingScreenCornerPos.move(-flyingScreenSize.x / 2, -flyingScreenSize.y / 2);

                if (playerScreenCornerPos.dist(flyingScreenPos) < playerScreenRad + flyingScreenRad) {//Vi har en kollisjon
                    scrollerView.removeFlyingObject(flying);
                    flying.onCollision(player);
                    Log.d(TAG, "run: Vi har kræsjet");
                } else
                    Log.d(TAG, "run: ikke kolisjon " + playerScreenCornerPos.dist(flyingScreenPos) + " og " + playerScreenRad + flyingScreenRad);
            }
        }

        //START plaser bildet av player på skjerm
        playerImageView.setRotation(180 - (player.getRot().getDeg() / 10));//180 minus for å få det i det formatet som trengs, /10 for å få mer presise verdier. getDeg fordi det er en OldRotation.
        //Det etterf;lgende er for [ plasere height;yden. Det er en funksjon som skal ende om med en faktor som ganges med height;yden p[ skjermen.
        /*Dette er komentert ut for å kunne implementeres i del 5.
        double height = scrollerView.getHeight();
        Log.d(TAG, "run.getRot(): " + player.getRot());
        double screenScaledPlayerPos = player.getVelY() / 10.00 + 1;//divisoren m[ tilpasses
        Log.d(TAG, "run.screenScaledPlayerPos: " + screenScaledPlayerPos);
        if (screenScaledPlayerPos < 0) {
            screenScaledPlayerPos = 0;
        } else if (screenScaledPlayerPos > 2) {
            screenScaledPlayerPos = 2;
        }
        screenScaledPlayerPos = screenScaledPlayerPos / 2.00000;
        double scaled = screenScaledPlayerPos * 0.6 + 0.2;// for å få det inn på skjermen.
        Log.d(TAG, "run.scaled: " + scaled);
        playerImageView.setTranslationY((float) (scaled * height));
        Log.d(TAG, "run.res: " + (scaled * height));*/
        //END plasere bildet av player på skjermen

        scrollerView.tick(player.getPos());

        scoreText.setText(String.valueOf(player.getPos().x));

        if (player.getPos().y != 0 || player.getVelY() != 0) {
            handler.postDelayed(this, 16);
        } else {
            Log.w(TAG, "run: END OF GAME!");
            splatImageView.setVisibility(View.VISIBLE);
            splatImageView.setScaleX(0);
            splatImageView.setScaleY(0);
            splatImageView.animate()
                    .scaleX(1).scaleY(1)
                    .setInterpolator(new OvershootInterpolator());
            splatImageView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showEndGameDialog();
                }
            }, END_GAME_DELAY);
        }
    }

    private void showEndGameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        int distance = (int) player.getPos().x;
        final int money = calculateMoney(distance);
        String message = "Distance: " + distance + "\n" +
                            "Money earned: " + money;

        builder.setTitle("Score").setMessage(message);

        builder.setCancelable(false);
        builder.setPositiveButton("Back to cell", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                shopData.addMoney(money);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    /**
     * Tar inn indata fra akslerometeret og konverterer det til grader
     * Vi bruker formatet der 0 er rett ned og gradene øker mot klokka. Dette er egentlig ikke så bra, men det var slik vi begynte.
     */
    public OldRotation calculateRotation(float readValue) {
        int res = (int) (readValue * 90);//For å få det til grader gange 10 for å få en mer presis verdi
        if (res < -900)//For å begrense utslaget til maks
            res = -900;
        else if (res > 900)
            res = 900;
        //Log.d(TAG, "calculateRotation.res: "+res);
        //Minus for å få det på riktig retning.
        return new OldRotation(1800 - (res + 900));//For at vi skal få et positivt tall mellom 0 og 180
    }

    /**
     * Endret den opprinnelige koden til at den nå bruker sirkler i steden for firkanter.
     *
     * @return true hvis det er en kollisjon
     */
    public boolean isCollision(Circular c1, Circular c2) {
        return c1.getCenter().dist(c2.getCenter()) < c1.getRadius() + c2.getRadius();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        readMeter = event.values[1];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private int calculateMoney(int distance) {
        return distance / MONEYRATE + player.getMoneyBalloonCount() * MoneyBalloon.VALUE;
    }

    Runnable launchRocket = new Runnable() {
        @Override
        public void run() {
            //Apply rocket powerup
            Toast.makeText(GameActivity.this, "Launc the rocket!", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {

        Log.w("onToutch", "" + event.getAction());

        if (MotionEvent.ACTION_DOWN == event.getAction()){
            handler.postDelayed(launchRocket, LONG_TOUTCH);
        } else if (MotionEvent.ACTION_CANCEL == event.getAction()){
            handler.removeCallbacks(launchRocket);
        } else if(MotionEvent.ACTION_UP == event.getAction()){
            handler.removeCallbacks(launchRocket);
        }
        return true;
    }
}
