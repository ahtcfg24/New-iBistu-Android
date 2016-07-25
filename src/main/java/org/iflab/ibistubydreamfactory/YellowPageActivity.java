package org.iflab.ibistubydreamfactory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.iflab.ibistubydreamfactory.adapters.YellowPageAdapter;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.YellowPageService;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.Resource;
import org.iflab.ibistubydreamfactory.models.YellowPageDepart;
import org.iflab.ibistubydreamfactory.utils.ACache;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YellowPageActivity extends AppCompatActivity {
    private ListView listViewYellowPageDepart;
    private ProgressBar progressBar;
    private List<YellowPageDepart> yellowPageDepartList;
    private Resource<YellowPageDepart> yellowPageDepartResource;
    private ACache aCache;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellow_page_depart);
        init();//初始化
        if (yellowPageDepartResource == null) {
            /*如果缓存没有就从网络获取*/
            getYellowPageDepartResource();
        } else {
            loadData();
        }
        listViewYellowPageDepart.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent();
                intent.putExtra("depart", yellowPageDepartList.get(position).getDepart());
                intent.putExtra("name", yellowPageDepartList.get(position).getName());
                intent.setClass(YellowPageActivity.this, YellowPageDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar_yellowPageDepart);
        listViewYellowPageDepart = (ListView) findViewById(R.id.listView_yellowPage);
        aCache = ACache.get(MyApplication.getAppContext());
        yellowPageDepartResource = (Resource<YellowPageDepart>) aCache.getAsObject("yellowPageDepartResource");

    }


    /**
     * 填充部门列表数据到ListView
     */
    private void loadData() {
        yellowPageDepartList = yellowPageDepartResource.getResource();
        progressBar.setVisibility(View.GONE);
        listViewYellowPageDepart.setAdapter(new YellowPageAdapter(yellowPageDepartList, YellowPageActivity.this));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_yellow_page_depart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }



    /**
     * 获得学校部门信息
     */
    private void getYellowPageDepartResource() {
        YellowPageService departmentService = APISource.getInstance()
                                                       .getService(YellowPageService.class);
        Call<Resource<YellowPageDepart>> call = departmentService.getYellowPageDepart();
        call.enqueue(new Callback<Resource<YellowPageDepart>>() {
            @Override
            public void onResponse(Call<Resource<YellowPageDepart>> call, Response<Resource<YellowPageDepart>> response) {
                if (response.isSuccessful()) {
                    yellowPageDepartResource = response.body();
                    aCache.put("yellowPageDepartResource", yellowPageDepartResource);
                    loadData();
                }else {
                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<Resource<YellowPageDepart>> call, Throwable t) {
                System.out.println("error：" + t.toString());
                Toast.makeText(YellowPageActivity.this, "错误：" + t.getMessage(), Toast.LENGTH_LONG)
                     .show();

            }
        });

    }
}
