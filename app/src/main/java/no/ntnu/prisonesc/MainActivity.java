package no.ntnu.prisonesc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;

import no.ntnu.prisonesc.powerups.Cannon;
import no.ntnu.prisonesc.powerups.Powerup;

public class MainActivity extends AppCompatActivity {

    private ImageView playerImage, cannonImage, boomImage;
    private int pixelSize;

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pixelSize = getResources().getDimensionPixelSize(R.dimen.world_scale_factor);

        playerImage = (ImageView) findViewById(R.id.playerImage);
        cannonImage = (ImageView) findViewById(R.id.cannonImage);
        boomImage = (ImageView) findViewById(R.id.boomImage);
        playerImage.setTranslationX(-20 * pixelSize);

        Player player = new Player(0, 0, 0, 0, 0, 0, new Point(0, 0));
        for (Powerup powerup : SaveData.getData(getApplicationContext()).getBoughtPowerups()) {
            if (powerup instanceof Cannon) {
                playerImage.setVisibility(View.GONE);
                cannonImage.setVisibility(View.VISIBLE);
                cannonImage.setTranslationX(-200 * pixelSize);
                cannonImage.animate()
                        .translationX(0)
                        .setDuration(1000)
                        .setInterpolator(new DecelerateInterpolator());
            }
            powerup.apply(player);
        }
        playerImage.setImageResource(player.imageSelector.getImageResource());
    }

    public void startGame(View view) {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this, GameActivity.class));
                finish();
            }
        };
        if (cannonImage.getVisibility() == View.VISIBLE) {
            boomImage.setVisibility(View.VISIBLE);
            boomImage.animate()
                    .setDuration(100)
                    .scaleX(1.5f).scaleY(1.5f)
                    .setInterpolator(new OvershootInterpolator())
                    .withEndAction(runnable);
            playerImage.setVisibility(View.VISIBLE);
            playerImage.setRotation(70);
            playerImage.setTranslationY(100 * pixelSize);
            playerImage.animate()
                    .translationX(1000).translationY(-200)
                    .setDuration(100)
                    .setInterpolator(new AccelerateInterpolator());
        } else {
            playerImage.animate()
                    .translationX(200 * pixelSize)
                    .rotation(20)
                    .setInterpolator(new AccelerateInterpolator())
                    .withEndAction(runnable);
        }
    }

    public void openShop(View view) {
        startActivity(new Intent(this, ShopActivity.class));
        finish();
    }
}
