package no.ntnu.prisonesc;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.prisonesc.powerups.Cannon;
import no.ntnu.prisonesc.powerups.Clothes;
import no.ntnu.prisonesc.powerups.Powerup;
import no.ntnu.prisonesc.powerups.Wings;

public class ShopActivity extends AppCompatActivity {

    private ShopData data;
    private List<Powerup> powerups;

    private int money;
    private TextView moneyText;

    private LinearLayout shopWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        hideAndroidUiElements();

        shopWrapper = (LinearLayout) findViewById(R.id.shop_wrapper);
        data = ShopData.getData(getApplicationContext());
        moneyText = (TextView) findViewById(R.id.shop_money_number_text);

        updateShopData();
    }

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

    public void updateShopData() {
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_button_shop:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case R.id.shop_text:
                //TODO remove on release, this is a debug function
                data.addMoney(1000);
                money = data.getMoney();
                moneyText.setText(String.valueOf(money));
                break;
        }
    }

    private void drawShop() {
        hideAndroidUiElements();

        shopWrapper.removeAllViews();
        List<Powerup> powerupList = powerups;
        for (int i = 0; i < powerups.size(); i++) {
            final Powerup currentPowerup = powerupList.get(i);

            String powerupString = currentPowerup.getName();
            TextView tv = new TextView(this);
            tv.setText(powerupString);
            tv.setTextSize(32);
            tv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            shopWrapper.addView(tv);

            HorizontalScrollView horizontalScrollView = new HorizontalScrollView(this);
            horizontalScrollView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            shopWrapper.addView(horizontalScrollView);

            LinearLayout buyLayout = new LinearLayout(this);
            buyLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            buyLayout.setOrientation(LinearLayout.HORIZONTAL);

            for (int j = 0; j < currentPowerup.getMaxLevel(); j++) {
                if (j < currentPowerup.getLevel() + 1) {

                    final ImageView boughtPowerup = new ImageView(this);
                    int id = ((i + 1) * 100) + j;
                    boughtPowerup.setId(id);

                    //Set custom imageresoruces here
                    if (currentPowerup instanceof Clothes) {
                        customBoughtClothes(j, boughtPowerup);
                    } else if (currentPowerup instanceof Cannon) {
                        customBoughtCannon(j, boughtPowerup);
                    } else if (currentPowerup instanceof Wings) {
                        customBoughtWings(j, boughtPowerup);
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
                    int id = ((i + 1) * 100) + j;
                    unBoughtPowerup.setId(id);

                    //Set custom imageresources here
                    if (currentPowerup instanceof Clothes) {
                        customUnBoughtClothes(j, unBoughtPowerup);
                    } else if (currentPowerup instanceof Cannon) {
                        customUnBoughtCannon(j, unBoughtPowerup);
                    } else if (currentPowerup instanceof Wings) {
                        customUnBoughtWings(j, unBoughtPowerup);
                    } else {
                        unBoughtPowerup.setImageResource(R.drawable.checkbox_empty);
                    }

                    final int finalJ = j;
                    unBoughtPowerup.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final int currentLevel = currentPowerup.getLevel();
                            final int levelDiff = finalJ - currentLevel;
                            final int price = currentPowerup.getPrice(levelDiff);

                            if(money < price){

                                final Toast toast = Toast.makeText(getApplicationContext(), "Not enough money! Missing " + String.valueOf(price - money), Toast.LENGTH_SHORT);
                                toast.show();

                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        toast.cancel();
                                    }
                                }, 500);

                                //Toast.makeText(ShopActivity.this, "Not enough money! Missing " + String.valueOf(price - money), Toast.LENGTH_SHORT).show();
                            } else {
                                buyPowerup(unBoughtPowerup.getId());
                            }
                        }
                    });

                    buyLayout.addView(unBoughtPowerup);
                }

            }
            horizontalScrollView.addView(buyLayout);
        }

    }

    private void buyPowerup(int id) {
        final int pup = (id / 100) - 1;
        final Powerup powerupToBuy = this.powerups.get(pup);
        final int level = id % 100;
        final int currentLevel = powerupToBuy.getLevel();
        final int levelDiff = level - currentLevel;
        final int price = powerupToBuy.getPrice(levelDiff);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Are you sure?\nThis cost is " + price);

        builder.setPositiveButton("Buy!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    data.doPurchase(price);
                } catch (ShopData.OutOfFundsException e) {
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

    private void hideAndroidUiElements() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void customUnBoughtClothes(int level, ImageView image) {
        switch (level) {
            case 0:
                image.setImageResource(R.drawable.prisoner_1);
                break;
            case 1:
                image.setImageResource(R.drawable.prisoner_2);
                break;
            case 2:
                image.setImageResource(R.drawable.prisoner_3);
                break;
            case 3:
                image.setImageResource(R.drawable.prisoner_4);
                break;
            case 4:
                image.setImageResource(R.drawable.prisoner_5);
                break;
            default:
                image.setImageResource(R.drawable.prisoner_1);
        }
        adjustClothingSize(image);
    }

    private void customBoughtClothes(int level, ImageView image) {
        switch (level) {
            case 0:
                image.setImageResource(R.drawable.prisoner_1_selected);
                break;
            case 1:
                image.setImageResource(R.drawable.prisoner_2_selected);
                break;
            case 2:
                image.setImageResource(R.drawable.prisoner_3_selected);
                break;
            case 3:
                image.setImageResource(R.drawable.prisoner_4_selected);
                break;
            case 4:
                image.setImageResource(R.drawable.prisoner_5_selected);
                break;
            default:
                image.setImageResource(R.drawable.prisoner_1_selected);
        }
        adjustClothingSize(image);
    }

    private void adjustClothingSize(ImageView image) {
        int pixel = Util.pixelSize(getApplicationContext());
        image.setPadding(4 * pixel, 0, 8 * pixel, 0);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 150 * pixel);
        image.setLayoutParams(lp);
        image.setAdjustViewBounds(true);
    }


    private void customBoughtCannon(int level, ImageView image) {
        switch (level) {
            case 0:
                image.setImageResource(R.drawable.checkmark);
                adjustCannonImage(image);
                break;
            case 1:
                image.setImageResource(R.drawable.cannon_checkmark);
                adjustCannonImage(image);
                int pixel = Util.pixelSize(getApplicationContext());
                image.setPadding(24 * pixel, 0, 0, 0);
                break;
        }
    }

    private void customUnBoughtCannon(int level, ImageView image) {
        switch (level) {
            case 0:
                image.setImageResource(R.drawable.checkmark);
                adjustCannonImage(image);
                break;
            case 1:
                image.setImageResource(R.drawable.cannon);
                adjustCannonImage(image);
                break;
        }
    }

    private void adjustCannonImage(ImageView image) {
        int pixel = Util.pixelSize(getApplicationContext());
        image.setPadding(4 * pixel, 0, 8 * pixel, 0);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100 * pixel);
        image.setLayoutParams(lp);
    }

    private void customBoughtWings(int level, ImageView image) {
        switch (level) {
            case 0:
                image.setImageResource(R.drawable.checkmark);
                adjustWingImage(image);
                break;
            case 1:
                image.setImageResource(R.drawable.wings_checked);
                adjustWingImage(image);
                break;
        }
    }

    private void customUnBoughtWings(int level, ImageView image) {
        switch (level) {
            case 0:
                image.setImageResource(R.drawable.checkmark);
                adjustWingImage(image);
                break;
            case 1:
                image.setImageResource(R.drawable.wings);
                adjustWingImage(image);
                break;
        }
    }

    private void adjustWingImage(ImageView image) {
        int pixel = Util.pixelSize(getApplicationContext());
        image.setPadding(4 * pixel, 0, 8 * pixel, 0);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 100 * pixel);
        image.setLayoutParams(lp);
    }
}
