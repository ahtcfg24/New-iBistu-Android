package org.iflab.ibistubydreamfactory.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.models.VRMedia;

import java.util.List;

/**
 *
 */
public class VRMediaAdapter extends RecyclerView.Adapter {
    public static final int TYPE_HEADER = 0;
    private static final int TYPE_VIDEO = 1;
    private static final int TYPE_IMAGE = 2;
    private static final int TYPE_NONE = 3;
    private LayoutInflater mInflater;
    private Context context;
    private List<VRMedia> vrMediaList;
    private MyOnItemClickListener myOnItemClickListener;


    public VRMediaAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    /**
     * 当需要展示一个新的类型的item时会调用该方法去创建一个新类型的holder
     */
    @Override
    public VRMediaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType != TYPE_NONE) {
            return new VRMediaViewHolder(mInflater.inflate(R.layout.item_vr_meida, parent, false), viewType);
        } else {
            return new VRMediaViewHolder(mInflater.inflate(R.layout.item_vr_none, parent, false), viewType);
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) != TYPE_NONE) {
            final VRMedia vrMedia = vrMediaList.get(position);
            VRMediaViewHolder vrMediaViewHolder = (VRMediaViewHolder) holder;
            vrMediaViewHolder.textViewTitle.setText(vrMedia.getTitle());
            Glide.with(context)
                 .load(vrMedia.getPreview())
                 .fitCenter()
                 .placeholder(R.drawable.ic_image_loading_picture)
                 .error(R.drawable.ic_image_error_picture)
                 .into(vrMediaViewHolder.imageViewPreview);
            //设置监听回调,当子类传入了监听器类的对象时，就会调用子类中实现的onItemClick方法，
            // 并传入holder.itemView和itemPosition这两个参数
            if (myOnItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int itemPosition = holder.getLayoutPosition();
                        myOnItemClickListener.onItemClick(holder.itemView, itemPosition, vrMedia.getType(), vrMedia
                                .getResource());
                    }
                });
            }
        }
    }


    @Override
    public int getItemCount() {
        return vrMediaList.size();
    }

    /**
     * 返回使用的布局类型
     */
    @Override
    public int getItemViewType(int position) {
        switch (vrMediaList.get(position).getType()) {
            case "VIDEO":
                return TYPE_VIDEO;
            case "IMAGE":
                return TYPE_IMAGE;
            default:
                return TYPE_NONE;
        }
    }


    /**
     * 把更新后的列表同步
     *
     * @param list 更新后的列表
     */
    public void addItem(List<VRMedia> list) {
        vrMediaList = list;
    }

    /**
     * 自定义设置监听器方法，在子类中添加监听器
     */
    public void setMyOnItemClickListener(MyOnItemClickListener myOnItemClickListener) {
        this.myOnItemClickListener = myOnItemClickListener;
    }

    /**
     * 自定义监听器接口
     */
    public interface MyOnItemClickListener {
        /**
         * 点击item时响应的方法
         *
         * @param view 所点击的View
         * @param position 所点击的View的位置
         * @param type 点击的item的资源类型
         * @param url 点击的item的资源url
         */
        void onItemClick(View view, int position, String type, String url);
    }

    /**
     * ViewHolder
     */
    private class VRMediaViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewPreview, imageViewType;
        private TextView textViewTitle;

        VRMediaViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType != TYPE_NONE) {
                imageViewPreview = (ImageView) itemView.findViewById(R.id.imageView_preview);
                imageViewType = (ImageView) itemView.findViewById(R.id.imageView_Type);
                textViewTitle = (TextView) itemView.findViewById(R.id.text_title);
                if (viewType == TYPE_VIDEO) {
                    imageViewType.setImageResource(R.drawable.ic_action_video);
                }
            }
        }

    }
}
