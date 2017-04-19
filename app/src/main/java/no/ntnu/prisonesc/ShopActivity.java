package no.ntnu.prisonesc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.ntnu.prisonesc.powerups.Powerup;

public class ShopActivity extends AppCompatActivity {

    private SaveData data;
    private List<Powerup> powerups;

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
        powerups = new ArrayList<>(data.getPowerups());
        drawShop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        powerups = new ArrayList<>(data.getBoughtPowerups());
        money = data.getMoney();
    }
/*
    void OnClick(View view){
        switch ((String) view.getTag()){

        }
    }
*/

    private void test(){
        for (int i = 0; i < powerups.size(); i++) {
            powerups.get(i).setLevel(3);
        }
        powerups.get(1).setLevel(5);
        data.updatePowerups(this.powerups);
    }


    private void drawShop(){
        LinearLayout shopWraperLayout = (LinearLayout) findViewById(R.id.shop_wraper);
        List<Powerup> powerupList = new ArrayList<>(powerups);
        for (int i = 0; i < powerups.size(); i++) {
            Powerup currentPowerup = powerupList.get(i);

            String powerupString = currentPowerup.getName();
            TextView tv = new TextView(this);
            tv.setText(powerupString);
            tv.setTextSize(32);
            tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            shopWraperLayout.addView(tv);

            LinearLayout buyLayout = new LinearLayout(this);
            buyLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            buyLayout.setOrientation(LinearLayout.HORIZONTAL);

            for (int j = 0; j < currentPowerup.getMaxLevel(); j++) {
                if(j < currentPowerup.getLevel()){

                    ImageView boughtPowerup = new ImageView(this);
                    //TODO add resource
                    boughtPowerup.setImageResource(R.drawable.balloon_dude);
                    buyLayout.addView(boughtPowerup);
                } else {

                    ImageView unBoughtPowerup = new ImageView(this);
                    //TODO add resource
                    unBoughtPowerup.setImageResource(R.drawable.balloon_1);
                    buyLayout.addView(unBoughtPowerup);
                }
            }

            shopWraperLayout.addView(buyLayout);
        }

    }
}
