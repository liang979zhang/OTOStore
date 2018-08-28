package com.example.administrator.otostore.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.Bean.MyEventCode;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.CommonUtils;
import com.example.administrator.otostore.Utils.EventBusUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.example.administrator.otostore.Utils.UpdateGlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.mchsdk.paysdk.retrofitutils.result.HttpResponseException;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.functions.Action;

public class MomentImageLaunchActivity extends BaseActivity {
    @BindView(R.id.left)
    ImageView left;
    @BindView(R.id.imageshow)
    ImageView imageshow;
    @BindView(R.id.selectimage)
    TextView selectimage;
    @BindView(R.id.momentfabu)
    TextView momentfabu;
    private String headImage;
    private Long Momentid;
    private Context context;
    private static final int IMAGE_PICKER = 300;
    private String imagename = "";

    @Override
    public int getContentViewResId() {
        context = this;
        Bundle bundle = getIntent().getExtras();
        Momentid = Long.valueOf(bundle.getString("momentid"));
        return R.layout.activity_moment_image_launch;
    }

    @Override
    public void initView() {
        setcenterTitle("发表动态图片");
    }

    @Override
    protected boolean isshowtitlebar() {
        return false;
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }


    @Override
    public void initData() {

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWight();
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);


                for (ImageItem datae : images) {
                    headImage = datae.path;
                    imagename = headImage.substring(headImage.lastIndexOf("/") + 1);
//                    updateimaegmoment(imagename, CommonUtils.imageToBase64(headImage));

                    Bitmap bitmap = BitmapFactory.decodeFile(headImage);
                    imageshow.setImageBitmap(bitmap);
                    LogUtils.d("DDDDDDDDDDDDDDDDDDDDDD" + headImage);
                }


            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateimaegmoment(Long momentid, String imagename, String file) {
        RetrofitHttpUtil.getApiService()
                .UploadFile("", SPUtils.getUserId(context), 8, momentid, 9, imagename,true, file)
                .compose(SchedulerTransformer.<String>transformer())
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {

                    }
                })
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        if (s.contains("1")) {
                            EventBusUtil.sendStickyEvent(new MessageEvent(MyEventCode.CODE_B,"MomentPublish"));
                            showToast("上传图片成功");
                            finish();
                        }
                        LogUtils.d("图片" + s);
                    }

                    @Override
                    protected void onFailed(HttpResponseException responseException) {
                        super.onFailed(responseException);
                    }
                });
    }

    @OnClick({R.id.momentfabu, R.id.selectimage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.momentfabu:
                if (imagename.equals("")) {
                    showToast("请选择图片");
                } else {
                    updateimaegmoment(Momentid, imagename, CommonUtils.imageToBase64(headImage));
                }
                break;
            case R.id.selectimage:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.left:
                finish();
                break;
        }
    }
}
