package org.iflab.ibistubydreamfactory.activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.adapters.VRMediaAdapter;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.VRMediaAPI;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.Resource;
import org.iflab.ibistubydreamfactory.models.VRMedia;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 主界面.
 */
public class VRActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private List<VRMedia> vrMediaList;
    private Resource<VRMedia> vrMediaResource;
    private VRMediaAdapter vrMediaAdapter;
    private boolean isFirstGetData = true;//是否第一次获取数据
    private View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = getLayoutInflater().inflate(R.layout.activity_player, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        initView();
        initRefresh();
        getVRMediaResource();
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vrMediaAdapter = new VRMediaAdapter(this);
        vrMediaAdapter.setMyOnItemClickListener(new VRMediaAdapter.MyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String type, String url) {
                switch (type) {
                    case "VIDEO":
                        VRPlayerActivity.startVideo(VRActivity.this, Uri.parse(url));
                        break;
                    case "IMAGE":
                        VRPlayerActivity.startBitmap(VRActivity.this, Uri.parse(url));
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 初始化下拉刷新功能
     */
    private void initRefresh() {
        refreshLayout.setColorSchemeColors(Color.RED, Color.BLUE);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        vrMediaList.clear();
                        getVRMediaResource();
                        Snackbar.make(rootView, "刷新完成", Snackbar.LENGTH_SHORT).show();
                    }
                }, 500);
            }


        });
    }


    private void getVRMediaResource() {
        VRMediaAPI vrMediaAPI = APISource.getInstance().getAPIObject(VRMediaAPI.class);
        Call<Resource<VRMedia>> call = vrMediaAPI.getVRMedia();
        call.enqueue(new Callback<Resource<VRMedia>>() {
            @Override
            public void onResponse(Call<Resource<VRMedia>> call, Response<Resource<VRMedia>> response) {
                if (response.isSuccessful()) {
                    vrMediaResource = response.body();
                    loadData();
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<Resource<VRMedia>> call, Throwable t) {
                refreshLayout.setRefreshing(false);
                System.out.println("error：" + t.toString());
                Toast.makeText(VRActivity.this, "错误：" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 填充数据
     */
    private void loadData() {
        refreshLayout.setRefreshing(false);
        vrMediaList = vrMediaResource.getResource();
        vrMediaAdapter.addItem(vrMediaList);
        if (isFirstGetData) {
            recyclerView.setAdapter(vrMediaAdapter);
            isFirstGetData = false;
        } else {
            vrMediaAdapter.notifyDataSetChanged();
        }
    }

}
