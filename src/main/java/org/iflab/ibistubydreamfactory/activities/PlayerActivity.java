package org.iflab.ibistubydreamfactory.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.view.EditSpinner;

import java.util.ArrayList;
import java.util.List;

/**
 * 主界面.
 */
public class PlayerActivity extends AppCompatActivity {
    private EditSpinner mEditSpinner;
    private List<String> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mEditSpinner = (EditSpinner) findViewById(R.id.edit_spinner);
        list = new ArrayList<>();
        list.add("全景视频示例：http://7xlbe9.com1.z0.glb.clouddn.com/Kodak%20PixPro%20SP360%20.mp4");
        list.add("全景图片示例：http://7xp4hv.com1.z0.glb.clouddn.com/image/vr/20098c.jpg");
        list.add("STEREO镜头示例：http://7xp4hv.com1.z0.glb.clouddn.com/image/vr/stereo.jpg");
        list.add("多重鱼眼镜头示例：http://7xp4hv.com1.z0.glb.clouddn.com/image/vr/multifisheye.png");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                list);
        mEditSpinner.setAdapter(adapter);
        mEditSpinner.setDropDownDrawableSpacing(50);


        mEditSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mEditSpinner.setText(list.get(position).split("：")[1]);
            }
        });



        /**
         * 播放VR视频
         */
        findViewById(R.id.video_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mEditSpinner.getText().toString();
                if (!TextUtils.isEmpty(url)){
                    VRPlayerActivity.startVideo(PlayerActivity.this, Uri.parse(url));
                } else {
                    Toast.makeText(PlayerActivity.this, "URL不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.bitmap_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mEditSpinner.getText().toString();
                if (!TextUtils.isEmpty(url)){
                    VRPlayerActivity.startBitmap(PlayerActivity.this, Uri.parse(url));
                } else {
                    Toast.makeText(PlayerActivity.this, "URL不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });

        /**
         * 播放普通视频
         */
        findViewById(R.id.ijk_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mEditSpinner.getText().toString();
                if (!TextUtils.isEmpty(url)){
                    NormalPlayerActivity.start(PlayerActivity.this, Uri.parse(url));
                } else {
                    Toast.makeText(PlayerActivity.this, "URL不能为空！", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
