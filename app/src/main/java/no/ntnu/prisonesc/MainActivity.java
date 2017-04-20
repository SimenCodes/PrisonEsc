package no.ntnu.prisonesc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import no.ntnu.prisonesc.powerups.Clothes;
import no.ntnu.prisonesc.powerups.Powerup;

public class MainActivity extends AppCompatActivity {

    private ImageView playerImage;
    private int pixelSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pixelSize = getResources().getDimensionPixelSize(R.dimen.world_scale_factor);

        playerImage = (ImageView) findViewById(R.id.playerImage);
        playerImage.setTranslationX(-20 * pixelSize);

        playerImage.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        for (Powerup powerup : SaveData.getData(getApplicationContext()).getBoughtPowerups()) {
            if (powerup instanceof Clothes)
                powerup.apply(new Player(0, 0, 0, 0, 0, 0, new Point(0, 0)), playerImage);
        }
    }

    public void startGame(View view) {
        playerImage.animate()
                .translationX(200 * pixelSize)
                .rotation(20)
                .setInterpolator(new AccelerateInterpolator())
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(MainActivity.this, GameActivity.class));
                        finish();
                    }
                });
    }

    public void openShop(View view) {
        startActivity(new Intent(this, ShopActivity.class));
        finish();
    }
}
