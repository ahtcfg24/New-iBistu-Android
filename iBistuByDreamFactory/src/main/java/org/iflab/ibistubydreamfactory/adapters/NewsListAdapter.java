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
    private static int TYPE_FIRST_NEWS = 0;
    private static int TYPE_NORMAL_NEWS = 1;
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

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_FIRST_NEWS;
        } else {
            return TYPE_NORMAL_NEWS;
        }
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
        if (getItemViewType(position) == TYPE_FIRST_NEWS) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_first_news, null);
            TextView textViewTitle = (TextView) convertView.findViewById(R.id.title);
            TextView textViewContent = (TextView) convertView.findViewById(R.id.content);
            ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
            textViewTitle.setText(newsList.get(position).getNewsTitle());
            textViewContent.setText(newsList.get(position).getNewsIntro());
            Glide.with(context)
                 .load(newsList.get(position).getNewsImage())
                 .fitCenter()
                 .placeholder(R.drawable.ic_image_loading_picture)
                 .error(R.drawable.ic_image_no_picture)
                 .into(imageView);
        } else {
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
        }
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
