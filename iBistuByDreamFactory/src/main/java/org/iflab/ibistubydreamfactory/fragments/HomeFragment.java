package org.iflab.ibistubydreamfactory.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.activities.BusActivity;
import org.iflab.ibistubydreamfactory.activities.LostFoundActivity;
import org.iflab.ibistubydreamfactory.activities.MapActivity;
import org.iflab.ibistubydreamfactory.activities.NewsActivity;
import org.iflab.ibistubydreamfactory.activities.VRActivity;
import org.iflab.ibistubydreamfactory.activities.YellowPageActivity;
import org.iflab.ibistubydreamfactory.adapters.HomeAdapter;
import org.iflab.ibistubydreamfactory.models.HomeItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 绘制GridView
 */
public class HomeFragment extends Fragment {
    @BindView(R.id.gridView_home)
    GridView gridViewHome;
    private List<HomeItem> modules;//存放所有模块的列表
    private View rootView;//fragment的主界面

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, rootView);
        initGridView();
        return rootView;

    }

    public void initGridView() {
        modules = new ArrayList<>();
        modules.add(new HomeItem(R.drawable.ic_module_news, "新闻", NewsActivity.class));
        modules.add(new HomeItem(R.drawable.ic_module_yellowpage, "黄页", YellowPageActivity.class));
        modules.add(new HomeItem(R.drawable.ic_module_map, "地图", MapActivity.class));
        modules.add(new HomeItem(R.drawable.ic_module_schoolbus, "校车", BusActivity.class));
        modules.add(new HomeItem(R.drawable.ic_module_vr, "全景", VRActivity.class));
        modules.add(new HomeItem(R.drawable.ic_module_lost_found, "招领", LostFoundActivity.class));
        //        modules.add(new HomeItem(R.drawable.wifi, "WiFi", WiFiActivity.class));
        //        modules.add(new HomeItem(R.drawable.job, "工作", JobActivity.class));
        //        modules.add(new HomeItem(R.drawable.group, "群组", GroupActivity.class));
        //        modules.add(new HomeItem(R.drawable.secondhand, "二手", SecondHandActivity.class));
        //        modules.add(new HomeItem(R.drawable.classroom, "教室", ClassRoomActivity.class));
        //        modules.add(new HomeItem(R.drawable.grade, "成绩", GradeActivity.class));

        gridViewHome.setAdapter(new HomeAdapter(modules, HomeFragment.this.getActivity()));

        gridViewHome.setOnItemClickListener(new MyItemListener());


    }


    private class MyItemListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            startActivity(new Intent(HomeFragment.this.getActivity(), modules.get(position)
                                                                             .getItemModule()));
        }
    }


}
