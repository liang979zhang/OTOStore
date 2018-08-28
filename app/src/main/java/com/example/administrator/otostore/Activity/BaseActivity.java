package com.example.administrator.otostore.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adorkable.iosdialog.AlertDialog;
import com.apkfuns.logutils.LogUtils;
import com.example.administrator.otostore.Bean.MessageEvent;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.Utils.ActivityUtils;
import com.example.administrator.otostore.Utils.EventBusUtil;
import com.example.administrator.otostore.Utils.ProgressDialogUtils;
import com.github.nukc.stateview.StateView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends Activity implements StateView.OnRetryClickListener, View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();
    //    @BindView(R.id.left_base_bar)
    private ImageView leftBaseBar;
    //    @BindView(R.id.center_base_bar_title)
    private TextView centerBaseBarTitle;
    //    @BindView(R.id.right_base_bar)
    private TextView rightBaseBar;

    private View view;
    private Unbinder mUnbinder;
    private ProgressDialogUtils progressDialog;
    private StateView networkStateView;
    private AlertDialog mAlertDialog;
    private static Toast toast;
//    private boolean lefttopbaroverload = false;
//    private Boolean showtitle = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());
        LogUtils.d(TAG, "onCreate()");
        mUnbinder = ButterKnife.bind(this);
        ActivityUtils.addActivity(this);
        initDialog();
        initView();
        initData();
        if (isRegisterEventBus()) {
            LogUtils.d("EBBaseActivity", "register");
            EventBusUtil.register(this);
        }
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        view = getLayoutInflater().inflate(R.layout.activity_base, null);
        LinearLayout linearLayoutbarshoworhide = view.findViewById(R.id.basetitle_show_or_hide);

        leftBaseBar = view.findViewById(R.id.left_base_bar);
        centerBaseBarTitle = view.findViewById(R.id.center_base_bar_title);
        rightBaseBar = view.findViewById(R.id.right_base_bar);

        if (!isshowtitlebar()) {
            linearLayoutbarshoworhide.setVisibility(View.GONE);
        }
        leftBaseBar.setOnClickListener(this);
        rightBaseBar.setOnClickListener(this);

        //设置填充activity_base布局
        super.setContentView(view);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            view.setFitsSystemWindows(true);
        }

        //加载子类Activity的布局
        initDefaultView(layoutResID);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_base_bar:
                rightbarclick();
                break;
            case R.id.left_base_bar:
                leftbarclick();
                break;
            default:
        }
    }

    public void leftbarclick() {

    }

    public void rightbarclick() {

    }

    protected boolean isshowtitlebar() {
        return true;
    }

    public void setcenterTitle(String title) {
        centerBaseBarTitle.setText(title);
    }

    public void setrightTitle(String title) {
        rightBaseBar.setText(title);
    }

    public void showright(boolean showrightbar) {
        if (showrightbar == true) {
            rightBaseBar.setVisibility(View.VISIBLE);
        } else {
            rightBaseBar.setVisibility(View.GONE);
        }
    }

    /*
      contenview 显示
    * */
    protected void showAlertDialog(@Nullable String message, @Nullable View.OnClickListener onPositiveButtonClickListener) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("万商");
//        builder.setMessage(message);
//        builder.setNeutralButton("确定", onPositiveButtonClickListener);
//        this.mAlertDialog = builder.show();

        new AlertDialog(this).builder().setTitle("万商商家版").setMsg(message).setPositiveButton("确定", onPositiveButtonClickListener).setCancelable(false).show();

    }

    public void showLoadingView() {
        networkStateView.showLoading();
    }

    public void showErrorView() {
        networkStateView.showRetry();
        networkStateView.setOnRetryClickListener(this);
    }

    public void showEmptyView() {
        networkStateView.showEmpty();
        networkStateView.setOnRetryClickListener(this);
    }

    public void showContentView() {
        networkStateView.showContent();
    }

    /*
    * 再次点击的显示
    * */
    @Override
    public void onRetryClick() {
        onNetworkViewRefresh();
    }

    public void onNetworkViewRefresh() {
    }

    public void showProgressDialog() {
        progressDialog.showProgressDialog();
    }

    /**
     * 显示有加载文字ProgressDialog，文字显示在ProgressDialog的下面
     *
     * @param text 需要显示的文字
     */
    public void showProgressDialogWithText(String text) {
        progressDialog.showProgressDialogWithText(text);
    }

    /**
     * 显示加载成功的ProgressDialog，文字显示在ProgressDialog的下面
     *
     * @param message 加载成功需要显示的文字
     * @param time    需要显示的时间长度(以毫秒为单位)
     */
    public void showProgressSuccess(String message, long time) {
        progressDialog.showProgressSuccess(message, time);
    }

    /**
     * 显示加载成功的ProgressDialog，文字显示在ProgressDialog的下面
     * ProgressDialog默认消失时间为1秒(1000毫秒)
     *
     * @param message 加载成功需要显示的文字
     */
    public void showProgressSuccess(String message) {
        progressDialog.showProgressSuccess(message);
    }

    /**
     * 显示加载失败的ProgressDialog，文字显示在ProgressDialog的下面
     *
     * @param message 加载失败需要显示的文字
     * @param time    需要显示的时间长度(以毫秒为单位)
     */
    public void showProgressFail(String message, long time) {
        progressDialog.showProgressFail(message, time);
    }

    /**
     * 显示加载失败的ProgressDialog，文字显示在ProgressDialog的下面
     * ProgressDialog默认消失时间为1秒(1000毫秒)
     *
     * @param message 加载成功需要显示的文字
     */
    public void showProgressFail(String message) {
        progressDialog.showProgressFail(message);
    }

    private void initDefaultView(int layoutResID) {
        networkStateView = (StateView) findViewById(R.id.stateview);
        FrameLayout container = (FrameLayout) findViewById(R.id.fl_activity_child_container);
        View childView = LayoutInflater.from(this).inflate(layoutResID, null);
        container.addView(childView, 0);
    }

    private void initDialog() {
        progressDialog = new ProgressDialogUtils(this, R.style.dialog_no_background);
    }

    public abstract int getContentViewResId();


    public abstract void initView();

    /**
     * 设置数据
     */
    public abstract void initData();


    /**
     * [页面跳转]
     *
     * @param clz
     */
    public void startActivity(Class<?> clz) {
        startActivity(new Intent(BaseActivity.this, clz));
    }

    /**
     * [携带数据的页面跳转]
     *
     * @param clz
     * @param bundle
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param cls
     * @param bundle
     * @param requestCode
     */
    public void startActivityForResult(Class<?> cls, Bundle bundle,
                                       int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);

    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtils.d(TAG, "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtils.d(TAG, "onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        LogUtils.d(TAG, "onRestart");
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (this.mAlertDialog != null && this.mAlertDialog.isShowing()) {
//            this.mAlertDialog.dismiss();
//        }
        LogUtils.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        if (isRegisterEventBus()) {
            LogUtils.d("EBBaseActivity", "unregister");
            EventBusUtil.unregister(this);
        }
        mUnbinder.unbind();
        LogUtils.d(TAG, "onDestroy()");
        ActivityUtils.removeActivity(this);
    }

    protected void exitall() {
        ActivityUtils.removeAllActivity();
    }

    /*
                     快速点击
    * */
//    private boolean fastClick() {
//        long lastClick = 0;
//        if (System.currentTimeMillis() - lastClick <= 1000) {
//            return false;
//        }
//        lastClick = System.currentTimeMillis();
//        return true;
//    }

    protected boolean isRegisterEventBus() {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventBusCome(MessageEvent event) {
        if (event != null) {
            receiveEvent(event);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onStickyEventBusCome(MessageEvent event) {
        if (event != null) {
            receiveStickyEvent(event);
        }
    }

    /**
     * 接收到分发到事件
     *
     * @param event 事件
     */
    protected void receiveEvent(MessageEvent event) {

    }

    /**
     * 接受到分发的粘性事件
     *
     * @param event 粘性事件
     */
    protected void receiveStickyEvent(MessageEvent event) {

    }

    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

//    public void toastLong(String msg) {
//        if (null == toast) {
//            toast = new Toast(this);
//            toast.setDuration(Toast.LENGTH_LONG);
//            toast.setText(msg);
//            toast.show();
//        } else {
//            toast.setText(msg);
//            toast.show();
//        }
//    }

    /**
     * 显示短toast
     *
     * @param msg
     */
//    public void toastShort(String msg) {
//        if (null == toast) {
//            toast = new Toast(this);
//            toast.setDuration(Toast.LENGTH_SHORT);
//            toast.setText(msg);
//            toast.show();
//        } else {
//            toast.setText(msg);
//            toast.show();
//        }
//    }

}
