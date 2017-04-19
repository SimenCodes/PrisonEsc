package no.ntnu.prisonesc;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import no.ntnu.prisonesc.powerups.Powerup;

public class ShopActivity extends AppCompatActivity {

    private SaveData data;
    private List<Powerup> powerups;

    private int money;
    private TextView moneyText;

    private LinearLayout shopWraper;

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

        shopWraper = (LinearLayout) findViewById(R.id.shop_wraper);

        data = SaveData.getData(getApplicationContext());
        powerups = new ArrayList<>(data.getPowerups());
        money = data.getMoney();

        moneyText = (TextView) findViewById(R.id.shop_money_number_text);
        String moneyString = "" + money;
        moneyText.setText(moneyString);
        test();
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
        List<Powerup> powerupList = new ArrayList<>(powerups);
        for (int i = 0; i < powerups.size(); i++) {
            Powerup currentPowerup = powerupList.get(i);

            String powerupString = currentPowerup.getName();
            TextView tv = new TextView(this);
            tv.setText(powerupString);
            tv.setTextSize(32);
            tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            shopWraper.addView(tv);

            LinearLayout buyLayout = new LinearLayout(this);
            buyLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            buyLayout.setOrientation(LinearLayout.HORIZONTAL);

            for (int j = 0; j < currentPowerup.getMaxLevel(); j++) {
                if(j < currentPowerup.getLevel()){

                    final ImageView boughtPowerup = new ImageView(this);
                    //TODO add resource
                    int id = ((i+1) * 100) + j;
                    boughtPowerup.setId(id);
                    boughtPowerup.setImageResource(R.drawable.balloon_dude);
                    boughtPowerup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ShopActivity.this, "You already own this! Id: " + boughtPowerup.getId(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    buyLayout.addView(boughtPowerup);
                } else {
                    final ImageView unBoughtPowerup = new ImageView(this);
                    //TODO add resource
                    unBoughtPowerup.setImageResource(R.drawable.balloon_1);
                    int id = ((i+1) * 100) + j;
                    unBoughtPowerup.setId(id);
                    unBoughtPowerup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ShopActivity.this, "Image is pressed! Id: " + unBoughtPowerup.getId(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    buyLayout.addView(unBoughtPowerup);
                }

            }
            shopWraper.addView(buyLayout);
        }

    }
}
