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

    private static final String TAG = "VRPlayerActivity";
    //SparseArray相当于是一个优化版的HashMap，<>内表示存储的值
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
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_DOME180, "半球 180度");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_DOME230, "半球 230度");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_DOME180_UPPER, "半球 180 UPPER");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_DOME230_UPPER, "半球 230 UPPER");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_STEREO_SPHERE, "STEREO");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_PLANE_FIT, "平面适应");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_PLANE_CROP, "平面拉伸");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_PLANE_FULL, "平面填充");
        sProjectionMode.put(MDVRLibrary.PROJECTION_MODE_MULTI_FISHEYE, "多鱼眼");

        sAntiDistortion.put(1, "显示边框");
        sAntiDistortion.put(0, "隐藏边框");
    }

    /**
     * 开始播放
     */
    public static void startVideo(Context context, Uri uri) {
        start(context, uri, VRVideoPlayerActivity.class);
    }

    public static void startBitmap(Context context, Uri uri){
        start(context, uri, VRImagePlayerActivity.class);
    }

    private static void start(Context context, Uri uri, Class<? extends Activity> cls) {
        Intent intent = new Intent(context, cls);
        intent.setData(uri);
        context.startActivity(intent);
    }


//    private List<MDAbsPlugin> plugins = new LinkedList<>();
//
//    private MDPosition logoPosition = MDPosition.newInstance().setY(-8.0f).setYaw(-90.0f);
//
//    private MDPosition[] positions = new MDPosition[]{
//            MDPosition.newInstance().setZ(-8.0f).setYaw(-45.0f),
//            MDPosition.newInstance().setZ(-18.0f).setYaw(15.0f).setAngleX(15),
//            MDPosition.newInstance().setZ(-10.0f).setYaw(-10.0f).setAngleX(-15),
//            MDPosition.newInstance().setZ(-10.0f).setYaw(30.0f).setAngleX(30),
//            MDPosition.newInstance().setZ(-10.0f).setYaw(-30.0f).setAngleX(-30),
//            MDPosition.newInstance().setZ(-5.0f).setYaw(30.0f).setAngleX(60),
//            MDPosition.newInstance().setZ(-3.0f).setYaw(15.0f).setAngleX(-45),
//            MDPosition.newInstance().setZ(-3.0f).setYaw(15.0f).setAngleX(-45).setAngleY(45),
//            MDPosition.newInstance().setZ(-3.0f).setYaw(0.0f).setAngleX(90),
//    };

    private MDVRLibrary mVRLibrary;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_md_player_using_surface_view);
//        setContentView(R.layout.activity_md_player_using_texture_view);

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

//        findViewById(R.id.button_add_plugin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final int index = (int) (Math.random() * 100) % positions.length;
//                MDPosition position = positions[index];
//                MDSimplePlugin plugin = MDSimplePlugin.builder()
//                                                      .size(4f, 4f)
//                                                      .provider(new MDVRLibrary.IBitmapProvider() {
//                                                          @Override
//                                                          public void onProvideBitmap(MD360BitmapTexture.Callback callback) {
//                                                              Bitmap bitmap = BitmapFactory.decodeResource(getResources(), android.R.drawable.star_on);
//                                                              callback.texture(bitmap);
//                                                          }
//                                                      })
//                                                      .listenClick(new MDVRLibrary.IPickListener() {
//                                                          @Override
//                                                          public void onHotspotHit(IMDHotspot hotspot, long hitTimestamp) {
//                                                              Toast.makeText(VRPlayerActivity.this, "click star" + index, Toast.LENGTH_SHORT)
//                                                                   .show();
//                                                          }
//                                                      })
//                                                      .title("star" + index)
//                                                      .position(position)
//                                                      .build();
//
//                plugins.add(plugin);
//                getVRLibrary().addPlugin(plugin);
//                Toast.makeText(VRPlayerActivity.this, "add plugin position:" + position, Toast.LENGTH_SHORT)
//                     .show();
//            }
//        });

//        findViewById(R.id.button_add_plugin_logo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MDSimplePlugin plugin = MDSimplePlugin.builder()
//                                                      .size(4f, 4f)
//                                                      .provider(new MDVRLibrary.IBitmapProvider() {
//                                                          @Override
//                                                          public void onProvideBitmap(MD360BitmapTexture.Callback callback) {
//                                                              Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.moredoo_logo);
//                                                              callback.texture(bitmap);
//                                                          }
//                                                      })
//                                                      .title("logo")
//                                                      .position(logoPosition)
//                                                      .listenClick(new MDVRLibrary.IPickListener() {
//                                                          @Override
//                                                          public void onHotspotHit(IMDHotspot hotspot, long hitTimestamp) {
//                                                              Toast.makeText(VRPlayerActivity.this, "click logo", Toast.LENGTH_SHORT)
//                                                                   .show();
//                                                          }
//                                                      })
//                                                      .build();
//
//                plugins.add(plugin);
//                getVRLibrary().addPlugin(plugin);
//                Toast.makeText(VRPlayerActivity.this, "add plugin logo", Toast.LENGTH_SHORT)
//                     .show();
//            }
//        });

//        findViewById(R.id.button_remove_plugin).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (plugins.size() > 0) {
//                    MDAbsPlugin plugin = plugins.remove(plugins.size() - 1);
//                    getVRLibrary().removePlugin(plugin);
//                }
//            }
//        });
//
//        findViewById(R.id.button_remove_plugins).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                plugins.clear();
//                getVRLibrary().removePlugins();
//            }
//        });

//        final TextView hotspotText = (TextView) findViewById(R.id.hotspot_text);
//        getVRLibrary().setEyePickChangedListener(new MDVRLibrary.IPickListener() {
//            @Override
//            public void onHotspotHit(IMDHotspot hotspot, long hitTimestamp) {
//                String text = hotspot == null ? "nop" : String.format(Locale.CHINESE, "%s  %fs", hotspot
//                        .getTitle(), (System.currentTimeMillis() - hitTimestamp) / 1000.0f);
//                hotspotText.setText(text);
//            }
//        });
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