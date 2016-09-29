package org.iflab.ibistubydreamfactory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.models.YellowPageDepartment;

import java.util.List;

/**
 * 绑定列表与黄页部门列表数据的适配器
 */
public class YellowPageAdapter extends BaseAdapter {

    private List<YellowPageDepartment> yellowPageDepartmentList;
    private Context context;

    public YellowPageAdapter(List<YellowPageDepartment> yellowPageDepartmentList, Context context) {
        this.yellowPageDepartmentList = yellowPageDepartmentList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return yellowPageDepartmentList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 绘制每个item
     *
     * @param position    点击的位置
     * @param convertView item对应的View
     * @param parent      可选的父控件
     * @return 要显示的单个item的View
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_yellow_page_depart, null);
            viewHolder = new ViewHolder();
            viewHolder.yellowPageItemTextView = (TextView) convertView.findViewById(R.id.yellowPage_item_textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.yellowPageItemTextView.setText(yellowPageDepartmentList.get(position).getName());
        return convertView;
    }

    /**
     * 起优化作用ListView的ViewHolder类，避免多次加载TextView
     */
    private class ViewHolder {
        private TextView yellowPageItemTextView;
    }
}


