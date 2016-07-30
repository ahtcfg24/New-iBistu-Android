package org.iflab.ibistubydreamfactory;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.iflab.ibistubydreamfactory.adapters.AboutListViewAdapter;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.AboutAPI;
import org.iflab.ibistubydreamfactory.models.About;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.Resource;
import org.iflab.ibistubydreamfactory.utils.ACache;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutActivity extends AppCompatActivity {
    private Resource<About> introductionResource;
    private ProgressBar progressBar;
    private List<About> aboutItemList;//存储关于列表各选项的名字
    private ListView aboutListView;
    private Intent intent;
    private ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        init();
        if (introductionResource == null) {
            /*如果缓存没有就从网络获取*/
            getIntroductionResource();
        } else {
            loadData();
        }
        aboutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent.putExtra("aboutContent", aboutItemList.get(position).getAboutDetails());//把模块的内容传过去
                intent.putExtra("aboutName", aboutItemList.get(position)
                                                          .getAboutName());//把所点击的模块的名字传过去
                intent.setClass(AboutActivity.this, AboutDetailsActivity.class);
                startActivity(intent);
            }
        });

    }

    /**
     * 初始化
     */
    private void init() {
        intent = new Intent();
        progressBar = (ProgressBar) findViewById(R.id.progressBar_about);
        aboutListView = (ListView) findViewById(R.id.about_listView);
        aCache = ACache.get(MyApplication.getAppContext());
        introductionResource = (Resource<About>) aCache.getAsObject("introductionResource");
    }


    /**
     * 获取数据
     */
    private void getIntroductionResource() {
        AboutAPI aboutAPI = APISource.getInstance().getAPIObject(AboutAPI.class);
        Call<Resource<About>> call = aboutAPI.getAboutIntroductions();
        call.enqueue(new Callback<Resource<About>>() {
            @Override
            public void onResponse(Call<Resource<About>> call, Response<Resource<About>> response) {
                if (response.isSuccessful()) {
                    introductionResource = response.body();
                    aCache.put("introductionResource", introductionResource);
                    loadData();
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<Resource<About>> call, Throwable t) {
                System.out.println("error：" + t.toString());

            }
        });
    }

    /**
     * 填充数据
     */
    private void loadData() {
        aboutItemList = introductionResource.getResource();
        progressBar.setVisibility(View.GONE);
        aboutListView.setAdapter(new AboutListViewAdapter(aboutItemList, this));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }


}
