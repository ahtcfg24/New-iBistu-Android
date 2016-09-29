package org.iflab.ibistubydreamfactory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.models.BusStation;

import java.util.List;

/**
 *
 */
public class BusLineAdapter extends BaseAdapter {
    private final int TYPE_ITEM=0;
    private final int TYPE_FOOTER=1;
    private final int TYPE_HEADER=2;
    private List<BusStation> busStationList;
    private Context context;


    public BusLineAdapter(List<BusStation> busStationList, Context context) {
        this.busStationList = busStationList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return busStationList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0){
            return TYPE_HEADER;
        }else if (position==busStationList.size() - 1){
            return TYPE_FOOTER;
        }
        return TYPE_ITEM ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == TYPE_HEADER) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bus_line_header, null);
            TextView textViewHeaderStation = (TextView) convertView.findViewById(R.id.textView_header_station);
            TextView textViewHeaderArrivalTime = (TextView) convertView.findViewById(R.id.textView_header_arrivalTime);

            textViewHeaderStation.setText(busStationList.get(position).getStation());
            textViewHeaderArrivalTime.setText(busStationList.get(position).getArrivalTime());

        } else if (getItemViewType(position) == TYPE_FOOTER) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_bus_line_footer, null);
            TextView textViewFooterStation = (TextView) convertView.findViewById(R.id.textView_footer_station);
            TextView textViewFooterArrivalTime = (TextView) convertView.findViewById(R.id.textView_footer_arrivalTime);

            textViewFooterStation.setText(busStationList.get(position).getStation());
            textViewFooterArrivalTime.setText(busStationList.get(position).getArrivalTime());

        } else if(getItemViewType(position)==TYPE_ITEM){
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_bus_line, null);
                viewHolder.textViewStation = (TextView) convertView.findViewById(R.id.textView_station);
                viewHolder.textViewArrivalTime = (TextView) convertView.findViewById(R.id.textView_arrivalTime);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.textViewStation.setText(busStationList.get(position).getStation());
            viewHolder.textViewArrivalTime.setText(busStationList.get(position).getArrivalTime());
        }
        return convertView;
    }

    /**
     * 起优化作用ListView的ViewHolder类，避免多次加载TextView
     */
    private class ViewHolder {
        private TextView textViewStation;
        private TextView textViewArrivalTime;

    }
}
