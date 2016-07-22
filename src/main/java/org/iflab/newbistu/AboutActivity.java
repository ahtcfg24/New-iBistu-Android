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

import org.iflab.newbistu.adapters.AboutListViewAdapter;
import org.iflab.newbistu.apis.APISource;
import org.iflab.newbistu.apis.AboutService;
import org.iflab.newbistu.models.ErrorMessage;
import org.iflab.newbistu.models.Introduction;
import org.iflab.newbistu.models.Resource;
import org.iflab.newbistu.utils.ACache;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AboutActivity extends AppCompatActivity {
    private Resource<Introduction> introductionResource;
    private ProgressBar progressBar;
    private List<Introduction> aboutItemList;//存储关于列表各选项的名字
    private ListView aboutListView;
    private Intent intent;
    private ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                intent.putExtra("introCont", aboutItemList.get(position).getIntroCont());//把模块的内容传过去
                intent.putExtra("introName", aboutItemList.get(position)
                                                          .getIntroName());//把所点击的模块的名字传过去
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
    }


    /**
     * 获取数据
     */
    private void getIntroductionResource() {
        AboutService aboutService = APISource.getInstance().getService(AboutService.class);
        Call<Resource<Introduction>> call = aboutService.getAboutIntroDuctions();
        call.enqueue(new Callback<Resource<Introduction>>() {
            @Override
            public void onResponse(Call<Resource<Introduction>> call, Response<Resource<Introduction>> response) {
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
            public void onFailure(Call<Resource<Introduction>> call, Throwable t) {
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
