package org.iflab.ibistubydreamfactory.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.adapters.LostFoundListAdapter;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.LostFoundAPI;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.LostFound;
import org.iflab.ibistubydreamfactory.models.Resource;
import org.iflab.ibistubydreamfactory.models.SetIsFoundRequestBody;
import org.iflab.ibistubydreamfactory.models.User;
import org.iflab.ibistubydreamfactory.utils.ACache;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.materialdialog.MaterialDialog;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LostFoundActivity extends AppCompatActivity {
    @BindView(R.id.listView_lostFound)
    ListView listViewLostFound;
    @BindView(R.id.pullToRefreshView)
    SwipeRefreshLayout pullToRefreshView;
    @BindView(R.id.floatingButton)
    FloatingActionButton floatingButton;
    private List<LostFound> lostFoundList;
    private Resource<LostFound> lostFoundResource;
    private int currentPage;//分页加载的当前页编号
    private View rootView;
    private View loadMoreView;//上拉加载更多控件
    private LinearLayout footerProgressLayout;
    private TextView loadToLastTextView;
    private LostFoundListAdapter lostFoundListAdapter;
    private String onlyShowFound = "isFound=false";//用于判断当前显示的数据是所有已发布的还是用户发布的
    private User user;
    private LostFoundAPI lostFoundAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = getLayoutInflater().inflate(R.layout.activity_lost_found, null);
        setContentView(rootView);
        ButterKnife.bind(this);

        initView();

        String intentExtra = getIntent().getStringExtra("isFound");
        if (intentExtra != null) {
            setTitle("我发布的");
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            onlyShowFound = intentExtra;
            floatingButton.setVisibility(View.INVISIBLE);
        }

        initRefresh();
        getLostFoundResource(currentPage, onlyShowFound);

    }


    private void initView() {
        user = (User) ACache.get(MyApplication.getAppContext()).getAsObject("user");
        lostFoundListAdapter = new LostFoundListAdapter(this);
        currentPage = 1;
        loadMoreView = getLayoutInflater().inflate(R.layout.item_load_more, null);
        loadToLastTextView = (TextView) loadMoreView.findViewById(R.id.load_to_last_textView);
        footerProgressLayout = (LinearLayout) loadMoreView.findViewById(R.id.footer_progress_layout);
        listViewLostFound.addFooterView(loadMoreView);
        listViewLostFound.setOnScrollListener(new ScrollListener());
        listViewLostFound.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (onlyShowFound.equals("isFound=false")) {
                    return false;
                } else {
                    showFinishDialog(lostFoundList.get(i).getId());
                }
                return true;
            }
        });
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LostFoundActivity.this, PostLostFoundActivity.class));
            }
        });
    }

    /**
     * 初始化下拉刷新功能
     */
    private void initRefresh() {
        pullToRefreshView.setColorSchemeColors(Color.RED, Color.BLUE);
        pullToRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lostFoundList.clear();
                        currentPage = 1;
                        getLostFoundResource(currentPage, onlyShowFound);
                        Snackbar.make(rootView, "刷新完成", Snackbar.LENGTH_SHORT).show();
                    }
                }, 500);
            }


        });
    }


    /**
     * 获取招领信息
     *
     * @param currentPage 当前页
     * @param isFound 是否结束
     */
    private void getLostFoundResource(final int currentPage, String isFound) {
        lostFoundAPI = APISource.getInstance().getAPIObject(LostFoundAPI.class);
        Call<Resource<LostFound>> call = lostFoundAPI.getLostFound((currentPage - 1) * 10 + "", isFound);
        call.enqueue(new Callback<Resource<LostFound>>() {
            @Override
            public void onResponse(Call<Resource<LostFound>> call, Response<Resource<LostFound>> response) {
                if (response.isSuccessful()) {
                    pullToRefreshView.setRefreshing(false);//加载完成收起下拉刷新的进度圈
                    lostFoundResource = response.body();
                    loadData();
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<Resource<LostFound>> call, Throwable t) {
                pullToRefreshView.setRefreshing(false);//加载完成收起下拉刷新的进度圈
                footerProgressLayout.setVisibility(View.INVISIBLE);
                loadToLastTextView.setVisibility(View.VISIBLE);
                System.out.println("error：" + t.toString());
                Toast.makeText(LostFoundActivity.this, "错误：" + t.getMessage(), Toast.LENGTH_LONG)
                     .show();
            }
        });
    }

    /**
     * 填充数据
     */
    private void loadData() {
        if (currentPage == 1) {
            lostFoundList = lostFoundResource.getResource();
            lostFoundListAdapter.addItem(lostFoundList);
            listViewLostFound.setAdapter(lostFoundListAdapter);
        } else {
            List<LostFound> tempList = lostFoundResource.getResource();
            if (tempList.size() != 0) {
                lostFoundList.addAll(tempList);
                lostFoundListAdapter.addItem(lostFoundList);
                lostFoundListAdapter.notifyDataSetChanged();//更新列表视图
            }
        }
        footerProgressLayout.setVisibility(View.INVISIBLE);
        loadToLastTextView.setVisibility(View.VISIBLE);
        currentPage++;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (onlyShowFound.equals("isFound=false")) {
            inflater.inflate(R.menu.menu_lost_found, menu);
        } else {
            inflater.inflate(R.menu.menu_my_lost_found, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_posted:
                if (onlyShowFound.equals("isFound=false")) {
                    Intent intent = new Intent();
                    intent.putExtra("isFound", "(isFound=false)And(author=" + user.getName() + ")");
                    intent.setClass(this, LostFoundActivity.class);
                    startActivity(intent);
                } else {
                    finish();
                }

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /**
     * 弹出更新提示框
     */
    private void showFinishDialog(final int lostFoundId) {
        final MaterialDialog materialDialog = new MaterialDialog(this);
        materialDialog.setPositiveButton("是", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
                SetIsFoundRequestBody setIsFoundRequestBody = new SetIsFoundRequestBody();
                setIsFoundRequestBody.setIsFound(1);
                Call<ResponseBody> call = lostFoundAPI.setIsFound(lostFoundId, setIsFoundRequestBody);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            lostFoundList.clear();
                            currentPage = 1;
                            getLostFoundResource(currentPage, onlyShowFound);
                        } else {
                            ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                            onFailure(call, e.toException());
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println("error：" + t.toString());
                        Toast.makeText(LostFoundActivity.this, "错误：" + t.getMessage(), Toast.LENGTH_LONG)
                             .show();
                    }
                });

            }
        }).setNegativeButton("否", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDialog.dismiss();
            }
        });
        materialDialog.setTitle("结束招领");
        materialDialog.setMessage("是否将该招领信息状态改为结束？");
        materialDialog.show();

    }

    /**
     * listView的滑动监听器
     */
    private class ScrollListener implements AbsListView.OnScrollListener {

        boolean isLastRow = false;//是否滚动到最后一行

        /**
         * 滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
         *
         * @param firstVisibleItem 当前能看见的第一个列表项ID（从0开始）
         * @param visibleItemCount 当前能看见的列表项个数（小半个也算）
         * @param totalItemCount 列表项共数
         */
        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            //判断是否滚到最后一行
            if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0) {
                isLastRow = true;
            }
        }

        /**
         * 正在滚动时回调，回调2-3次，手指没抛则回调2次。scrollState = 2的这次不回调
         * 回调顺序如下
         * 第1次：scrollState = SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
         * 第2次：scrollState = SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
         * 第3次：scrollState = SCROLL_STATE_IDLE(0) 停止滚动
         * 当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；
         * 由于用户的操作，屏幕产生惯性滑动时为2
         *
         * @param scrollState 滚动状态
         */
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            //当滚到最后一行且停止滚动时，执行加载
            if (isLastRow && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                loadToLastTextView.setVisibility(View.INVISIBLE);
                footerProgressLayout.setVisibility(View.VISIBLE);
                getLostFoundResource(currentPage, onlyShowFound);
                isLastRow = false;
            }
        }
    }
}
