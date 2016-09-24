package org.iflab.ibistubydreamfactory.activities;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.MyTrafficStyle;

import org.iflab.ibistubydreamfactory.MyApplication;
import org.iflab.ibistubydreamfactory.R;
import org.iflab.ibistubydreamfactory.apis.APISource;
import org.iflab.ibistubydreamfactory.apis.MapAPI;
import org.iflab.ibistubydreamfactory.models.ErrorMessage;
import org.iflab.ibistubydreamfactory.models.MapLocation;
import org.iflab.ibistubydreamfactory.models.Resource;
import org.iflab.ibistubydreamfactory.utils.ACache;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * AMapV2地图中简单介绍矢量地图和卫星地图模式切换
 */
public class MapActivity extends AppCompatActivity implements LocationSource, AMapLocationListener {
    private static final int PERMISSION_CODE_LOCATION = 001;
    private AMap map;
    private MapView mapView;
    private int mapType;
    private boolean isShowTraffic = false;
    private Resource<MapLocation> mapLocationResource;
    private ACache aCache;
    private List<MapLocation> mapLocationList;
    private List<LatLng> latLngList = new ArrayList<>();//存储地图上所有marker的坐标


    private AMapLocationClient locationClient;//定位器
    private OnLocationChangedListener locationChangedListener;//定位监听器
    private LatLng currentPosition;//当前位置坐标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mapView = (MapView) findViewById(R.id.mapView_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        if (mapLocationResource == null) {
            /*如果缓存没有就从网络获取*/
            getMapLocationResource();
        } else {
            loadData();
        }
    }


    /**
     * 初始化
     */
    private void init() {
        if (map == null) {
            map = mapView.getMap();
        }
        aCache = ACache.get(MyApplication.getAppContext());
        mapLocationResource = (Resource<MapLocation>) aCache.getAsObject("mapLocationResource");
    }


    /**
     * 获取数据
     */
    private void getMapLocationResource() {
        MapAPI mapAPI = APISource.getInstance().getAPIObject(MapAPI.class);
        Call<Resource<MapLocation>> call = mapAPI.getMapLocation();
        call.enqueue(new Callback<Resource<MapLocation>>() {
            @Override
            public void onResponse(Call<Resource<MapLocation>> call, Response<Resource<MapLocation>> response) {
                if (response.isSuccessful()) {
                    mapLocationResource = response.body();
                    aCache.put("mapLocationResource", mapLocationResource);
                    loadData();
                } else {
                    ErrorMessage e = APISource.getErrorMessage(response);//解析错误信息
                    onFailure(call, e.toException());
                }
            }

            @Override
            public void onFailure(Call<Resource<MapLocation>> call, Throwable t) {
                System.out.println("error：" + t.toString());

            }
        });
    }

    /**
     * 处理数据
     */
    private void loadData() {
        mapLocationList = mapLocationResource.getResource();
        setMapType();
        addMarkersToMap();
        if (Build.VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            setLocation();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, PERMISSION_CODE_LOCATION);
        }
    }

    /**
     * 授权回调
     *
     * @param grantResults grantResults[i] 对应的权限用户是否授权
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE_LOCATION://同类权限只要有一个被允许，其他都算允许
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {//用户授权了
                    setLocation();
                } else {
                    Toast.makeText(MapActivity.this, "没有定位权限，定位功能无法使用！", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 设置地图类型
     */
    private void setMapType() {
        mapType = AMap.MAP_TYPE_NORMAL;
        map.setMapType(mapType);
        MyTrafficStyle myTrafficStyle = new MyTrafficStyle();
        myTrafficStyle.setSeriousCongestedColor(0xff92000a);
        myTrafficStyle.setCongestedColor(0xffea0312);
        myTrafficStyle.setSlowColor(0xffff7508);
        myTrafficStyle.setSmoothColor(0xff00a209);
        map.setMyTrafficStyle(myTrafficStyle);


    }


    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {

        for (MapLocation mapLocation : mapLocationList) {
            map.addMarker(new MarkerOptions().position(new LatLng(mapLocation.getLatitude(), mapLocation
                    .getLongitude()))
                                             .title(mapLocation.getAreaName())
                                             .snippet("地址：" + mapLocation.getAreaAddress() + "\n邮编：" + mapLocation
                                                     .getZipCode())
                                             .draggable(false));
            latLngList.add(new LatLng(mapLocation.getLatitude(), mapLocation.getLongitude()));
        }
        map.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng clickLatLng = marker.getPosition();
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(clickLatLng, 13));
                marker.showInfoWindow();
                return true;
            }
        });

    }

    /**
     * 配置定位功能
     */
    private void setLocation() {
        MyLocationStyle myLocationStyle = new MyLocationStyle();//创建定位中心样式
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_location_marker));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
        myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
        map.setMyLocationStyle(myLocationStyle);// 自定义定位中心样式
        map.setLocationSource(this);//设置定位资源。如果不设置此定位资源则定位按钮不可点击。
        map.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示
        map.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        map.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);//设置定位类型

    }


    /**
     * 创建工具栏菜单
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    /**
     * 监听工具栏选项
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.satellite_View://切换到卫星地图
                if (mapType == AMap.MAP_TYPE_NORMAL) {
                    mapType = AMap.MAP_TYPE_SATELLITE;
                    map.setMapType(mapType);
                } else {
                    mapType = AMap.MAP_TYPE_NORMAL;
                    map.setMapType(AMap.MAP_TYPE_NORMAL);
                }
                break;
            case R.id.traffic_View://切换到交通路况图
                isShowTraffic = !isShowTraffic;
                map.setTrafficEnabled(isShowTraffic);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (locationChangedListener != null && amapLocation != null) {
            if (amapLocation.getErrorCode() == 0) {
                locationChangedListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                if (currentPosition == null) {
                    currentPosition = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    for (LatLng latLng : latLngList) {
                        builder.include(latLng);
                    }
                    LatLngBounds bounds = builder.build();

                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
                }
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Toast.makeText(MapActivity.this, errText, Toast.LENGTH_SHORT).show();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        locationChangedListener = listener;
        if (locationClient == null) {
            locationClient = new AMapLocationClient(this);
            locationClient.setLocationListener(this);//设置定位监听
            AMapLocationClientOption locationClientOption = new AMapLocationClientOption();
            //设置定位模式
            locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否强制刷新WIFI，默认为强制刷新
            locationClientOption.setWifiActiveScan(true);
            //设置是否允许模拟位置,默认为false，不允许模拟位置
            locationClientOption.setMockEnable(true);
            //设置定位间隔,单位毫秒,默认为2000ms
            locationClientOption.setInterval(3000);
            //设置定位参数
            locationClient.setLocationOption(locationClientOption);
            locationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        locationChangedListener = null;
        if (locationClient != null) {
            locationClient.stopLocation();
            locationClient.onDestroy();
        }
        locationClient = null;
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != locationClient) {
            locationClient.onDestroy();
        }
    }


}
