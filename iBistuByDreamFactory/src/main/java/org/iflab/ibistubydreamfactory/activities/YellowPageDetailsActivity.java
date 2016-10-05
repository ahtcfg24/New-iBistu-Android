package org.iflab.ibistubydreamfactory.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class YellowPageDetailsActivity extends AppCompatActivity {
    @BindView(R.id.progressBar_yellowPageDepartDetails)
    ProgressBar progressBar;
    @BindView(R.id.listView_yellowPageDepartDetails)
    ListView listViewYellowPageDepartDetails;
    private List<YellowPageDepartment> yellowPageDepartmentDetailsList;
    private Resource<YellowPageDepartment> yellowPageDepartDetailsResource;
    private ACache aCache;
    private String branchName;//部门下分支的名字
    private String telephoneNumber;//部门下分支的号码
    private String department;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yellow_page_depart_details);
        ButterKnife.bind(this);
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

    /**
     * 初始化
     */
    private void init() {
        Intent intent = getIntent();
        department = intent.getStringExtra("department");
        setTitle(intent.getStringExtra("name"));
        aCache = ACache.get(MyApplication.getAppContext());
        yellowPageDepartDetailsResource = (Resource<YellowPageDepartment>) aCache.getAsObject(department);

    }


    /**
     * 获得学校部门信息
     */
    private void getYellowPageDepartResource() {
        YellowPageAPI departmentService = APISource.getInstance().getAPIObject(YellowPageAPI.class);
        String filter = "department=";
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


}
