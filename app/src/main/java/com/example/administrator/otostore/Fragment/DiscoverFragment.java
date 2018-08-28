package com.example.administrator.otostore.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.otostore.Activity.FriendsMomentActivity;
import com.example.administrator.otostore.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class DiscoverFragment extends Fragment {


    @BindView(R.id.dongtai)
    ImageView dongtai;
    Unbinder unbinder;
    @BindView(R.id.left_base_bar)
    ImageView leftBaseBar;
    @BindView(R.id.center_base_bar_title)
    TextView centerBaseBarTitle;
    @BindView(R.id.right_base_bar)
    TextView rightBaseBar;
    @BindView(R.id.basetitle_show_or_hide)
    LinearLayout basetitleShowOrHide;

    public DiscoverFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fridne)
    public void onViewClicked() {
        Intent intent=new Intent(getActivity(),FriendsMomentActivity.class);
        startActivity(intent);
    }
}
