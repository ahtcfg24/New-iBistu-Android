package org.iflab.ibistubydreamfactory.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.asha.vrlib.MDVRLibrary;
import com.asha.vrlib.model.MDRay;
import com.asha.vrlib.plugins.IMDHotspot;
import com.asha.vrlib.texture.MD360BitmapTexture;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.iflab.ibistubydreamfactory.R;

import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;

/**
 * 全景图片播放器
 */
public class VRImagePlayerActivity extends VRPlayerActivity {

    private static final String TAG = "VRImagePlayerActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cancelBusy();
    }

    private Target mTarget;// keep the reference for picasso.

    private void loadImage(Uri uri, final MD360BitmapTexture.Callback callback){
        mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                // notify if size changed
                getVRLibrary().onTextureResize(bitmap.getWidth(),bitmap.getHeight());

                // texture
                callback.texture(bitmap);
                cancelBusy();
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        Picasso.with(getApplicationContext()).load(uri).resize(3072,2048).centerInside().memoryPolicy(NO_CACHE, NO_STORE).into(mTarget);
    }

    @Override
    protected MDVRLibrary createVRLibrary() {
        return MDVRLibrary.with(this)
                .displayMode(MDVRLibrary.DISPLAY_MODE_NORMAL)
                .interactiveMode(MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH)
                .asBitmap(new MDVRLibrary.IBitmapProvider() {
                    @Override
                    public void onProvideBitmap(final MD360BitmapTexture.Callback callback) {
                        loadImage(getUri(),callback);
                    }
                })
                .listenGesture(new MDVRLibrary.IGestureListener() {
                    @Override
                    public void onClick(MotionEvent e, MDRay ray, IMDHotspot hitHotspot) {
                        Log.d(TAG,"Ray:" + ray + ", hitHotspot:" + hitHotspot);
                    }
                })
                .pinchEnabled(true)
                .build(R.id.gl_view);
    }
}
