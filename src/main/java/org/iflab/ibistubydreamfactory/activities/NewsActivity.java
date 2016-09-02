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

import static org.iflab.ibistubydreamfactory.MyApplication.newsCategory;


public class NewsActivity extends AppCompatActivity {
    private ScrollIndicatorView categoryIndicatorView;
    private ViewPager viewPager;
    private IndicatorViewPager indicatorViewPager;
    private Map<String, String> newsPathMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        initView();
    }

    private void initView() {
        newsPathMap = new HashMap<>();
        newsPathMap.put(newsCategory[0], "zhxw");
        newsPathMap.put(newsCategory[1], "tpxw");
        newsPathMap.put(newsCategory[2], "rcpy");
        newsPathMap.put(newsCategory[3], "jxky");
        newsPathMap.put(newsCategory[4], "whhd");
        newsPathMap.put(newsCategory[5], "xyrw");
        newsPathMap.put(newsCategory[6], "jlhz");
        newsPathMap.put(newsCategory[7], "shfw");
        newsPathMap.put(newsCategory[8], "mtgz");

        categoryIndicatorView = (ScrollIndicatorView) findViewById(R.id.category_news);

        /**
         * 设置tab下面的滑动条
         */
        categoryIndicatorView.setScrollBar(new DrawableBar(this,R.drawable.bg_news_tab, ScrollBar.Gravity.BOTTOM) {
            @Override
            public int getHeight(int tabHeight) {
                return tabHeight/12;
            }

            @Override
            public int getWidth(int tabWidth) {
                return tabWidth;
            }
        });

        viewPager = (ViewPager) findViewById(R.id.viewPager_newsList);
        indicatorViewPager = new IndicatorViewPager(categoryIndicatorView, viewPager);
        indicatorViewPager.setAdapter(new NewsListFragmentAdapter(getSupportFragmentManager(), NewsActivity.this, newsPathMap));
    }


}
