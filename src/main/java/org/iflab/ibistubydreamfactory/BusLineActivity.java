package org.iflab.ibistubydreamfactory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import org.iflab.ibistubydreamfactory.adapters.BusLineAdapter;
import org.iflab.ibistubydreamfactory.models.BusStation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BusLineActivity extends AppCompatActivity {

    private ListView listViewBusLine;
    private TextView textViewBusName;
    private List<BusStation> busStationList;
    private Intent intent;
    private String busLine;//班车路线json数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_line);
        init();
        loadData();
        listViewBusLine.setAdapter(new BusLineAdapter(busStationList, BusLineActivity.this));
    }

    private void init() {
        listViewBusLine = (ListView) findViewById(R.id.listView_busLine);
        textViewBusName = (TextView) findViewById(R.id.textView_busName);
        intent = getIntent();
        busLine = intent.getStringExtra("line");
        getSupportActionBar().setTitle(intent.getStringExtra("type"));//获取传过来的类型并设为activity的标题
        textViewBusName.setText(intent.getStringExtra("name"));
        busStationList = new ArrayList<>();
    }

    private void loadData() {
        try {
            JSONArray jsonArray = new JSONArray(busLine);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                BusStation busStation = new BusStation();
                busStation.setStation(jsonObject.getString("station"));
                busStation.setArrivalTime(jsonObject.getString("arrivalTime"));
                busStationList.add(busStation);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bus, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }
}
