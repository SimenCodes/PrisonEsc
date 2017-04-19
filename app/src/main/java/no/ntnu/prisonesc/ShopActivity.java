package no.ntnu.prisonesc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Collection;

import no.ntnu.prisonesc.powerups.Powerup;

public class ShopActivity extends AppCompatActivity {

    private SaveData data;
    private Collection<Powerup> powerups;

    private int money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        ViewGroup layoutRoot = (ViewGroup) findViewById(R.id.shop_activity_root);
        layoutRoot.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        data = SaveData.getData(getApplicationContext());
        powerups = data.getPowerups();

        ScrollView shopRoot = (ScrollView) layoutRoot;

        LinearLayout wrap = new LinearLayout(this);
        wrap.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        wrap.setOrientation(LinearLayout.VERTICAL);
        shopRoot.addView(wrap);

        for (int i = 0; i < 20; i++) {

            String powerupString = "Powerup " + i;
            TextView tv = new TextView(this);
            tv.setText(powerupString);
            tv.setTextSize(32);

            tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            wrap.addView(tv);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        powerups = data.getBoughtPowerups();
        money = data.getMoney();
    }
}
