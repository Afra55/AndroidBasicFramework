package com.magus.youxiclient.module.home;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.magus.youxiclient.R;
import com.magus.youxiclient.base.BaseFragment;

public class HomeFragment extends BaseFragment {

    public static HomeFragment newInstance(String param1, String param2) {
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, String message) {
        View vi = inflater.inflate(R.layout.fragment_home, container, false);
        TextView viewById = (TextView) vi.findViewById(R.id.text_view);
        viewById.setOnClickListener(this);
        return vi;
    }

    @Override
    protected void onFragmentSeleted(boolean isFirst) {

    }

    @Override
    protected void onFragmentUnSeleted() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_view:
                Toast.makeText(getActivity(), "home", Toast.LENGTH_SHORT).show();
                String s = null;
                s.length();
                break;
        }
    }
}
