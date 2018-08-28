package com.example.administrator.otostore.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.EventBusUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Action;

public class EditSpecificationManagementActivity extends BaseActivity {

    @BindView(R.id.SpecIdx)
    EditText SpecIdx;
    @BindView(R.id.SpecCode)
    EditText SpecCode;
    @BindView(R.id.ProductSpec)
    EditText ProductSpec;
    @BindView(R.id.Price)
    EditText Price;
    @BindView(R.id.Quantity)
    EditText Quantity;
    @BindView(R.id.Remark)
    EditText Remark;
    @BindView(R.id.showproduct)
    LinearLayout showproduct;
    private String productid;
    private Long ProductSpecID = Long.valueOf("0");
    private int SpecIdxa;
    private String SpecCodea;
    private String ProductSpeca;
    private BigDecimal Pricea;
    private int Quantitya;
    private String Remarka;
    private int type;

    @Override
    public int getContentViewResId() {
        return R.layout.activity_edit_specification_management;
    }

    @Override
    public void initView() {
        showright(true);
        setrightTitle("确定");
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("specificationType").equals("0")) {
            type = 0;
            setcenterTitle("新增规格");
        } else if (bundle.getString("specificationType").equals("1")) {
            type = 1;
            setcenterTitle("修改规格");
            showproduct.setVisibility(View.GONE);

            ProductSpecID = Long.valueOf(bundle.getString("specificationProductSpecID"));

            SpecIdxa = Integer.valueOf(bundle.getString("specificationTypeSpecIdx"));
            SpecIdx.setText(bundle.getString("specificationTypeSpecIdx"));

            SpecCodea = bundle.getString("specificationTypeSpecCode");
            SpecCode.setText(bundle.getString("specificationTypeSpecCode"));

            ProductSpeca = bundle.getString("specificationTypeProductSpec");

            Pricea = BigDecimal.valueOf(Double.valueOf(bundle.getString("specificationTypePrice")));
            Price.setText(bundle.getString("specificationTypePrice"));

            Quantitya = Integer.valueOf(bundle.getString("specificationTypeQuantity"));
            Quantity.setText(bundle.getString("specificationTypeQuantity"));

            Remarka = bundle.getString("specificationTypeRemark");
            Remark.setText(bundle.getString("specificationTypeRemark"));
        }
    }

    @Override
    public void leftbarclick() {
        finish();
    }

    @Override
    public void rightbarclick() {

        super.rightbarclick();
        SpecIdxa = Integer.valueOf(editreturn(SpecIdx));
        SpecCodea = editreturn(SpecCode);
        if (type == 0) {
            ProductSpeca = editreturn(ProductSpec);
        } else if (type == 1) {
        }
        Pricea = BigDecimal.valueOf(Double.valueOf(editreturn(Price)));
        Quantitya = Integer.valueOf(editreturn(Quantity));
        Remarka = editreturn(Remark);
        editspecificationman(ProductSpecID, SpecIdxa, SpecCodea, ProductSpeca, Pricea, Quantitya, Remarka);
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void editspecificationman(
            long ProductSpecID,
            int SpecIdx,
            String SpecCode,
            String ProductSpec,
            BigDecimal Price,
            int Quantity,
            String Remark
    ) {
        Bundle bundle = getIntent().getExtras();
        productid = bundle.getString("saveproductrule");
        RetrofitHttpUtil.getApiService()
                .SaveProductSpec("", ProductSpecID, Long.valueOf(productid),
                        SpecIdx, SpecCode, ProductSpec, Price, Quantity, Remark
                ).compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d("实质是" + s);
                CategroyBean categroyBean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                if (categroyBean.getResult().equals("1")) {
                    showToast("保存成功");
                    EventBusUtil.sendStickyEvent(new MessageEvent(MyEventCode.CODE_B, "ProductSpecifi"));
                    finish();
                } else if (categroyBean.getResult().equals("0")) {
                    showAlertDialog("产品不存在", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else if (categroyBean.getResult().equals("2")) {
                    showAlertDialog("产品规格代码或名称已存在", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else if (categroyBean.getResult().equals("3")) {
                    showAlertDialog("产品规格数量小于已销售数量", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }
            }
        });
    }

    private String editreturn(EditText editText) {
        return editText.getText().toString().trim();
    }

    private boolean editempty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }
}
