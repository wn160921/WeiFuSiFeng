package com.example.a25564.weifusifeng.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a25564.weifusifeng.R;

/**
 * Created by 25564 on 2017/7/16.
 */

public class MeFragment extends BaseFragment {
    private View view;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_me, container, false);
//        view.findViewById(R.id.tv).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//            }
//        });
        return view;
    }
}
