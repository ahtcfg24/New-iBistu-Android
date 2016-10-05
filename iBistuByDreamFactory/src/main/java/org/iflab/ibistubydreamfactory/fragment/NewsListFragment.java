package org.iflab.ibistubydreamfactory.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.activities.NewsDetailActivity;
import org.iflab.ibistubydreamfactory.adapters.NewsListAdapter;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.NewsAPI;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.News;
import org.iflab.ibistubydreamfactory.utils.ACache;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 新闻列表
 */
public class NewsListFragment extends Fragment {
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private ListView newsListView;
    private SwipeRefreshLayout pullToRefreshView;//下拉刷新控件
    private View loadMoreView;//上拉加载更多控件
    private LinearLayout footerProgressLayout;
    private View rootView;//Fragment的界面
    private String fragmentName;//每个fragmentTab的名字
    private String newsListURL;//缺少当前页编号参数的新闻列表URL
    private int currentPage;//分页加载的当前页编号
    private NewsListAdapter newsListAdapter;
    private List<News> newsList;//
    private ProgressBar progressBar;
    private TextView loadToLastTextView;
    private Intent intent;
    private String category;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        init();
        initView();
        initRefresh();
//        getNews();
        loadData();
        return rootView;
    }

    /**
     * 初始化新闻列表
     */
    private void init() {
        Bundle bundle = getArguments();
        fragmentName = bundle.getString("fragmentName");
        category = bundle.getString("newsCategoryPath");
        newsListURL = MyApplication.newsListBaseURL + "?category=" + bundle.getString("newsCategoryPath") + "&page=";
        currentPage = 0;
        newsList = new ArrayList<>();
        newsListAdapter = new NewsListAdapter(NewsListFragment.this.getActivity());
    }

    /**
     * 初始化布局控件
     */
    private void initView() {
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        newsListView = (ListView) rootView.findViewById(R.id.newsListView);
        loadMoreView = getActivity().getLayoutInflater().inflate(R.layout.item_load_more, null);
        loadToLastTextView = (TextView) loadMoreView.findViewById(R.id.load_to_last_textView);
        footerProgressLayout = (LinearLayout) loadMoreView.findViewById(R.id.footer_progress_layout);
        newsListView.addFooterView(loadMoreView);
        pullToRefreshView = (SwipeRefreshLayout) rootView.findViewById(R.id.pullToRefreshView);
        newsListView.setOnScrollListener(new ScrollListener());//上拉加载
        /*监听listView*/
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                intent = new Intent();
                intent.putExtra("newsLink", newsList.get(position).getNewsLink());
                intent.putExtra("fragmentName", fragmentName);
                intent.setClass(getActivity(), NewsDetailActivity.class);
                startActivity(intent);
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
                        newsList.clear();
                        currentPage = 0;
                        getNews();
                        Snackbar.make(rootView, "刷新完成", Snackbar.LENGTH_SHORT).show();
                    }
                }, 1000);
            }


        });
    }

    private void loadData() {
        List<News> tempList = (List<News>) aCache.getAsObject(newsListURL);
        if (tempList == null) {//如果缓存没有
            getNews();
        } else {
            newsList.addAll(tempList);
            progressBar.setVisibility(View.GONE);
            newsListAdapter.addItem(newsList);
            if (currentPage == 0) {
                newsListView.setAdapter(newsListAdapter);
            } else {
                newsListAdapter.notifyDataSetChanged();//更新列表视图
            }
            currentPage++;
        }
    }


    /**
     * 获得新闻列表信息
     */
    private void getNews() {
        NewsAPI newsAPI = APISource.getInstance().getAPIObject(NewsAPI.class);
        Call<List<News>> call = newsAPI.getNewsList(category, currentPage + "");
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                pullToRefreshView.setRefreshing(false);//加载完成收起下拉刷新的进度圈
                if (response.isSuccessful()) {
                    List<News> tempList = response.body();
                    if (tempList.size() == 0) {
                        footerProgressLayout.setVisibility(View.INVISIBLE);
                        loadToLastTextView.setVisibility(View.VISIBLE);
                    } else {
                        if (currentPage == 0) {
                            aCache.put(newsListURL, (Serializable) tempList);//缓存第一页的数据
                        }
                        loadData();
                    }
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                pullToRefreshView.setRefreshing(false);//加载完成收起下拉刷新的进度圈
                Log.e("error：", t.toString());
                footerProgressLayout.setVisibility(View.INVISIBLE);
                loadToLastTextView.setVisibility(View.VISIBLE);
                Snackbar.make(rootView, "错误：" + t.getMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

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
                getNews();
                isLastRow = false;
            }
        }
    }
}