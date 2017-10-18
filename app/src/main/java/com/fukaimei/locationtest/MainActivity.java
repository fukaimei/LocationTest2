package com.fukaimei.locationtest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private static final int WRITE_COARSE_LOCATION_REQUEST_CODE = 0;
    // 定义LocationManager对象
    LocationManager locManager;
    // 定义程序界面中的EditText组件
    EditText show;
    Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取程序界面上的EditText组件
        show = (EditText) findViewById(R.id.show);
        // 创建LocationManager对象
        locManager = (LocationManager) getSystemService
                (Context.LOCATION_SERVICE);
//        SDK在Android 6.0以上的版本需要进行运行检测的动态权限如下：
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.READ_EXTERNAL_STORAGE,
//                Manifest.permission.READ_PHONE_STATE
//
//        这里以ACCESS_FINE_LOCATION与ACCESS_COARSE_LOCATION为例
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
        }
        // 从GPS获取最近的最近的定位信息
        Location location = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        // 使用location来更新EditText的显示
        updateView(location);
        // 设置每3秒获取一次GPS的定位信息
        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 8,
                new LocationListener() {  // ①
                    @Override
                    public void onLocationChanged(Location location) {
                        // 当GPS定位信息发生改变时，更新位置
                        updateView(location);
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                        updateView(null);
                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                        // 当GPS LocationProvider可用时，更新位置
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            //申请WRITE_EXTERNAL_STORAGE权限
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
                        } else if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                            //申请WRITE_EXTERNAL_STORAGE权限
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    WRITE_COARSE_LOCATION_REQUEST_CODE);//自定义的code
                        }
                        updateView(locManager.getLastKnownLocation(provider));
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }
                });
    }

    // 更新EditText中显示的内容
    public void updateView(Location newLocation) {
        if (newLocation != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("实时的位置信息：\n");
            sb.append("经度：");
            sb.append(newLocation.getLongitude());
            sb.append("\n纬度：");
            sb.append(newLocation.getLatitude());
            sb.append("\n高度：");
            sb.append(newLocation.getAltitude());
            sb.append("\n速度：");
            sb.append(newLocation.getSpeed());
            sb.append("\n方向：");
            sb.append(newLocation.getBearing());
            show.setText(sb.toString());
        } else {
            // 如果传入的Location对象为空则清空EditText
            show.setText("");
        }
    }
}
