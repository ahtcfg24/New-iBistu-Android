package org.iflab.ibistubydreamfactory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.models.Bus;
import org.iflab.ibistubydreamfactory.models.BusType;

import java.util.List;

/**
 * 班车列表适配器
 * 分类显示listView
 * 参考：http://blog.csdn.net/androiddevelop/article/details/8316759
 */
public class BusAdapter extends BaseAdapter {
    private List<BusType> busData;
    private Context context;
    private ViewHolder viewHolder;
    private final int TYPE_NOT_ITEM = 0;//用0表示不是item类型
    private final int TYPE_ITEM = 1;//用1表示item类型
    private String busTypeName = "默认值";

    public BusAdapter(List<BusType> busData, Context context) {
        this.busData = busData;
        this.context = context;
    }

    @Override
    public int getCount() {
        int rowCount = 0;
        for (BusType busType : busData) {
            rowCount = busType.getItemCount() + rowCount;
        }
        return rowCount;
    }

    @Override
    public Bus getItem(int position) {
        // 同一种班车数据的起始位置
        int FirstPosition = 0;
        for (BusType busType : busData) {
            int size = busType.getItemCount();//获得某一种班车数据的个数
            // 在当前分类中的索引值
            int busTypeInnerPosition = position - FirstPosition;
            // 当一种班车内部position<这个班车数据总数时，说明item来自当前班车内部
            if (busTypeInnerPosition < size) {
                if (busTypeInnerPosition == 0) {
                    return null;
                } else {
                    return busType.getItem(busTypeInnerPosition);
                }
            }
            // 索引移动到当前分类结尾，即下一个分类第一个元素索引
            FirstPosition = size + FirstPosition;

        }
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    /**
     * 如果当前bus对象的busType属性不同于上一个bus对象的busType属性
     * 就返回BUSTYPE_ITEM，表示这一行应该插入班车类型（通勤班车或者教学班车）item
     * 返回值必须在0到getViewTypeCount()-1之间，此处必须是0或者1
     */
    @Override
    public int getItemViewType(int position) {
        int FirstPosition = 0;
        for (BusType busType : busData) {
            int size = busType.getItemCount();
            // 在当前分类中的索引值
            int busTypeInnerPosition = position - FirstPosition;
            if (busTypeInnerPosition == 0) {
                busTypeName = busType.getBusTypeName();
                return TYPE_NOT_ITEM;
            }
            FirstPosition = size + FirstPosition;
        }

        return TYPE_ITEM;
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
        if (getItemViewType(position) == TYPE_NOT_ITEM) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bus_type, null);
            TextView textViewBusType = (TextView) convertView.findViewById(R.id.textView_busType);
            textViewBusType.setText(busTypeName);
        } else if (getItemViewType(position) == TYPE_ITEM) {
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_bus, null);
                viewHolder.textViewBusName = (TextView) convertView.findViewById(R.id.textView_busName);
                viewHolder.textViewBusIntro = (TextView) convertView.findViewById(R.id.textView_busIntro);
                viewHolder.textViewGoTime = (TextView) convertView.findViewById(R.id.textView_goTime);
                viewHolder.textViewBackTime = (TextView) convertView.findViewById(R.id.textView_backTime);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            Bus bus = getItem(position);
            viewHolder.textViewBusName.setText(bus.getBusName());
            viewHolder.textViewBusIntro.setText(bus.getBusIntro());
            viewHolder.textViewGoTime.setText(bus.getDepartureTime());
            viewHolder.textViewBackTime.setText(bus.getReturnTime());

        }
        return convertView;
    }


    /**
     * 设定TYPE_NOT_ITEM为分隔行，不可点击
     */
    @Override
    public boolean isEnabled(int position) {
        return getItemViewType(position) != TYPE_NOT_ITEM;
    }

    /**
     * 有分隔行使返回false
     */
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    /**
     * 起优化作用ListView的ViewHolder类，避免多次加载TextView
     */
    private class ViewHolder {
        private TextView textViewBusName;
        private TextView textViewBusIntro;
        private TextView textViewGoTime;
        private TextView textViewBackTime;

    }


}
