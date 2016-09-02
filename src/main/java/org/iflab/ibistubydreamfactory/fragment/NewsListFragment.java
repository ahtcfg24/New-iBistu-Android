package org.iflab.ibistubydreamfactory.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.loopj.android.http.JsonHttpResponseHandler;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.activities.NewsDetailActivity;
import org.iflab.ibistubydreamfactory.adapters.NewsListAdapter;
import org.iflab.ibistubydreamfactory.models.News;
import org.iflab.ibistubydreamfactory.utils.ACache;
import org.iflab.ibistubydreamfactory.utils.HttpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 新闻列表
 */
public class NewsListFragment extends Fragment {
    private ACache aCache = ACache.get(MyApplication.getAppContext());
    private ListView newsListView;
    private JSONArray newsListJsonArray;
    //    private PullToRefreshView pullToRefreshView;//下拉刷新控件
    private View loadMoreView;//上拉加载更多控件
    private View rootView;//Fragment的界面
    private String newsCategoryPath;//对应Fragment的相对路径
    private String fragmentName;//每个fragmentTab的名字
    private String newsListURL;//缺少当前页编号参数的新闻列表URL
    private int currentPage;//分页加载的当前页编号
    private NewsListAdapter newsListAdapter;
    private List<News> newsList;//
    private ProgressBar progressBar;
    private TextView loadToLastTextView;
    private LinearLayout footerProgressLayout;
    private Intent intent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        init();
        initView();
        loadData();
        return rootView;
    }

    /**
     * 初始化新闻列表
     */
    private void init() {
        Bundle bundle = getArguments();
        newsCategoryPath = bundle.getString("newsCategoryPath");
        fragmentName = bundle.getString("fragmentName");
        newsListURL = MyApplication.newsListBaseURL + "?category=" + newsCategoryPath + "&page=";
        currentPage = 1;
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
//        pullToRefreshView = (PullToRefreshView) rootView.findViewById(R.id.pull_to_refresh);
//        pullToRefreshView.setOnRefreshListener(new RefreshListener());
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
     * 从网络或者缓存载入数据
     */
    private void loadData() {

        newsListJsonArray = aCache.getAsJSONArray(newsListURL + currentPage);
        if (newsListJsonArray == null) {
            getNewsListDataByURL(newsListURL + currentPage);
        } else {
            handleNewsListData(newsListJsonArray);
        }
    }

    /**
     * 通过URL从网络获取数据
     *
     * @param URL 按页分的数据URL
     */
    private void getNewsListDataByURL(final String URL) {
        HttpUtil.get(URL, new JsonHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.e(URL, throwable.getMessage());
                footerProgressLayout.setVisibility(View.INVISIBLE);
                loadToLastTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.i(URL, response.toString());
                if (response.toString().equals("[]")) {
                    footerProgressLayout.setVisibility(View.INVISIBLE);
                    loadToLastTextView.setVisibility(View.VISIBLE);
                } else {
                    aCache.put(URL, response);
                    handleNewsListData(response);
                }
            }
        });
    }

    /**
     * 处理分页后的新闻列表数据
     *
     * @param jsonArray 分页存放的数据
     */
    private void handleNewsListData(JSONArray jsonArray) {
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                News news = new News();
                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                news.setNewsTitle(jsonObject2.getString("newsTitle"));
                news.setNewsIntro(jsonObject2.getString("newsIntro"));
                news.setNewsTime(jsonObject2.getString("newsTime"));
                news.setNewsImage(jsonObject2.getString("newsImage"));
                news.setNewsLink(jsonObject2.getString("newsLink"));
                newsList.add(news);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressBar.setVisibility(View.GONE);
        newsListAdapter.addItem(newsList);
        if (currentPage == 1) {
            newsListView.setAdapter(newsListAdapter);
        } else {
            newsListAdapter.notifyDataSetChanged();//更新列表视图
        }
        currentPage++;
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
                loadData();
                isLastRow = false;
            }
        }
    }
//
//    /**
//     * 下拉刷新监听器
//     */
//    private class RefreshListener implements PullToRefreshView.OnRefreshListener {
//        @Override
//        public void onRefresh() {
//            pullToRefreshView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    pullToRefreshView.setRefreshing(false);
//                    currentPage = 1;
//                    newsList.clear();//清空旧的新闻列表
//                    getNewsListDataByURL(newsListURL + currentPage);
//                    new MyToast("刷新完成");
//                }
//            }, 1000);
//
//        }
//    }
}
