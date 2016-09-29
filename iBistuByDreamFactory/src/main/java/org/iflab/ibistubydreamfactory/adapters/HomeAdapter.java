package org.iflab.ibistubydreamfactory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.models.HomeItem;

import java.util.List;

/**
 * 自定义的用于绑定模块数据和GridView的Adapter
 */
public class HomeAdapter extends BaseAdapter {
    private List<HomeItem> items;//存放从GridView传过来的items
    private Context context;//代表GridView所在的Activity
    private TextView textView;
    private ImageView imageView;

    public HomeAdapter(List<HomeItem> items, Context context) {
        this.items = items;
        this.context = context;
    }

    /**
     * @return 要绘制的View数目，不能大于实际存在资源总数
     */
    @Override
    public int getCount() {
        return items.size();
    }

    /**
     * 覆盖自AdapteView，此处不会自动调用
     *
     * @param position 正在点击的item在数据集合中的的位置
     * @return 该位置处的item对象
     */
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    /**
     * 点击时调用
     *
     * @param position 正在点击的item在数据集合中的的位置
     * @return 当前选项所在的位置的id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 绘制每个item
     *
     * @param position    item的位置
     * @param convertView 即将出缓冲区再度进入内存被重新利用的View,这里屏幕未占满，始终为空
     * @param parent      item的父级容器
     * @return 要显示的每个Item的View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        /**
         * 如果Recycle缓冲区里没有可用的View，那么就从资源加载
         */
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_home, null);
            convertView.setTag("使convertView不为空");
            //findViewById是用来在父控件中找子控件的
            imageView = (ImageView) convertView.findViewById(R.id.home_icon);
            textView = (TextView) convertView.findViewById(R.id.home_name);
        }
        imageView.setImageResource(items.get(position).getIconId());
        textView.setText(items.get(position).getItemName());
        return convertView;
    }
}
