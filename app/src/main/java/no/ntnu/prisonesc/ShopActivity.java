package no.ntnu.prisonesc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Collection;

public class ShopActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SaveData data;
    private Collection<Powerup> availeble, owned;

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
        owned = data.getBoughtPowerups();

        mRecyclerView = (RecyclerView) findViewById(R.id.shop_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        String[] myDataset = {"Hello", "World!", "It", "is", "ALIVE!", "Wo", "Fuking","HO!!!!"};
        mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        owned = data.getBoughtPowerups();
        money = data.getMoney();
        TextView moneyAmount = (TextView) findViewById(R.id.shop_money_text);
        String moneyString = "Money: " + money;
        moneyAmount.setText(moneyString);
    }
}
