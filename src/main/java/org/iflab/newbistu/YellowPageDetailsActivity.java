package org.iflab.newbistu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.iflab.newbistu.adapters.yellowPageDetailsAdapter;
import org.iflab.newbistu.apis.APISource;
import org.iflab.newbistu.apis.YellowPageService;
import org.iflab.newbistu.customviews.YellowPageDialog;
import org.iflab.newbistu.models.ErrorMessage;
import org.iflab.newbistu.models.Resource;
import org.iflab.newbistu.models.YellowPageDepart;
import org.iflab.newbistu.utils.ACache;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YellowPageDetailsActivity extends AppCompatActivity {
    private ListView listViewYellowPageDepartDetails;
    private ProgressBar progressBar;
    private List<YellowPageDepart> yellowPageDepartDetailsList;
    private Resource<YellowPageDepart> yellowPageDepartDetailsResource;
    private ACache aCache;
    private Intent intent;
    private String branchName;//部门下分支的名字
    private String telephoneNumber;//部门下分支的号码
    private String depart;
    private String filterDepart = "depart=";//后端接口查询depart的过滤字段


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellow_page_depart_details);
        init();//初始化
        if (yellowPageDepartDetailsResource == null) {
            /*如果缓存没有就从网络获取*/
            getYellowPageDepartResource();
        } else {
            loadData();
        }
        listViewYellowPageDepartDetails.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                branchName = yellowPageDepartDetailsList.get(position).getName();
                telephoneNumber = yellowPageDepartDetailsList.get(position).getTelnum().toString();
                new YellowPageDialog(YellowPageDetailsActivity.this, branchName, telephoneNumber, yellowPageDepartDetailsList, position);

            }
        });

    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar_yellowPageDepartDetails);
        intent = getIntent();
        depart = intent.getStringExtra("depart");
        getSupportActionBar().setTitle(intent.getStringExtra("name"));
        listViewYellowPageDepartDetails = (ListView) findViewById(R.id.listView_yellowPageDepartDetails);
        aCache = ACache.get(MyApplication.getAppContext());
        yellowPageDepartDetailsResource = (Resource<YellowPageDepart>) aCache.getAsObject(depart);

    }


    /**
     * 获得学校部门信息
     */
    private void getYellowPageDepartResource() {
        YellowPageService departmentService = APISource.getInstance()
                                                       .getService(YellowPageService.class);
        Call<Resource<YellowPageDepart>> call = departmentService.getYellowPageDepartDetails(filterDepart + depart);
        call.enqueue(new Callback<Resource<YellowPageDepart>>() {
            @Override
            public void onResponse(Call<Resource<YellowPageDepart>> call, Response<Resource<YellowPageDepart>> response) {
                if (response.isSuccessful()) {
                    yellowPageDepartDetailsResource = response.body();
                    aCache.put(depart, yellowPageDepartDetailsResource);
                    loadData();
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<Resource<YellowPageDepart>> call, Throwable t) {
                System.out.println("error：" + t.toString());
                Toast.makeText(YellowPageDetailsActivity.this, "错误：" + t.getMessage(), Toast.LENGTH_LONG)
                     .show();

            }
        });

    }

    /**
     * 加载部门下的详情数据到listview中
     */
    private void loadData() {
        yellowPageDepartDetailsList = yellowPageDepartDetailsResource.getResource();
        progressBar.setVisibility(View.GONE);
        listViewYellowPageDepartDetails.setAdapter(new yellowPageDetailsAdapter(YellowPageDetailsActivity.this, yellowPageDepartDetailsList));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_yellow_page_depart_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }


}
