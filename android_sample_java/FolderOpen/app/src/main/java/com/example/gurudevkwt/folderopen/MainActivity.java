package com.example.gurudevkwt.folderopen;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.File;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/gurupet");
        if (!file.isDirectory()) {
            file.mkdirs();
        }

        new MediaScanner(this).startScan(new String[]{Environment.getExternalStorageDirectory().getAbsolutePath() + "/gurupet"});
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivity(intent);
    }
}
