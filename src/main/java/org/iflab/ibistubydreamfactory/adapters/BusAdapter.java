package org.iflab.ibistubydreamfactory.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.iflab.ibistubydreamfactory.models.Bus;

import java.util.List;

/**
 * 班车列表适配器
 */
public class BusAdapter extends BaseAdapter{
    private List<Bus> busList;
    private Context context;
    private ViewHolder viewHolder;

    public BusAdapter(List<Bus> busList, Context context) {
        this.busList = busList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return busList.size();
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
//        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.item_yellow_page_depart, null);
//            viewHolder = new ViewHolder();
//            viewHolder.yellowPageItemTextView = (TextView) convertView.findViewById(R.id.yellowPage_item_textView);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        viewHolder.yellowPageItemTextView.setText(busList.get(position).getName());
        return convertView;
    }

    /**
     * 起优化作用ListView的ViewHolder类，避免多次加载TextView
     */
    private class ViewHolder {
        private TextView yellowPageItemTextView;
    }
}
