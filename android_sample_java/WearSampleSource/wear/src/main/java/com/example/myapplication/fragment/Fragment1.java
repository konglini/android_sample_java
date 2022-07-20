package com.example.myapplication.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.myapplication.R;

public class Fragment1 extends Fragment implements View.OnClickListener {

    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item1, container, false);

        textView = view.findViewById(R.id.text1);

        if (getArguments() != null) {
            textView.setText(getArguments().getString("data"));
        }

        return view;
    }

    @Override
    public void onClick(View v) {

    }

    public void setData(String data){
        if (data != null) {
            textView.setText(data);
        }
    }
}
