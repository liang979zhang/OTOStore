package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.EventBusUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.functions.Action;

public class AddVarietyActivity extends BaseActivity {
    @BindView(R.id.catnum)
    EditText catnum;
    @BindView(R.id.catname)
    EditText catname;
    @BindView(R.id.catremark)
    EditText catremark;
    private Context context;
    private int CarID = 0;
    private String catecaid = "";
    private int type;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_add_variety;
    }

    @Override
    public void initView() {
        showright(true);
        setrightTitle("完成");
        Bundle bundle = new Bundle();
        bundle = this.getIntent().getExtras();
        catecaid = bundle.getString("catecaid");
        LogUtils.d("数值是:" + catecaid);
        if (bundle.getString("catetype").equals("0")) {
            setcenterTitle("添加分类");
            type = 0;
        } else if (bundle.getString("catetype").equals("1")) {
            setcenterTitle("修改分类");
            type = 1;
            catname.setText(bundle.getString("catename"));
            catremark.setText(bundle.getString("catedes"));
            CarID = Integer.valueOf(bundle.getString("catidx"));
        }
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void rightbarclick() {
        super.rightbarclick();
        if (editempty(catname)) {
            showToast("名称不能为空");
        } else if (editempty(catremark)) {
            showToast("填写描述");
        } else {
            addcategory(CarID, Integer.valueOf(catecaid), editText(catname), editText(catremark));
        }
//        showToast("1");
    }

    @Override
    public void initData() {

    }

    private void addcategory(long CatID, int CatIdx, String CatName, String remark) {
        LogUtils.d("数值是:" + CatID);
        RetrofitHttpUtil.getApiService()
                .SaveShopCategory("", CatID, SPUtils.getShopId(context), CatName, CatIdx, remark, "10000000 ")
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                CategroyBean categroyBean = GsonUtil.parseJsonWithGson(s, CategroyBean.class);
                if (categroyBean.getResult().equals("1")) {
                    EventBusUtil.sendStickyEvent(new MessageEvent<>(MyEventCode.CODE_B, "category"));
                    if (type == 0) {
                        showToast("新增成功");
                        finish();
                    } else if (type == 1) {
                        showToast("修改成功");
                        finish();
                    }
                } else if (categroyBean.getResult().equals("2")) {
                    showAlertDialog("种类重复，重新修改", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                }

                LogUtils.d(s);
            }
        });
    }

    private String editText(EditText text) {
        return text.getText().toString().trim();
    }

    private boolean editempty(EditText text) {
        return text.getText().toString().trim().isEmpty();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
