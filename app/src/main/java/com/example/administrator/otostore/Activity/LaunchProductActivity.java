package com.example.administrator.otostore.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.example.administrator.otostore.Bean.CategroyBean;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.Bean.ProductImageBean;
import com.example.administrator.otostore.Bean.ProductLaunchResultBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.CommonUtils;
import com.example.administrator.otostore.Utils.Constants;
import com.example.administrator.otostore.Utils.EventBusUtil;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.example.administrator.otostore.Utils.UpdateGlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.mchsdk.paysdk.retrofitutils.result.HttpResponseException;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.io.File;
import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Action;

public class LaunchProductActivity extends BaseActivity {
    @BindView(R.id.name)
    EditText name;
    @BindView(R.id.date)
    EditText date;
    @BindView(R.id.type)
    TextView type;
    @BindView(R.id.danwei)
    EditText danwei;
    @BindView(R.id.remark)
    EditText remark;
    @BindView(R.id.yes)
    RadioButton yes;
    @BindView(R.id.no)
    RadioButton no;
    @BindView(R.id.peisong)
    RadioGroup peisong;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.daiyes)
    RadioButton daiyes;
    @BindView(R.id.daino)
    RadioButton daino;
    @BindView(R.id.dai)
    RadioGroup daijinjuan;
    private Context context;
    private List<CategroyBean> list;
    private long selectid;
    private int dai = 0;
    private int pei = 0;
    private int productid = 0;
    private String ProductCode = "";
    private int typeproduct;
    private static final int IMAGE_PICKER = 300;
    private String headImage;
    private String imagename;
    private int select = 0;

    @Override
    public int getContentViewResId() {

        context = this;
        return R.layout.activity_launch_product;
    }

    private void initWight() {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new UpdateGlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(true);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(false); //是否按矩形区域保存
        imagePicker.setSelectLimit(1);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
        imagePicker.setMultiMode(false);   //允许剪切
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        typeproduct = Integer.valueOf(bundle.getString("producttype"));
        if (bundle.getString("producttype").equals("0")) {
            setcenterTitle("发布产品");
        } else if (bundle.getString("producttype").equals("1")) {
            productid = Integer.valueOf(bundle.getString("producttypeProductID"));
            showproductimage(productid);
            selectid = Long.valueOf(bundle.getString("producttypeCatID"));
            ProductCode = bundle.getString("producttypeProductCode");
            name.setText(bundle.getString("producttypeProductName"));
            date.setText(bundle.getString("producttypeValidDays"));
            danwei.setText(bundle.getString("producttypeUnit"));
            dai = bundle.getString("producttypeSupportVoucher").equals("False") ? 0 : 1;
            pei = bundle.getString("producttypeSupportDispatch").equals("False") ? 0 : 1;
            remark.setText(bundle.getString("producttypeRemark"));
            setcenterTitle("修改产品");
        }

        showright(true);
        setrightTitle("发布");

        peisong.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.daiyes:
                        dai = 1;
                        break;
                    case R.id.daino:
                        dai = 0;
                        break;
                }
            }
        });

        peisong.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.yes:
                        pei = 1;
                        break;
                    case R.id.no:
                        pei = 0;
                        break;
                }
            }
        });
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }


    @Override
    public void rightbarclick() {
        super.rightbarclick();
        LaunchProduct(
                productid,
                SPUtils.getShopId(context),
                selectid,
                ProductCode,
                name.getText().toString().trim(),
                Integer.valueOf(date.getText().toString().trim()),
                danwei.getText().toString().trim(),
                dai,
                pei,
                remark.getText().toString().trim()
        );
    }

    @Override
    public void initData() {
        showslect();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                select = 1;
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);


                for (ImageItem datae : images) {
                    headImage = datae.path;
                    imagename = headImage.substring(headImage.lastIndexOf("/") + 1);
                    image.setImageBitmap(BitmapFactory.decodeFile(headImage));
                    LogUtils.d("DDDDDDDDDDDDDDDDDDDDDD" + datae.path);

//                    bmp = BitmapFactory.decodeFile(headImage);
//                    file = new File(headImage);
//
//                    LogUtils.d("AAAAAAAAAAAAAAAAAAAAAA" + file);
//
//                    filinformation.setImageBitmap(bmp);


//                    updateheadimgae(headImage);
                }


            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void showproductimage(long ProductID) {
        RetrofitHttpUtil.getApiService()
                .GetProductAttachList("", ProductID, 3, 1, 1).compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d("图片是" + s);
                if (!s.equals("[]")) {
                    ProductImageBean imageBean = GsonUtil.parseJsonWithGson(s, ProductImageBean.class);
                    Glide.with(context).load(Constants.BASE_URL + imageBean.getAttachURL()).into(image);
                }
            }
        });
    }

    private void updateimage(String imagename, String file, long ownerId) {
        RetrofitHttpUtil.getApiService()
                .UploadFile("", SPUtils.getUserId(context), 4, ownerId, 3, imagename,true, file)
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d("图片" + s);
                        if (s.equals("1")) {
                            showToast("图片上传成功");
                        }
                    }

                    @Override
                    protected void onFailed(HttpResponseException responseException) {
                        super.onFailed(responseException);
                    }
                });
    }

    private void showslect() {
        RetrofitHttpUtil.getApiService()
                .GetShopCategory("", SPUtils.getShopId(context), "10000000")
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                list = GsonUtil.parseJsonArrayWithGson(s, CategroyBean.class);
                LogUtils.d(list.size() + "数值是");
            }
        });
    }

    private void LaunchProduct(
            long ProductID,
            long ShopID,
            long CatID,
            String ProductCode,
            String ProductName,
            int ValidDays,
            String Unit,
            int SupportVoucher,
            int SupportDispatch,
            String Remark
    ) {
        RetrofitHttpUtil.getApiService()
                .SaveShopProduct("",
                        ProductID,
                        ShopID,
                        CatID,
                        ProductCode,
                        ProductName,
                        ValidDays,
                        Unit,
                        SupportVoucher,
                        SupportDispatch,
                        Remark
                )
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                }).subscribe(new BaseObserver<String>() {
            @Override
            protected void onSuccess(String s) {
                LogUtils.d("产品" + s);
                ProductLaunchResultBean categroyBean = GsonUtil.parseJsonWithGson(s, ProductLaunchResultBean.class);
                if (categroyBean.getResult().equals("1")) {
                    if (select == 1) {
                        updateimage(imagename, CommonUtils.imageToBase64(headImage), Long.valueOf(categroyBean.getProductID()));
                    }
                    if (typeproduct == 0) {
                        showToast("添加产品成功");

                    } else if (typeproduct == 1) {
                        showToast("修改产品成功");
                    }

                    EventBusUtil.sendStickyEvent(new MessageEvent<>(MyEventCode.CODE_B, "producttype"));
//                    finish();
                    Bundle bundle = new Bundle();
                    bundle.putString("rulesproduct", categroyBean.getProductID());
                    startActivity(SpecificationManagementActivity.class, bundle);
                    finish();
                } else if (categroyBean.getResult().equals("2")) {
                    showToast("店铺内已存在同代码产品");
                } else if (categroyBean.getResult().equals("0")) {
                    showToast("店铺不存在");
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWight();
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.image, R.id.type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.image:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.type:
                dialogList();
                break;
        }
    }

    private void dialogList() {
        final String items[] = {"刘德华", "张柏芝", "蔡依林", "张学友"};
        final String itemA[] = new String[list.size()];
        List<String> itemlist = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            itemlist.add(list.get(i).getCatName());
            itemA[i] = list.get(i).getCatName();
            LogUtils.d("数值是：" + itemA[i]);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this, 3);
        builder.setTitle("选择种类");
        // builder.setMessage("是否确认退出?"); //设置内容
        builder.setIcon(R.mipmap.ic_launcher);
        // 设置列表显示，注意设置了列表显示就不要设置builder.setMessage()了，否则列表不起作用。
        builder.setItems(itemA, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                type.setText(list.get(which).getCatName());
                Toast.makeText(context, list.get(which).getCatName(),
                        Toast.LENGTH_SHORT).show();
                selectid = Long.valueOf(list.get(which).getCatID());

            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(context, "取消", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        builder.create().show();
    }

}
