package com.example.administrator.otostore.Adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.apkfuns.logutils.LogUtils;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.administrator.otostore.Bean.GetFriendsInvetoBean;
import com.example.administrator.otostore.Bean.ShopImageBean;
import com.example.administrator.otostore.R;
import com.example.administrator.otostore.RxJavaUtils.RetrofitHttpUtil;
import com.example.administrator.otostore.Utils.Constants;
import com.example.administrator.otostore.Utils.GsonUtil;
import com.example.administrator.otostore.Utils.SPUtils;
import com.mchsdk.paysdk.retrofitutils.rxjava.observable.SchedulerTransformer;
import com.mchsdk.paysdk.retrofitutils.rxjava.observer.BaseObserver;

import java.util.List;

/**
 * Created by Administrator on 2018/7/10.
 */

public class GetFriendsInvetorAdapter extends BaseQuickAdapter<GetFriendsInvetoBean, BaseViewHolder> {
    @Override
    protected void convert(final BaseViewHolder helper, GetFriendsInvetoBean item) {

        RetrofitHttpUtil.getApiService()
                .GetUserAttachList("", Long.valueOf(item.getFriendUserID()), 2, 10, 1)
                .compose(SchedulerTransformer.<String>transformer())
                .subscribe(new BaseObserver<String>() {
                    @Override
                    protected void onSuccess(String s) {
                        LogUtils.d("图片" + s);
                        if (!s.equals("[]")){
                            List<ShopImageBean> imageBean = GsonUtil.parseJsonArrayWithGson(s, ShopImageBean.class);
                            Glide.with(mContext).load(Constants.BASE_URL + imageBean.get(0).getAttachURL()).into((ImageView) helper.getView(R.id.customimage));

                        }}
                });
        helper.setText(R.id.name, item.getNickName());
        helper.setText(R.id.remark, item.getRemark());
        helper.addOnClickListener(R.id.tongyi);
    }

    public GetFriendsInvetorAdapter() {
        super(R.layout.item_getfriendsinveto);
    }
}
