package com.example.myapplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.fragment.Fragment1;
import com.example.myapplication.recycler.SimpleTextAdapter;

import java.util.ArrayList;

public class RecyclerActivity extends WearableActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    Button btn_left;
    Button btn_right;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Fragment1 fragment1;

    int item = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        // 리사이클러뷰에 표시할 데이터 리스트 생성.
        ArrayList<String> list = new ArrayList<>();
        for (int i = 0; i <= item; i++) {
            list.add(String.valueOf(i));
        }

        // 리사이클러뷰에 LinearLayoutManager 객체 지정.
        recyclerView = findViewById(R.id.recycler_container);

        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                return true;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
        // 리사이클러뷰에 SimpleTextAdapter 객체 지정.
        SimpleTextAdapter adapter = new SimpleTextAdapter(list);
        recyclerView.setAdapter(adapter);

        recyclerView.scrollToPosition(item);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_left) {
            item--;
            if (item < 0) {
                item = 1;
            }
            recyclerView.scrollToPosition(item);
        } else if (v == btn_right) {
            item++;
            if (item > 1) {
                item = 0;
            }
            recyclerView.scrollToPosition(item);
        }
    }

    public void Fragment1() {
        fragment1 = new Fragment1();
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.recycler_container, fragment1);
        fragmentTransaction.commit();
    }
}
