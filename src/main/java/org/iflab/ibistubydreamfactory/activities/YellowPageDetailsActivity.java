package org.iflab.ibistubydreamfactory.activities;

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

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.adapters.yellowPageDetailsAdapter;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.YellowPageAPI;
import org.iflab.ibistubydreamfactory.customviews.YellowPageDialog;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.Resource;
import org.iflab.ibistubydreamfactory.models.YellowPageDepartment;
import org.iflab.ibistubydreamfactory.utils.ACache;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YellowPageDetailsActivity extends AppCompatActivity {
    private ListView listViewYellowPageDepartDetails;
    private ProgressBar progressBar;
    private List<YellowPageDepartment> yellowPageDepartmentDetailsList;
    private Resource<YellowPageDepartment> yellowPageDepartDetailsResource;
    private ACache aCache;
    private Intent intent;
    private String branchName;//部门下分支的名字
    private String telephoneNumber;//部门下分支的号码
    private String department;
    private String filter = "department=";//后端接口查询department的过滤字段，相当于sql中的where子句


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
                branchName = yellowPageDepartmentDetailsList.get(position).getName();
                telephoneNumber = yellowPageDepartmentDetailsList.get(position).getTelephone();
                new YellowPageDialog(YellowPageDetailsActivity.this, branchName, telephoneNumber, yellowPageDepartmentDetailsList, position);

            }
        });

    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar_yellowPageDepartDetails);
        intent = getIntent();
        department = intent.getStringExtra("department");
        getSupportActionBar().setTitle(intent.getStringExtra("name"));
        listViewYellowPageDepartDetails = (ListView) findViewById(R.id.listView_yellowPageDepartDetails);
        aCache = ACache.get(MyApplication.getAppContext());
        yellowPageDepartDetailsResource = (Resource<YellowPageDepartment>) aCache.getAsObject(department);

    }


    /**
     * 获得学校部门信息
     */
    private void getYellowPageDepartResource() {
        YellowPageAPI departmentService = APISource.getInstance()
                                                   .getAPIObject(YellowPageAPI.class);
        Call<Resource<YellowPageDepartment>> call = departmentService.getYellowPageDetails(filter + department);
        call.enqueue(new Callback<Resource<YellowPageDepartment>>() {
            @Override
            public void onResponse(Call<Resource<YellowPageDepartment>> call, Response<Resource<YellowPageDepartment>> response) {
                if (response.isSuccessful()) {
                    yellowPageDepartDetailsResource = response.body();
                    aCache.put(department, yellowPageDepartDetailsResource);
                    loadData();
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<Resource<YellowPageDepartment>> call, Throwable t) {
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
        yellowPageDepartmentDetailsList = yellowPageDepartDetailsResource.getResource();
        progressBar.setVisibility(View.GONE);
        listViewYellowPageDepartDetails.setAdapter(new yellowPageDetailsAdapter(YellowPageDetailsActivity.this, yellowPageDepartmentDetailsList));

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
