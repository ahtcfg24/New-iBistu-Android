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
 * 选择图片适配器
 */
public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private static int MAX_SELECT_PHOTOS_COUNT = 3;//最多可选择三张图片
    private ArrayList<String> photoPaths = new ArrayList<>();
    private LayoutInflater inflater;
    private Context mContext;


    public PhotoAdapter(Context mContext, ArrayList<String> photoPaths) {
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
            Glide.with(mContext)
                 .load(R.drawable.ic_image_upload)
                 .centerCrop()
                 .thumbnail(0.1f)
                 .into(holder.ivPhoto);
            if (position == MAX_SELECT_PHOTOS_COUNT) {//选择了最大图片数量时就自动隐藏选择图片入口
                holder.ivPhoto.setVisibility(View.GONE);
            }
        } else {
            Uri uri = Uri.fromFile(new File(photoPaths.get(position)));
            Glide.with(mContext)
                 .load(uri)
                 .centerCrop()
                 .thumbnail(0.1f)
                 .placeholder(me.iwf.photopicker.R.drawable.__picker_ic_photo_black_48dp)
                 .error(me.iwf.photopicker.R.drawable.__picker_ic_broken_image_black_48dp)
                 .into(holder.ivPhoto);
        }

    }


    @Override
    public int getItemCount() {
        if (photoPaths.size() == MAX_SELECT_PHOTOS_COUNT) {
            return 3;
        }
        return photoPaths.size() + 1;
    }


    public static class PhotoViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivPhoto;
        private View vSelected;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(me.iwf.photopicker.R.id.iv_photo);
            vSelected = itemView.findViewById(me.iwf.photopicker.R.id.v_selected);
            vSelected.setVisibility(View.GONE);
        }
    }

}
