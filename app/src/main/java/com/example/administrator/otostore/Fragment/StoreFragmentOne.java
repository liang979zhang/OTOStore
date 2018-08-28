package com.example.administrator.otostore.Fragment;


import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.adorkable.iosdialog.ActionSheetDialog;
import com.apkfuns.logutils.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.administrator.otostore.Adapter.AddressAdapter;
import com.example.administrator.otostore.Bean.AddressBean;
import com.example.administrator.otostore.Bean.UserRegisterResultBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.mchsdk.paysdk.retrofitutils.result.HttpResponseException;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import org.feezu.liuli.timeselector.TimeSelector;

import java.math.BigDecimal;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.functions.Action;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoreFragmentOne extends Fragment {


    @BindView(R.id.sales_return)
    TextView salesReturn;
    @BindView(R.id.stop_doing_business)
    TextView stopDoingBusiness;
    Unbinder unbinder;
    CityPickerView mPicker = new CityPickerView();
    @BindView(R.id.peisong)
    EditText peisong;
    @BindView(R.id.mianpeisong)
    EditText mianpeisong;
    @BindView(R.id.baozhuang)
    EditText baozhuang;
    @BindView(R.id.mianbaozhuang)
    EditText mianbaozhuang;
    @BindView(R.id.zhekou)
    EditText zhekou;
    @BindView(R.id.yingyemode)
    TextView yingyemode;
    @BindView(R.id.starttime)
    TextView starttime;
    @BindView(R.id.endtime)
    TextView endtime;
    @BindView(R.id.orderdate)
    EditText orderdate;
    @BindView(R.id.selectaddress)
    TextView selectaddress;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.click)
    Button click;
    private Context context;
    private String starttimeselect = "";
    private String endtimeselect = "";
    private String addresId = "";
    private PopupWindow popupWindow;
    private int yingyemodel = 1;

    public StoreFragmentOne() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        context = getActivity();
        mPicker.init(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_store_fragment_one, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.sales_return, R.id.stop_doing_business, R.id.yingyemode, R.id.starttime, R.id.endtime, R.id.selectaddress, R.id.click})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sales_return:
                CityConfig cityConfig = new CityConfig.Builder().build();
                mPicker.setConfig(cityConfig);
                mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
                    @Override
                    public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                        super.onSelected(province, city, district);
                        LogUtils.d("A" + province.getName() + city.getName() + district.getName() + province.getId() + " " + city.getId() + " " + district.getId());
                        stopDoingBusiness.setText(province.getName() + city.getName() + district.getName());
                    }

                    @Override
                    public void onCancel() {
                        super.onCancel();
                    }
                });
                mPicker.showCityPicker();
                break;
            case R.id.stop_doing_business:
                break;
            case R.id.yingyemode:
                new ActionSheetDialog(getActivity())
                        .builder()
                        .setTitle("模式选择")
                        .setCancelable(false)
                        .setCanceledOnTouchOutside(false)
                        .addSheetItem("持续营业", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                yingyemode.setText("持续营业");
                                yingyemodel = 1;
                            }
                        })
                        .addSheetItem("阶段营业", ActionSheetDialog.SheetItemColor.Blue, new ActionSheetDialog.OnSheetItemClickListener() {
                            @Override
                            public void onClick(int which) {
                                yingyemode.setText("阶段营业");
                                yingyemodel = 2;
                            }
                        })
                        .show();
                break;
            case R.id.starttime:
                TimeSelector timeSelector = new TimeSelector(getActivity(), new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        LogUtils.d("" + time);
                        starttimeselect = time;
                        starttime.setText(time);
                    }
                }, "2018-6-30 10:10", "2035-6-30 10:10");

                timeSelector.show();
                break;
            case R.id.endtime:
                TimeSelector timeSelectora = new TimeSelector(getActivity(), new TimeSelector.ResultHandler() {
                    @Override
                    public void handle(String time) {
                        endtime.setText(time);
                        endtimeselect = time;
                        LogUtils.d("" + time);
                    }
                }, "2018-6-30 10:10", "2035-6-30 10:10");
                timeSelectora.show();
                break;
            case R.id.selectaddress:
                showselectaddress();
                break;
            case R.id.click:
//                getshopstate();
//                shopsubmit();
                if (getStringEmpty(peisong)) {
                    showToast("输入配送费");
                } else if (getStringEmpty(mianpeisong)) {
                    showToast("输入满减配送费");
                } else if (getStringEmpty(baozhuang)) {
                    showToast("输入包装费");
                } else if (getStringEmpty(mianbaozhuang)) {
                    showToast("输入满减包装费");
                } else if (getStringEmpty(zhekou)) {
                    showToast("输入折扣");
                } else if (getStringEmpty(yingyemode)) {
                    showToast("选择营业模式");
                } else if (getStringEmpty(starttime)) {
                    showToast("选择起始时间");
                } else if (getStringEmpty(endtime)) {
                    showToast("选择结束时间");
                } else if (getStringEmpty(orderdate)) {
                    showToast("填写下单有效天数");
                } else if (getStringEmpty(selectaddress)) {
                    showToast("选择退货地址");
                } else if (getStringEmpty(remark)) {
                    showToast("填写描述");
                } else {
                    editshopsetting(1,
                            getdoublefee(peisong),
                            getdoublefee(mianpeisong),
                            getdoublefee(baozhuang),
                            getdoublefee(mianbaozhuang),
                            getdoublefee(zhekou),

                            yingyemodel,

                            starttime.getText().toString().trim(),
                            endtime.getText().toString().trim(),
                            Long.valueOf(addresId),
                            Integer.valueOf(orderdate.getText().toString().trim()),
                            remark.getText().toString().trim()

                    );
                }
                break;
            default:
                break;
        }
    }

    private void editshopsetting(int OrderType,
                                 String DeliverFee,
                                 String DeliveryNoChargeAmt,
                                 String PackingFee,
                                 String PackingNoChargeAmt,
                                 String Discount,
                                 int ClosingMode,
                                 String ClosingStartDate,
                                 String ClosingEndDate,
                                 Long ReturnAddrID,
                                 int ValidDays,
                                 String Remark
    ) {
        BigDecimal a = new BigDecimal(DeliverFee);
        BigDecimal b = new BigDecimal(DeliveryNoChargeAmt);
        BigDecimal c = new BigDecimal(PackingFee);
        BigDecimal d = new BigDecimal(PackingNoChargeAmt);
        BigDecimal e = new BigDecimal(Discount);
        RetrofitHttpUtil.getApiService()
                .SetShopSettings("", SPUtils.getShopId(getActivity()), OrderType, a, b, c, d, e,
                        ClosingMode, ClosingStartDate, ClosingEndDate, ReturnAddrID, ValidDays, Remark).compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                UserRegisterResultBean bean = GsonUtil.parseJsonWithGson(s, UserRegisterResultBean.class);
                if (bean.getResult().equals("0")) {
                    showToast("店铺不存在");
                } else if (bean.getResult().equals("1")) {
                    showToast("设置成功");
                    getActivity().finish();
                }
            }

            @Override
            protected void onFailed(HttpResponseException responseException) {
                super.onFailed(responseException);
            }
        });
    }

    private void showselectaddress() {
        RetrofitHttpUtil.getApiService()
                .GetUserAddress("", SPUtils.getUserId(getActivity()))
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d("数据是：" + s);
                View view = getActivity().getLayoutInflater().inflate(R.layout.popwindowrecycler, null);
                RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_select);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(linearLayoutManager);
                final List<AddressBean> list = GsonUtil.parseJsonArrayWithGson(s, AddressBean.class);
                AddressAdapter addressAdapter = new AddressAdapter(R.layout.address_item, list);
                addressAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                        selectaddress.setText(list.get(position).getProvinceName() + " " + list.get(position).getCityName() + " " + list.get(position).getDistrictName() + " " + list.get(position).getAddr());
                        addresId = list.get(position).getAddrID();
                        popupWindow.dismiss();
                        LogUtils.d("地址是:" + addresId);
                    }
                });
                recyclerView.setAdapter(addressAdapter);

                popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                darkenBackground(0.2f);
                popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

                    @Override
                    public void onDismiss() {
                        darkenBackground(1f);
                    }
                });
//                popupWindow.showAsDropDown(mianpeisong, 0, 20);

            }

            @Override
            protected void onFailed(HttpResponseException responseException) {
                super.onFailed(responseException);
            }
        });
    }

    private void darkenBackground(Float bgcolor) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgcolor;

        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getActivity().getWindow().setAttributes(lp);

    }

    private boolean getStringEmpty(EditText textView) {
        return textView.getText().toString().trim().isEmpty();
    }

    private void showToast(String show) {
        Toast.makeText(getActivity(), show, Toast.LENGTH_SHORT).show();
    }

    private boolean getStringEmpty(TextView textView) {
        return textView.getText().toString().trim().isEmpty();
    }

    private String getdoublefee(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void getshopstate() {
        RetrofitHttpUtil.getApiService()
                .GetShopSettings("", SPUtils.getShopId(getActivity()))
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d("测试是" + s);
            }
        });
    }

    private void shopsubmit() {
        RetrofitHttpUtil.getApiService()
                .ShopSubmit("", SPUtils.getUserId(getActivity()), SPUtils.getShopId(getActivity()))
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d("测试是" + s);
            }
        });
    }
}
