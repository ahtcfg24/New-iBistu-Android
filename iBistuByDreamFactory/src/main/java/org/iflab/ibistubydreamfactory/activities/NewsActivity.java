package org.iflab.ibistubydreamfactory.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.ScrollIndicatorView;
import com.shizhefei.view.indicator.slidebar.DrawableBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.adapters.NewsListFragmentAdapter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static org.iflab.ibistubydreamfactory.MyApplication.newsCategory;


public class NewsActivity extends AppCompatActivity {

    @BindView(R.id.category_news)
    ScrollIndicatorView categoryNews;
    @BindView(R.id.viewPager_newsList)
    ViewPager viewPagerNewsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        Map<String, String> newsPathMap = new HashMap<>();
        newsPathMap.put(newsCategory[0], "zhxw");
        newsPathMap.put(newsCategory[1], "tpxw");
        newsPathMap.put(newsCategory[2], "rcpy");
        newsPathMap.put(newsCategory[3], "jxky");
        newsPathMap.put(newsCategory[4], "whhd");
        newsPathMap.put(newsCategory[5], "xyrw");
        newsPathMap.put(newsCategory[6], "jlhz");
        newsPathMap.put(newsCategory[7], "shfw");
        newsPathMap.put(newsCategory[8], "mtgz");


        /**
         * 设置tab下面的滑动条
         */
        categoryNews.setScrollBar(new DrawableBar(this, R.drawable.bg_news_tab, ScrollBar.Gravity.BOTTOM) {
            @Override
            public int getHeight(int tabHeight) {
                return tabHeight / 12;
            }

            @Override
            public int getWidth(int tabWidth) {
                return tabWidth;
            }
        });

        IndicatorViewPager indicatorViewPager = new IndicatorViewPager(categoryNews, viewPagerNewsList);
        indicatorViewPager.setAdapter(new NewsListFragmentAdapter(getSupportFragmentManager(), NewsActivity.this, newsPathMap));
    }


}
