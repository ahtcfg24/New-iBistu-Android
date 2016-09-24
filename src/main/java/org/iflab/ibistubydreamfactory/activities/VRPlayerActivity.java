package org.iflab.ibistubydreamfactory.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.asha.vrlib.MDVRLibrary;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.utils.SpinnerBuildUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * VR播放器抽象类
 */
public abstract class VRPlayerActivity extends Activity {

    private static final SparseArray<String> sDisplayMode = new SparseArray<>();
    private static final SparseArray<String> sInteractiveMode = new SparseArray<>();
    private static final SparseArray<String> sProjectionMode = new SparseArray<>();
    private static final SparseArray<String> sAntiDistortion = new SparseArray<>();

    static {
        sDisplayMode.put(MDVRLibrary.DISPLAY_MODE_NORMAL, "普通模式");
        sDisplayMode.put(MDVRLibrary.DISPLAY_MODE_GLASS, "眼镜模式");

        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_MOTION, "感应控制");
        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_TOUCH, "滑屏控制");
        sInteractiveMode.put(MDVRLibrary.INTERACTIVE_MODE_MOTION_WITH_TOUCH, "感应+滑屏");

        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_SPHERE, "SPHERE视角");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_STEREO_SPHERE, "STEREO");

        sAntiDistortion.put(1, "显示边框");
        sAntiDistortion.put(0, "隐藏边框");
    }

    private MDVRLibrary mVRLibrary;

    /**
     * 开始播放
     */
    public static void startVideo(Context context, Uri uri) {
        start(context, uri, VRVideoPlayerActivity.class);
    }

    public static void startBitmap(Context context, Uri uri) {
        start(context, uri, VRImagePlayerActivity.class);
    }

    private static void start(Context context, Uri uri, Class<? extends Activity> cls) {
        Intent intent = new Intent(context, cls);
        intent.setData(uri);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_md_player_using_surface_view);

        // init VR Library
        mVRLibrary = createVRLibrary();

        final List<View> hotspotPoints = new LinkedList<>();
        hotspotPoints.add(findViewById(R.id.hotspot_point1));
        hotspotPoints.add(findViewById(R.id.hotspot_point2));

        SpinnerBuildUtil.with(this)
                        .setData(sDisplayMode)
                        .setDefault(mVRLibrary.getDisplayMode())
                        .setClickHandler(new SpinnerBuildUtil.ClickHandler() {
                            @Override
                            public void onSpinnerClicked(int index, int key, String value) {
                                mVRLibrary.switchDisplayMode(VRPlayerActivity.this, key);
                                int i = 0;
                                for (View point : hotspotPoints) {
                                    point.setVisibility(i < mVRLibrary.getScreenSize() ? View.VISIBLE : View.GONE);
                                    i++;
                                }
                            }
                        })
                        .init(R.id.spinner_display);

        SpinnerBuildUtil.with(this)
                        .setData(sInteractiveMode)
                        .setDefault(mVRLibrary.getInteractiveMode())
                        .setClickHandler(new SpinnerBuildUtil.ClickHandler() {
                            @Override
                            public void onSpinnerClicked(int index, int key, String value) {
                                mVRLibrary.switchInteractiveMode(VRPlayerActivity.this, key);
                            }
                        })
                        .init(R.id.spinner_interactive);

        SpinnerBuildUtil.with(this)
                        .setData(sProjectionMode)
                        .setDefault(mVRLibrary.getProjectionMode())
                        .setClickHandler(new SpinnerBuildUtil.ClickHandler() {
                            @Override
                            public void onSpinnerClicked(int index, int key, String value) {
                                mVRLibrary.switchProjectionMode(VRPlayerActivity.this, key);
                            }
                        })
                        .init(R.id.spinner_projection);

        SpinnerBuildUtil.with(this)
                        .setData(sAntiDistortion)
                        .setDefault(mVRLibrary.isAntiDistortionEnabled() ? 1 : 0)
                        .setClickHandler(new SpinnerBuildUtil.ClickHandler() {
                            @Override
                            public void onSpinnerClicked(int index, int key, String value) {
                                mVRLibrary.setAntiDistortionEnabled(key != 0);
                            }
                        })
                        .init(R.id.spinner_distortion);

    }

    protected Uri getUri() {
        Intent i = getIntent();
        if (i == null || i.getData() == null) {
            return null;
        }
        return i.getData();
    }

    public void cancelBusy() {
        findViewById(R.id.progress).setVisibility(View.GONE);
    }

    abstract protected MDVRLibrary createVRLibrary();

    public MDVRLibrary getVRLibrary() {
        return mVRLibrary;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mVRLibrary.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVRLibrary.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVRLibrary.onDestroy();
    }


}