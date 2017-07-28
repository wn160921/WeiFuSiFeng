package com.example.a25564.weifusifeng.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by 25564 on 2017/7/16.
 */

public class BaseFragment extends Fragment {
    protected Activity mActivity;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mActivity=getActivity();
    }

    @Override
    public void startActivity(Intent intent){
        super.startActivity(intent);
        getActivity().startActivity(intent);
    }
}
