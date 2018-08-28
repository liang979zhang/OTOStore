package com.example.administrator.otostore.Activity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.ShopRegisterLastBean;
import com.example.administrator.otostore.Bean.VerifyIDNumBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.Constants;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.result.HttpResponseException;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Action;

public class EditRegisterActivity extends BaseActivity {
    @BindView(R.id.store_name)
    EditText storeName;
    @BindView(R.id.store_nickname)
    EditText storeNickname;
    @BindView(R.id.store_select)
    TextView storeSelect;
    @BindView(R.id.store_phone)
    EditText storePhone;
    @BindView(R.id.store_loca)
    TextView storeLoca;
    @BindView(R.id.store_drs)
    EditText storeDrs;
    @BindView(R.id.store_locb)
    TextView storeLocb;
    @BindView(R.id.store_faren)
    EditText storeFaren;
    @BindView(R.id.store_idcar)
    EditText storeIdcar;
    @BindView(R.id.store_reg_num)
    EditText storeRegNum;
    @BindView(R.id.store_reg_name)
    EditText storeRegName;
    @BindView(R.id.timeselect)
    TimePicker timeselect;
    @BindView(R.id.store_time_start)
    TextView storeTimeStart;
    @BindView(R.id.store_time_end)
    TextView storeTimeEnd;
    @BindView(R.id.store_remark)
    EditText storeRemark;
    @BindView(R.id.store_sumit)
    TextView storeSumit;


    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private String remark = "";
    private String starthour = "";
    private String startminute = "";
    private String endhour = "";
    private String endminute = "";
    private String starttime = "";
    private String endtime = "";
    private int leftnum;
    private int rightnum;
    private Context context;
    private String ProvinceCode;
    private String CityCode;
    private String DistrictCode;
    private BigDecimal Longitude;
    private BigDecimal Latitude;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_edit_register;
    }

    @Override
    public void initView() {
        setcenterTitle("店铺信息");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.SelectIndustryRequest && resultCode == Constants.SelectIndustryReSult) {
//            bundle.putInt("left", Integer.valueOf(industrySecondBeanList.get(position).getItemParCode()));
//            bundle.putInt("right", Integer.valueOf(industrySecondBeanList.get(position).getItemCode()));
//            bundle.putString("classtitle", industrySecondBeanList.get(position).getItemTitle());

            leftnum = data.getIntExtra("left", 0);
            rightnum = data.getIntExtra("right", 0);
            storeSelect.setText(data.getStringExtra("classtitle"));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mlocationClient.stopLocation();
    }

    @OnClick({R.id.store_select, R.id.store_loca, R.id.store_locb, R.id.store_time_start, R.id.store_time_end, R.id.store_sumit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.store_select:
//                startActivity(TypeSelectionActivity.class);
                startActivityForResult(new Intent(this, SelectHangyeActivity.class), Constants.SelectIndustryRequest);
                break;
            case R.id.store_loca:
                mlocationClient.startLocation();
                break;
            case R.id.store_locb:
                break;
            case R.id.store_time_start:
                new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay < 10) {
                            starthour = "0" + hourOfDay;
                        } else if (minute < 10) {
                            startminute = "0" + minute;
                        } else {
                            starthour = "" + hourOfDay;
                            startminute = "" + minute;
                        }
                        starttime = starthour + ":" + startminute + ":00";
                        LogUtils.d("时间是:" + starttime);
                        storeTimeStart.setText("\t" + String.valueOf(hourOfDay) + " 时 " + String.valueOf(minute) + "分");
                    }
                }, 10, 10, true).show();
                break;
            case R.id.store_time_end:
                new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        if (hourOfDay < 10) {
                            endhour = "0" + hourOfDay;
                        } else if (minute < 10) {
                            endminute = "0" + minute;
                        } else {
                            endhour = "" + hourOfDay;
                            endminute = "" + minute;
                        }
                        endtime = endhour + ":" + endminute + ":00";
                        LogUtils.d("时间是:" + endtime);
                        storeTimeEnd.setText("\t" + String.valueOf(hourOfDay) + " 时 " + String.valueOf(minute) + "分");
                    }
                }, 10, 10, true).show();
                break;
            case R.id.store_sumit:
                if (storeName.getText().toString().trim().isEmpty()) {
                    showToast("请输入店铺名称");
                } else if (storeNickname.getText().toString().trim().isEmpty()) {
                    showToast("请输入店铺昵称");
                } else if (storeSelect.getText().toString().trim().isEmpty()) {
                    showToast("请选择行业");
                } else if (storePhone.getText().toString().trim().isEmpty()) {
                    showToast("请填写电话号码");
                } else if (storeLoca.getText().toString().trim().isEmpty()) {
                    showToast("请定位地址:");
                } else if (storeDrs.getText().toString().trim().isEmpty()) {
                    showToast("填写详细地址");
                } else if (storeFaren.getText().toString().trim().isEmpty()) {
                    showToast("填写法人");
                } else if (storeIdcar.getText().toString().trim().isEmpty()) {
                    showToast("填写身份证号码");
                } else if (storeRegNum.getText().toString().trim().isEmpty()) {
                    showToast("填写营业执照");
                } else if (storeRegName.getText().toString().trim().isEmpty()) {
                    showToast("填写营业执照名称");
                } else if (storeTimeStart.getText().toString().trim().isEmpty()) {
                    showToast("填写开店时间");
                } else if (storeTimeEnd.getText().toString().trim().isEmpty()) {
                    showToast("填写关店时间");
                } else {
                    VerifyIDNum(storeIdcar.getText().toString().trim());

                    //shopfirst(storeName.getText().toString().trim(), leftnum, rightnum);
                }
                break;
        }
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    LogUtils.d(amapLocation.getProvince() + amapLocation.getCity() + amapLocation.getDistrict());
                    if (!amapLocation.getDistrict().isEmpty()) {
                        ProvinceCode = amapLocation.getAdCode().substring(0, 2) + "0000";
                        CityCode = amapLocation.getAdCode().substring(0, 4) + "00";
                        DistrictCode = amapLocation.getAdCode();
                        Longitude = BigDecimal.valueOf(amapLocation.getLongitude());
                        Latitude = BigDecimal.valueOf(amapLocation.getLatitude());
                        LogUtils.d(ProvinceCode + "  " + CityCode + " " + DistrictCode + "经纬度：" + Longitude + " " + Latitude);
                        storeLoca.setText(amapLocation.getProvince() + " " + amapLocation.getCity() + " " + amapLocation.getDistrict() + " " + amapLocation.getStreet());
                        mlocationClient.onDestroy();
                    }
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    LogUtils.d("Loc:" + amapLocation.getAddress() + "AOI" + amapLocation.getAoiName() + "Floor" + amapLocation.getFloor() + "POI" + amapLocation.getAoiName());
                    Date date = new Date(amapLocation.getTime());
                    df.format(date);//定位时间
                } else {
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };

    private void shoplatregister(String shopname, int left, int right, String NickName, String ContectTel,
                                 BigDecimal Longitude, BigDecimal Latitude, String ProvinceCode, String CityCode, String DistrictCode,
                                 String Addr, String LegalRepresentative, String LegalID, String RegistrationNo, String BusinessLicenceName,
                                 String OpeningTime, String ClosingTime, String Remark


    ) {
        RetrofitHttpUtil.getApiService()
                .EditShopInfo("", SPUtils.getShopId(context), shopname, left, right, NickName,
                        ContectTel, Longitude, Latitude, ProvinceCode, CityCode, DistrictCode, Addr, LegalRepresentative,
                        LegalID, RegistrationNo, BusinessLicenceName, OpeningTime, ClosingTime, Remark
                ).compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d(s);
                ShopRegisterLastBean lastBean = GsonUtil.parseJsonWithGson(s, ShopRegisterLastBean.class);
                if (lastBean.getResult().equals("1")) {
                    showAlertDialog("修改店铺成功", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            finish();
                        }
                    });
                }
            }
        });

    }

    private void initGaoMap() {
        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(locationListener);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initGaoMap();
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void VerifyIDNum(String s) {
        RetrofitHttpUtil.getApiService()
                .VerifyIDNum("", s).compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                VerifyIDNumBean verifyIDNumBean = GsonUtil.parseJsonWithGson(s, VerifyIDNumBean.class);
                if (verifyIDNumBean.getResult().equals("0")) {
                    if (storeRemark.getText().toString().trim().isEmpty()) {
                        remark = "";
                    } else {
                        remark = storeRemark.getText().toString().trim();
                    }
                    shoplatregister(storeName.getText().toString().trim(), leftnum, rightnum, storeNickname.getText().toString().trim(), storePhone.getText().toString().trim(), Longitude, Latitude,
                            ProvinceCode, CityCode, DistrictCode, storeDrs.getText().toString().trim(), storeFaren.getText().toString().trim(),
                            storeIdcar.getText().toString().trim(), storeRegNum.getText().toString().trim(),
                            storeRegName.getText().toString().trim(), starttime, endtime, remark);
                } else {
                    showAlertDialog(verifyIDNumBean.getMsg(), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            }

            @Override
            protected void onFailed(HttpResponseException responseException) {
                super.onFailed(responseException);
            }
        });
    }
}
