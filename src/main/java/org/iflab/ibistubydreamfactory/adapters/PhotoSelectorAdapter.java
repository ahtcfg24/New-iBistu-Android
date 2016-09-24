package org.iflab.ibistubydreamfactory.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import org.iflab.ibistubydreamfactory.R;

import java.io.File;
import java.util.ArrayList;


/**
 * 从本地选择图片的适配器
 */
public class PhotoSelectorAdapter extends RecyclerView.Adapter<PhotoSelectorAdapter.PhotoViewHolder> {

    public static int MAX_SELECT_PHOTOS_COUNT = 9;//最多可选择9张图片
    private ArrayList<String> photoPaths = new ArrayList<>();
    private LayoutInflater inflater;
    private Context mContext;


    public PhotoSelectorAdapter(Context mContext, ArrayList<String> photoPaths) {
        this.photoPaths = photoPaths;
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);

    }


    @Override
    public PhotoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(me.iwf.photopicker.R.layout.__picker_item_photo, parent, false);
        return new PhotoViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final PhotoViewHolder holder, final int position) {
        if (position == photoPaths.size()) {
            holder.ivPhoto.setImageResource(R.drawable.ic_image_upload);
            if (position == MAX_SELECT_PHOTOS_COUNT) {//选择了最大图片数量时就自动隐藏选择图片入口
                holder.ivPhoto.setVisibility(View.GONE);
            }
        } else {
            Uri uri = Uri.fromFile(new File(photoPaths.get(position)));
            Glide.with(mContext)
                 .load(uri)
                 .centerCrop()//选择图片列表中预览图显示效果
                 .placeholder(R.drawable.ic_image_loading_picture)
                 .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp)
                 .into(holder.ivPhoto);
        }

    }


    @Override
    public int getItemCount() {
        if (photoPaths.size() != MAX_SELECT_PHOTOS_COUNT) {
            return photoPaths.size() + 1;
        }
        return MAX_SELECT_PHOTOS_COUNT;
    }


    static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;

        PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(me.iwf.photopicker.R.id.iv_photo);
            vSelected = itemView.findViewById(me.iwf.photopicker.R.id.v_selected);
            vSelected.setVisibility(View.GONE);
        }
    }

}
