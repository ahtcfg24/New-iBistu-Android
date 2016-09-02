package org.iflab.ibistubydreamfactory.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shizhefei.view.indicator.IndicatorViewPager;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.fragment.NewsListFragment;

import java.util.Map;

import static org.iflab.ibistubydreamfactory.MyApplication.newsCategory;


/**
 * 新闻ViewPager适配器
 */
public class NewsListFragmentAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
    private LayoutInflater inflater;
    private Map<String, String> newsPathMap;


    public NewsListFragmentAdapter(FragmentManager fragmentManager, Context context, Map<String, String> newsPathMap) {
        super(fragmentManager);
        inflater = LayoutInflater.from(context);
        this.newsPathMap = newsPathMap;
    }

    @Override
    public int getCount() {
        return newsPathMap.size();
    }

    /**
     * 填充tab
     */
    @Override
    public View getViewForTab(int position, View convertView, ViewGroup container) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.tab_news_category, container, false);
        }
        TextView textView = (TextView) convertView;
        textView.setText(newsCategory[position % newsCategory.length]);
        return convertView;
    }

    /**
     * 填充viewpager中的fragment
     */
    @Override
    public Fragment getFragmentForPage(int position) {
        String fragmentName = newsCategory[position % newsCategory.length];
        NewsListFragment newsListFragment = new NewsListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("fragmentName", fragmentName);
        bundle.putString("newsCategoryPath", newsPathMap.get(fragmentName));
        newsListFragment.setArguments(bundle);
        return newsListFragment;
    }

}
