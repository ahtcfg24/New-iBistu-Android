package org.iflab.ibistubydreamfactory.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Toast;

import com.lhh.ptrrv.library.PullToRefreshRecyclerView;

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
public class PlayerActivity extends AppCompatActivity {
    @BindView(R.id.recyclerView)
    PullToRefreshRecyclerView recyclerView;
    private List<VRMedia> vrMediaList;
    private Resource<VRMedia> vrMediaResource;
    private VRMediaAdapter vrMediaAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vrMediaAdapter = new VRMediaAdapter(this);
        vrMediaAdapter.setMyOnItemClickListener(new VRMediaAdapter.MyOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String type, String url) {
                switch (type) {
                    case "VIDEO":
                        VRVideoPlayerActivity.startVideo(PlayerActivity.this, Uri.parse(url));
                        break;
                    case "IMAGE":
                        VRVideoPlayerActivity.startBitmap(PlayerActivity.this, Uri.parse(url));
                        break;
                    default:
                        break;
                }
            }
        });

        getVRMediaResource();

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
                System.out.println("error：" + t.toString());
                Toast.makeText(PlayerActivity.this, "错误：" + t.getMessage(), Toast.LENGTH_LONG)
                     .show();
            }
        });
    }

    /**
     * 填充数据
     */
    private void loadData() {
        vrMediaList = vrMediaResource.getResource();
        vrMediaAdapter.addItem(vrMediaList);
        recyclerView.setAdapter(vrMediaAdapter);
    }

}
