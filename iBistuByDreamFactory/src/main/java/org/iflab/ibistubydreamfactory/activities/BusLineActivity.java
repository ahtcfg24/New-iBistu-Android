package org.iflab.ibistubydreamfactory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.TextView;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.adapters.BusLineAdapter;
import org.iflab.ibistubydreamfactory.models.BusStation;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BusLineActivity extends AppCompatActivity {

    @BindView(R.id.textView_busName)
    TextView textViewBusName;
    @BindView(R.id.listView_busLine)
    ListView listViewBusLine;
    private List<BusStation> busStationList;
    private String busLine;//班车路线json数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_line);
        ButterKnife.bind(this);
        init();
        loadData();
        listViewBusLine.setAdapter(new BusLineAdapter(busStationList, BusLineActivity.this));
    }

    /**
     * 初始化
     */
    private void init() {
        Intent intent = getIntent();
        busLine = intent.getStringExtra("line");
        setTitle(intent.getStringExtra("type"));//获取传过来的类型并设为activity的标题
        textViewBusName.setText(intent.getStringExtra("name"));
        busStationList = new ArrayList<>();
    }

    /**
     * 填充数据
     */
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


}
