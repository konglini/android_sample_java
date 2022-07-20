package com.example.myapplication;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;
import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity implements DataClient.OnDataChangedListener, MessageClient.OnMessageReceivedListener {

    private static final String TAG = "!---";

    private static final String[] PERMISSIONS_LIST = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int REQCODE_REQUEST_PERMISSIONS = 1;

    SensorPackage sensorPackage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new StartWearableActivityTask().execute();
            }
        });

        initPermissions();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Wearable.getDataClient(this).addListener(this);
        Wearable.getMessageClient(this).addListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Wearable.getDataClient(this).removeListener(this);
        Wearable.getMessageClient(this).removeListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        sensorPackage.stopSensor();
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {

    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        Toast.makeText(this, new String(messageEvent.getData()), Toast.LENGTH_SHORT).show();
    }

    private class StartWearableActivityTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                sendStartActivityMessage(node);
            }
            return null;
        }
    }

    @WorkerThread
    private void sendStartActivityMessage(String node) {

        Task<Integer> sendMessageTask =
                Wearable.getMessageClient(this).sendMessage(node, "t", "test".getBytes());

        try {
            // Block on a task and get the result synchronously (because this is on a background
            // thread).
            Integer result = Tasks.await(sendMessageTask);
            Log.d(TAG, "Message sent: " + result);

        } catch (ExecutionException exception) {
            Log.e(TAG, "Task failed: " + exception);

        } catch (InterruptedException exception) {
            Log.e(TAG, "Interrupt occurred: " + exception);
        }
    }

    @WorkerThread
    private Collection<String> getNodes() {
        HashSet<String> results = new HashSet<>();

        Task<List<Node>> nodeListTask =
                Wearable.getNodeClient(getApplicationContext()).getConnectedNodes();

        try {
            // Block on a task and get the result synchronously (because this is on a background
            // thread).
            List<Node> nodes = Tasks.await(nodeListTask);

            for (Node node : nodes) {
                results.add(node.getId());
                Log.e(TAG, "Node ID: " + node.getId());
            }

        } catch (ExecutionException exception) {
            Log.e(TAG, "Task failed: " + exception);

        } catch (InterruptedException exception) {
            Log.e(TAG, "Interrupt occurred: " + exception);
        }

        return results;
    }

    private void initPermissions() {
        //설명_ 권한 체크
        boolean checkPermissionsEnabled = false;
        ArrayList<String> permissions = new ArrayList<>();
        for (int i = 0; i < PERMISSIONS_LIST.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, PERMISSIONS_LIST[i]) != PackageManager.PERMISSION_GRANTED) {
                checkPermissionsEnabled = true;
                permissions.add(PERMISSIONS_LIST[i]);
            }
        }
        if (checkPermissionsEnabled) {
            String[] str_permissions = new String[permissions.size()];
            permissions.toArray(str_permissions);
            ActivityCompat.requestPermissions(this, str_permissions, REQCODE_REQUEST_PERMISSIONS);
        } else {
            sensorPackage = SensorPackage.getInstance(this);
            sensorPackage.setListener(listener);
            sensorPackage.startSensor();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQCODE_REQUEST_PERMISSIONS: {
                boolean isAllEnabled = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        isAllEnabled = false;
                    }
                }
                if (!isAllEnabled) {
                    initPermissions();
                } else {
                    sensorPackage = SensorPackage.getInstance(this);
                    sensorPackage.setListener(listener);
                    sensorPackage.startSensor();
                }
                break;
            }
        }
    }

    SensorPackage.Listener listener = new SensorPackage.Listener() {
        @Override
        public void onLocationData(String info, double[] data) {
            Log.i("!---2", info + "/" + data[0] + "/" + data[1] + "/" + data[2]);
        }
    };
}
