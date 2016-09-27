package org.iflab.ibistubydreamfactory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.models.News;

import java.util.List;

/**
 *
 */
public class NewsListAdapter extends BaseAdapter {
    private List<News> newsList;
    private Context context;


    public NewsListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return newsList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 绘制每个item
     *
     * @param position 点击的位置
     * @param convertView item对应的View
     * @param parent 可选的父控件
     * @return 要显示的单个item的View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_news, null);
            viewHolder = new ViewHolder();
            viewHolder.newsListTitle = (TextView) convertView.findViewById(R.id.newsList_title);
            viewHolder.newsListIntro = (TextView) convertView.findViewById(R.id.newsList_content);
            viewHolder.newsListTime = (TextView) convertView.findViewById(R.id.newsList_time);
            viewHolder.newsListImage = (ImageView) convertView.findViewById(R.id.newsList_icon);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.newsListTitle.setText(newsList.get(position).getNewsTitle());
        viewHolder.newsListIntro.setText(newsList.get(position).getNewsIntro());
        viewHolder.newsListTime.setText(newsList.get(position).getNewsTime());
        Glide.with(context)
             .load(newsList.get(position).getNewsImage())
             .centerCrop()
             .placeholder(R.drawable.ic_image_loading_picture)
             .error(R.drawable.ic_image_no_picture)
             .into(viewHolder.newsListImage);
        return convertView;
    }

    /**
     * 把更新后的新闻列表同步到listView里
     *
     * @param list 更新后的新闻列表
     */
    public void addItem(List<News> list) {
        newsList = list;
    }

    /**
     * 起优化作用ListView的ViewHolder类，避免多次加载TextView
     */
    private class ViewHolder {
        private ImageView newsListImage;
        private TextView newsListTitle, newsListIntro, newsListTime;
    }
}
