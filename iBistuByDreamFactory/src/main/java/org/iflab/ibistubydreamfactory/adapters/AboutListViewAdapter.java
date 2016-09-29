package org.iflab.ibistubydreamfactory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.models.About;

import java.util.List;

public class AboutListViewAdapter extends BaseAdapter {
    private List<About> aboutItemList;
    private Context context;
    private TextView aboutItemTextView;


    public AboutListViewAdapter(List<About> aboutItemList, Context context) {
        this.aboutItemList = aboutItemList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return aboutItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

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
        if (convertView == null) {
                /*绑定控件资源*/
            convertView = LayoutInflater.from(context).inflate(R.layout.item_about, null);
                /*因为是要在convertView中找到TextView，因此不能省略前面的convertView，否则就是在Layout中找*/
            aboutItemTextView = (TextView) convertView.findViewById(R.id.about_item_textView);
        }
            /*填充列表文字*/
        aboutItemTextView.setText(aboutItemList.get(position).getAboutName());
        return convertView;
    }
}
