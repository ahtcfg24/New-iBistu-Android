package org.iflab.ibistubydreamfactory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.models.LostFound;
import org.iflab.ibistubydreamfactory.models.LostFoundImageURL;
import org.iflab.ibistubydreamfactory.utils.ACache;
import org.iflab.ibistubydreamfactory.utils.JsonUtils;
import org.iflab.ibistubydreamfactory.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LostFoundListAdapter extends BaseAdapter {
    private List<LostFound> lostFoundList;
    private Context context;
    private String SESSION_TOKEN = ACache.get(MyApplication.getAppContext())
                                         .getAsString("SESSION_TOKEN");

    public LostFoundListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return lostFoundList.size();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lost_found, null);
            viewHolder = new ViewHolder();
            viewHolder.lostFoundTitle = (TextView) convertView.findViewById(R.id.title_lost_found);
            viewHolder.lostFoundIntro = (TextView) convertView.findViewById(R.id.content_lost_found);
            viewHolder.lostFoundTime = (TextView) convertView.findViewById(R.id.time_lost_found);
            viewHolder.nineGridView = (NineGridView) convertView.findViewById(R.id.nineGridView_images);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        LostFound lostFound = lostFoundList.get(position);
        viewHolder.lostFoundTitle.setText(lostFound.getTitle());
        viewHolder.lostFoundIntro.setText(lostFound.getDetails());
        viewHolder.lostFoundTime.setText(StringUtil.getToZeroTime(lostFound.getCreateTime()));
        List<LostFoundImageURL> list = JsonUtils.json2List(lostFound.getImgUrlList(), LostFoundImageURL.class);
        if (list != null) {
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            for (LostFoundImageURL url : list) {
                String imageUrl = url.getUrl() + "?api_key=" + MyApplication.API_KEY + "&session_token=" + SESSION_TOKEN;
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(imageUrl);
                info.setBigImageUrl(imageUrl);
                imageInfo.add(info);
            }
            viewHolder.nineGridView.setAdapter(new NineGridViewClickAdapter(context, imageInfo));
        }

        return convertView;
    }

    /**
     * 把更新后的新闻列表同步到listView里
     *
     * @param list 更新后的新闻列表
     */
    public void addItem(List<LostFound> list) {
        lostFoundList = list;
    }

    /**
     * 起优化作用ListView的ViewHolder类，避免多次加载TextView
     */
    private class ViewHolder {
        private TextView lostFoundTitle, lostFoundIntro, lostFoundTime;
        private NineGridView nineGridView;
    }
}
