package org.iflab.ibistubydreamfactory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.adapters.BusAdapter;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.BusAPI;
import org.iflab.ibistubydreamfactory.models.Bus;
import org.iflab.ibistubydreamfactory.models.BusType;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.Resource;
import org.iflab.ibistubydreamfactory.utils.ACache;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BusActivity extends AppCompatActivity {

    @BindView(R.id.progressBar_scheduledbus)
    ProgressBar progressBar;
    @BindView(R.id.listView_scheduledBus)
    ListView listViewBus;
    private List<Bus> busSourceList;
    private BusType scheduledBus;
    private Resource<Bus> busResource;
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus);
        ButterKnife.bind(this);
        init();//初始化
        if (busResource == null) {
          /*如果缓存没有就从网络获取*/
            getBusResource();
        } else {
            loadData();
        }
        listViewBus.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent();
                if (position > scheduledBus.getItemCount()) {
                    position = position - 1;
                }
                position = position - 1;
                intent.putExtra("line", busSourceList.get(position).getBusLine());
                intent.putExtra("name", busSourceList.get(position).getBusName());
                intent.putExtra("type", busSourceList.get(position).getBusType());
                intent.setClass(BusActivity.this, BusLineActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 初始化
     */
    private void init() {
        busResource = (Resource<Bus>) aCache.getAsObject("busResource");
    }

    /**
     * 获得班车列表信息
     */
    private void getBusResource() {
        BusAPI busAPI = APISource.getInstance().getAPIObject(BusAPI.class);
        //获得通勤班车数据
        Call<Resource<Bus>> call = busAPI.getBus();
        call.enqueue(new Callback<Resource<Bus>>() {
            @Override
            public void onResponse(Call<Resource<Bus>> call, Response<Resource<Bus>> response) {
                if (response.isSuccessful()) {
                    busResource = response.body();
                    aCache.put("busResource", busResource);
                    loadData();
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<Resource<Bus>> call, Throwable t) {
                System.out.println("error：" + t.toString());
                Toast.makeText(BusActivity.this, "错误：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    /**
     * 填充部门列表数据到ListView
     */
    private void loadData() {
        busSourceList = busResource.getResource();
        scheduledBus = new BusType("通勤班车");
        BusType teachBus = new BusType("教学班车");
        for (Bus bus : busSourceList) {
            String busType = bus.getBusType();
            if (busType.equals("通勤班车")) {
                scheduledBus.addItem(bus);
            } else if (busType.equals("教学班车")) {
                teachBus.addItem(bus);
            }
        }
        List<BusType> busData = new ArrayList<>();
        busData.add(scheduledBus);
        busData.add(teachBus);

        progressBar.setVisibility(View.GONE);
        listViewBus.setAdapter(new BusAdapter(busData, BusActivity.this));

    }


}

