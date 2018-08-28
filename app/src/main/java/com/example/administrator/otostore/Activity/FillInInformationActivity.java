package com.example.administrator.otostore.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.Utils.CommonUtils;
import com.example.administrator.otostore.Utils.UpdateGlideImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FillInInformationActivity extends BaseActivity {
    @BindView(R.id.login_man)
    RadioButton loginMan;
    @BindView(R.id.login_woman)
    RadioButton loginWoman;
    @BindView(R.id.login_gender)
    RadioGroup loginGender;
    @BindView(R.id.login_email)
    EditText loginEmail;
    @BindView(R.id.login_complete)
    Button loginComplete;
    @BindView(R.id.filinformation)
    CircularImageView filinformation;
    private String headImage;
    private int choice_gander = 0;
    private File file;
    private Bitmap bmp;
    private static final int IMAGE_PICKER = 300;
    @Override
    public int getContentViewResId() {
        return R.layout.activity_fill_in_information;
    }

    @Override
    public void initView() {
        loginGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.login_woman:
                        choice_gander = 1;
                        LogUtils.d("参数是" + choice_gander);
                        break;
                    case R.id.login_man:

                        choice_gander = 0;
                        LogUtils.d("参数是" + choice_gander);
                        break;
                    default:
                }


//                        RadioButton choise = (RadioButton) findViewById(id);
//                        choice_gander=choise.gett
            }
        });
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
    protected boolean isshowtitlebar() {
        return false;
    }

    @Override
    public void initData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWight();
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.login_gender, R.id.login_email, R.id.login_complete, R.id.filinformation})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.filinformation:
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, IMAGE_PICKER);
                break;
            case R.id.login_gender:

                break;
            case R.id.login_email:
                break;
            case R.id.login_complete:
                if (!CommonUtils.isEmail(loginEmail.getText().toString().trim())) {
                    showToast("请输入正确邮箱");
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);



                for (ImageItem datae : images) {
                    headImage = datae.path;
                    LogUtils.d("DDDDDDDDDDDDDDDDDDDDDD" + datae.path);
                    bmp = BitmapFactory.decodeFile(headImage);
                    file = new File(headImage);
                    LogUtils.d("AAAAAAAAAAAAAAAAAAAAAA" + file);

                    filinformation.setImageBitmap(bmp);


//                    updateheadimgae(headImage);
                }


            } else {
                Toast.makeText(this, "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
