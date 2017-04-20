package no.ntnu.prisonesc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.prisonesc.powerups.AerodynamicClothes;
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

        hideAndroidUiElements();

        shopWraper = (LinearLayout) findViewById(R.id.shop_wraper);
        data = SaveData.getData(getApplicationContext());
        moneyText = (TextView) findViewById(R.id.shop_money_number_text);

        updateShopData();
    }

    public void updateShopData(){
        powerups = new ArrayList<>(data.getPowerups());
        money = data.getMoney();
        String moneyString = "" + money;
        moneyText.setText(moneyString);
        drawShop();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateShopData();
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.back_button_shop:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.shop_text:
                //TODO remove on release, this is a debug function
                data.addMoney(1000);
                money = data.getMoney();
                String moneyString = "" + money;
                moneyText.setText(moneyString);
                break;
        }
    }

    private void drawShop(){
        hideAndroidUiElements();

        shopWraper.removeAllViews();
        List<Powerup> powerupList = powerups;
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
                if(j < currentPowerup.getLevel() + 1){

                    final ImageView boughtPowerup = new ImageView(this);
                    int id = ((i+1) * 100) + j;
                    boughtPowerup.setId(id);

                    //Set custom imageresoruces here
                    if(currentPowerup instanceof AerodynamicClothes){
                        customBoughtClothes(j, boughtPowerup);
                    } else {
                        boughtPowerup.setImageResource(R.drawable.checkbox_filled);
                    }

                    boughtPowerup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(ShopActivity.this, "You already own this!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    buyLayout.addView(boughtPowerup);
                } else {
                    final ImageView unBoughtPowerup = new ImageView(this);
                    int id = ((i+1) * 100) + j;
                    unBoughtPowerup.setId(id);

                    //Set custom imageresources here
                    if (currentPowerup instanceof AerodynamicClothes){
                        customUnBoughtClothes(j, unBoughtPowerup);
                    } else {
                        unBoughtPowerup.setImageResource(R.drawable.checkbox_empty);
                    }

                    unBoughtPowerup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //Toast.makeText(ShopActivity.this, "Image is pressed! Id: " + unBoughtPowerup.getId(), Toast.LENGTH_SHORT).show();
                            buyPowerup(unBoughtPowerup.getId());
                        }
                    });

                    buyLayout.addView(unBoughtPowerup);
                }

            }
            shopWraper.addView(buyLayout);
        }

    }

    private void buyPowerup(int id){
        final int pup = (id/100) -1;
        Log.w("Size of poweruplist", "" + powerups.size());
        final Powerup powerupToBuy = this.powerups.get(pup);
        final int level = id % 100;
        final int currentLevel = powerupToBuy.getLevel();
        final int levelDiff = level - currentLevel;
        final int price = powerupToBuy.getPrice(levelDiff);

        Log.w("buy", "powerup: " + pup + ", level: " + level + ", Current Level: " + currentLevel);
        Log.w("Price", "" + price);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Are you sure?").setMessage("This cost is " + price);

        builder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try{
                    data.doPurchase(price);
                } catch (SaveData.OutOfFundsException e){
                    Toast.makeText(ShopActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    hideAndroidUiElements();
                    return;
                }
                
                
                for (int i = 0; i < levelDiff; i++) {
                    powerupToBuy.buy();
                }
                powerups.set(pup, powerupToBuy);
                data.updatePowerups(powerups);
                updateShopData();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                hideAndroidUiElements();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
        hideAndroidUiElements();
    }

    private void hideAndroidUiElements(){
        ViewGroup layoutRoot = (ViewGroup) findViewById(R.id.shop_activity_root);
        layoutRoot.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void customUnBoughtClothes(int level, ImageView image){
        switch (level){
            case 0:
                image.setImageResource(R.drawable.prisoner_1);
                break;
            case 1:
                image.setImageResource(R.drawable.prisoner_2);
                break;
            case 2:
                image.setImageResource(R.drawable.prisoner_3);
                break;
            default:
                image.setImageResource(R.drawable.prisoner_1);
        }
        int pixel = Util.pixelSize(getApplicationContext());
        image.setPadding(4*pixel, 0, 8*pixel, 0);
    }

    private void customBoughtClothes(int level, ImageView image){
        switch (level){
            case 0:
                image.setImageResource(R.drawable.prisoner_1_selected);
                break;
            case 1:
                image.setImageResource(R.drawable.prisoner_2_selected);
                break;
            case 2:
                image.setImageResource(R.drawable.prisoner_3_selected);
                break;
            default:
                image.setImageResource(R.drawable.prisoner_1_selected);
        }
        int pixel = Util.pixelSize(getApplicationContext());
        image.setPadding(4*pixel, 0, 8*pixel, 0);
    }
}
