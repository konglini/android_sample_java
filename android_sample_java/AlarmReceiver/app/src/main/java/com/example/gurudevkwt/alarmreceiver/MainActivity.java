package com.example.gurudevkwt.alarmreceiver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText edt_1;
    EditText edt_2;
    Button btn_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edt_1 = (EditText) findViewById(R.id.edt_1);
        edt_2 = (EditText) findViewById(R.id.edt_2);
        btn_1 = (Button) findViewById(R.id.btn_1);
        btn_1.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, subActivity.class);
        intent.putExtra("wake_time", edt_1.getText().toString());
        intent.putExtra("sleep_time", edt_2.getText().toString());
        startActivity(intent);
    }
}
