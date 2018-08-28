package com.example.administrator.otostore;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.administrator.otostore.Activity.BottomBarActivity;
import com.example.administrator.otostore.Activity.LoginActivity;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mylhyl.acp.Acp;
import com.mylhyl.acp.AcpListener;
import com.mylhyl.acp.AcpOptions;

import java.util.List;

public class MainActivity extends Activity {
    private static final int PERSONMISSION = 101;
    private final int PERMISSION_REQUESTCODE = 1;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_main);
        setPerssion();
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            requestPhotoPermiss();
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUESTCODE);
//        } else {
//            if (SPUtils.getUserId(context) == 0) {
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            } else {
//                Intent intent = new Intent(MainActivity.this, BottomBarActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        }
    }

    private void setPerssion() {
        Acp.getInstance(this).request(new AcpOptions.Builder()
                        .setPermissions(
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
//                        //存储空间
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_SETTINGS
                                Manifest.permission.WRITE_SETTINGS,
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                /*以下为自定义提示语、按钮文字
                .setDeniedMessage()
                .setDeniedCloseBtn()
                .setDeniedSettingBtn()
                .setRationalMessage()
                .setRationalBtn()*/
                        .build(),
                new AcpListener() {
                    @Override
                    public void onGranted() {
                        if (SPUtils.getShopId(context) == 0) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(MainActivity.this, BottomBarActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }

                    @Override
                    public void onDenied(List<String> permissions) {
                        Toast.makeText(context,permissions.toString() + "权限拒绝",Toast.LENGTH_SHORT).show();
                        setPerssion();
                    }
                });
    }
//
//    private void requestPhotoPermiss() {
//        PermissionGen.with(this)
//                .addRequestCode(PERSONMISSION)
//                .permissions(
////                        Manifest.permission.WRITE_CONTACTS,
////                        Manifest.permission.READ_CONTACTS,
////                        Manifest.permission.GET_ACCOUNTS,
////                        Manifest.permission.READ_PHONE_STATE,
////                        //位置
////                        Manifest.permission.ACCESS_FINE_LOCATION,
////                        Manifest.permission.ACCESS_COARSE_LOCATION,
////                        Manifest.permission.ACCESS_FINE_LOCATION,
////                        //相机、麦克风
////                        Manifest.permission.RECORD_AUDIO,
////                        Manifest.permission.WAKE_LOCK,
//                        Manifest.permission.CAMERA,
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
////                        //存储空间
////                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
////                        Manifest.permission.WRITE_SETTINGS
//                        Manifest.permission.WRITE_SETTINGS,
//                        Manifest.permission.ACCESS_FINE_LOCATION,
//                        Manifest.permission.READ_PHONE_STATE,
//                        Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//                .request();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
//    }
//
//
//    @PermissionFail(requestCode = PERSONMISSION)
//    public void requestPhotoFail() {
//        LogUtils.d("BBBBBBBBBBBBBBBBBBBBB");
//        requestPhotoPermiss();
//    }
//
//    @PermissionSuccess(requestCode = PERSONMISSION)
//    public void requestSuccess() {
//        LogUtils.d("AAAAAAAAAAAAAAAA");
//        if (SPUtils.getShopId(context) == 0) {
//            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        } else {
//            Intent intent = new Intent(MainActivity.this, BottomBarActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }
}
