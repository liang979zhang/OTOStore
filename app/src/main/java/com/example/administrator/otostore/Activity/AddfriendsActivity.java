package com.example.administrator.otostore.Activity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Adapter.PhoneAdapter;
import com.example.administrator.otostore.Bean.PhoneBean;
import com.example.administrator.otostore.R;
import com.github.promeg.pinyinhelper.Pinyin;
import com.gjiazhe.wavesidebar.WaveSideBar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/6/10.
 */

public class AddfriendsActivity extends BaseActivity {
    private static final int PERSONMISSION = 1000001;
    @BindView(R.id.phonerecycler)
    RecyclerView phonerecycler;
    @BindView(R.id.side_bar)
    WaveSideBar sideBar;
    private Context context;
    private List<PhoneBean> phoneBeanList;

    @Override
    public int getContentViewResId() {
        context = this;
        return R.layout.activity_add_friends;
    }

    @Override
    public void initView() {
        setcenterTitle("添加好友");
    }

    @Override
    public void leftbarclick() {
        super.leftbarclick();
        finish();
    }

    @Override
    public void initData() {
//        requestPhotoPermiss();
        getContacts();
    }

    private void getContacts() {
        ContentResolver resolver = getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        Cursor cursor = resolver.query(uri, null, null, null, null);
        phoneBeanList = new ArrayList<>();
        while (cursor.moveToNext()) {
            PhoneBean phoneBean = new PhoneBean();
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String convert = Pinyin.toPinyin(name, ",");
            String substring = convert.substring(0, 1);
            if (substring.matches("[A-Z]")) {
                phoneBean.setShoufirstletter(substring);
            } else {
                phoneBean.setShoufirstletter("#");
            }

            phoneBean.setPhonename(name);
            phoneBean.setPhonenumber(phone);
            phoneBean.setPinyin(convert);
            phoneBeanList.add(phoneBean);
            LogUtils.d("AAAAAAAA:name" + name);
            LogUtils.d("AAAAAAAA:phone" + phone);
            LogUtils.d("AAAAAAAA:pinyin" + convert);
            LogUtils.d("AAAAAAAA:firstletter" + substring);
        }
        cursor.close();
        sortphone();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        phonerecycler.setLayoutManager(linearLayoutManager);
        PhoneAdapter phoneAdapter = new PhoneAdapter(R.layout.phoneitem, phoneBeanList);
        phonerecycler.setAdapter(phoneAdapter);

        sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i=0; i<phoneBeanList.size(); i++) {
                    if (phoneBeanList.get(i).getShoufirstletter().equals(index)) {
                        ((LinearLayoutManager) phonerecycler.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });
    }

    public void sortphone() {
        // 排序
        Collections.sort(phoneBeanList, new Comparator<PhoneBean>() {
            @Override
            public int compare(PhoneBean lhs, PhoneBean rhs) {
                if (lhs.getShoufirstletter().equals(rhs.getShoufirstletter())) {
                    return lhs.getPhonename().compareTo(rhs.getPhonename());
                } else {
                    if ("#".equals(lhs.getShoufirstletter())) {
                        return 1;
                    } else if ("#".equals(rhs.getShoufirstletter())) {
                        return -1;
                    }
                    return lhs.getShoufirstletter().compareTo(rhs.getShoufirstletter());
                }
            }
        });
    }

//    private void requestPhotoPermiss() {
//        PermissionGen.with(this)
//                .addRequestCode(PERSONMISSION)
//                .permissions(
//                        Manifest.permission.WRITE_CONTACTS,
//                        Manifest.permission.READ_CONTACTS,
//                        Manifest.permission.CAMERA)
//                .request();
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
//    }
//
//    @PermissionSuccess(requestCode = PERSONMISSION)
//    public void requestPhotoSuccess() {
//        //成功之后的处理
//        //.......
//        getContacts();
//    }
//
//    @PermissionFail(requestCode = PERSONMISSION)
//    public void requestPhotoFail() {
//        //失败之后的处理，我一般是跳到设置界面
////        AppUtil.goToSetting(mContext);
//        showToast("获取权限失败,重新获取");
//        requestPhotoPermiss();
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // TODO: add setContentView(...) invocation
//        ButterKnife.bind(this);
//    }
}
