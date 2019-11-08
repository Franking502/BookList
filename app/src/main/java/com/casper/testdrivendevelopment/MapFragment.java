package com.casper.testdrivendevelopment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment {

private MapView mapView=null;
    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_map, container, false);
        mapView=(MapView)view.findViewById(R.id.bmapView);
       BaiduMap baiduMap = mapView.getMap();
        //设定中心点坐标
        LatLng centerPoint =  new LatLng(22.2559,113.541112);
        //定义地图状态
        MapStatus mMapStatus = new MapStatus.Builder()
                //要移动的点
                .target(centerPoint)
                //放大地图到20倍
                .zoom(17)
                .build();

        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

        //改变地图状态
        baiduMap.setMapStatus(mMapStatusUpdate);

        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.down_round);
//准备 marker option 添加 marker 使用
        MarkerOptions markerOptions = new MarkerOptions().icon(bitmap).position(centerPoint);
//获取添加的 marker 这样便于后续的操作
       Marker marker = (Marker) baiduMap.addOverlay(markerOptions);
        OverlayOptions textOption=new TextOptions().bgColor(0xAAFFFF00).fontSize(50).fontColor(0xFFFF00FF).text("暨南大学珠海校区").rotate(0).position(centerPoint);
baiduMap.addOverlay(textOption);
        baiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(getContext(), "Marker被点击了！", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        final ShopLoader shopLoader=new ShopLoader();
        Handler handler=new Handler(){
            public void handleMessage(Message msg){
                drawshops(shopLoader.getShops());
            }

        };
        shopLoader.load(handler,"http://file.nidama.net/class/mobile_develop/data/bookstore.json");//加网址
        return view;
    }

void drawshops(ArrayList<Shop>shops) {
            if(mapView==null)return ;
            BaiduMap mbaidumap=mapView.getMap();
    for(int i=0;i<shops.size();++i)
    {
            Shop shop=shops.get(i);
            LatLng point=new LatLng(shop.getLatitude(),shop.getLongitude());
            BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.down_round);
//准备 marker option 添加 marker 使用
            MarkerOptions markerOptions = new MarkerOptions().icon(bitmap).position(point);
//获取添加的 marker 这样便于后续的操作
            Marker marker = (Marker) mbaidumap.addOverlay(markerOptions);
            OverlayOptions textOption=new TextOptions().bgColor(0xAAFFFF00).fontSize(50).fontColor(0xFFFF00FF).text(shop.getName()).rotate(0).position(point);
            mbaidumap.addOverlay(textOption);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        if(mapView!=null)
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        if(mapView!=null)
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if(mapView!=null)
        mapView.onDestroy();
    }
}
