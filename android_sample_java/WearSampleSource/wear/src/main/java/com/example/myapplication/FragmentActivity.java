package com.example.myapplication;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.WorkerThread;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.example.myapplication.SensorPackage.sensorListener;
import com.example.myapplication.fragment.Fragment1;
import com.example.myapplication.fragment.Fragment2;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FragmentActivity extends WearableActivity implements View.OnClickListener,
        DataClient.OnDataChangedListener,
        MessageClient.OnMessageReceivedListener {

    Button btn_left;
    Button btn_right;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    Fragment1 fragment1;
    Fragment2 fragment2;

    SensorPackage sensorPackage = null;

    float heart_rate_data = 0;
    double[] location_data = new double[]{0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        btn_left = findViewById(R.id.btn_left);
        btn_right = findViewById(R.id.btn_right);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();

        fragmentManager = getFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.clay_container, fragment1).commit();

        sensorPackage = SensorPackage.getInstance(this);
        sensorPackage.setSensorListener(sensorListener);

        Wearable.getDataClient(this).addListener(this);
        Wearable.getMessageClient(this).addListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Wearable.getDataClient(this).addListener(this);
        Wearable.getMessageClient(this).addListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btn_left) {
            Fragment1();
        } else if (v == btn_right) {
            Fragment2();
        }
    }

    public void Fragment1() {
        Bundle bundle = new Bundle();
        bundle.putString("data", heart_rate_data + "");
        fragment1.setArguments(bundle);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.clay_container, fragment1);
        fragmentTransaction.commit();
    }

    public void Fragment2() {
        Bundle bundle = new Bundle();
        bundle.putString("data", "Latitude:  " + location_data[0] + "\nLongitude:  " + location_data[1]);
        fragment2.setArguments(bundle);

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.clay_container, fragment2);
        fragmentTransaction.commit();
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEventBuffer) {

    }

    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
        Toast.makeText(this, new String(messageEvent.getData()), Toast.LENGTH_SHORT).show();
        try {
            Log.d("!---", new String(messageEvent.getData(), "UTF-8"));

            new StartWearableActivityTask().execute(new String[]{"testtest"});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class StartWearableActivityTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                sendStartActivityMessage(node, strings);
            }
            return null;
        }
    }

    @WorkerThread
    private void sendStartActivityMessage(String node, String[] msg) {

        Task<Integer> sendMessageTask =
                Wearable.getMessageClient(this).sendMessage(node, "test", msg[0].getBytes());

        try {
            // Block on a task and get the result synchronously (because this is on a background
            // thread).
            Integer result = Tasks.await(sendMessageTask);
            Log.d("!---", "Message sent: " + result);

        } catch (ExecutionException exception) {
            Log.e("!---", "Task failed: " + exception);

        } catch (InterruptedException exception) {
            Log.e("!---", "Interrupt occurred: " + exception);
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
                Log.e("!---", "Node ID: " + node.getId());
            }

        } catch (ExecutionException exception) {
            Log.e("!---", "Task failed: " + exception);

        } catch (InterruptedException exception) {
            Log.e("!---", "Interrupt occurred: " + exception);
        }

        return results;
    }

    sensorListener sensorListener = new sensorListener() {
        @Override
        public void onHeartRateData(int data) {
            heart_rate_data = data;

            Log.i("!---1", data + "/");

            if (fragment1.isVisible()) {
                fragment1.setData(data + "");
            }
        }

        @Override
        public void onLocationData(double[] data) {
            location_data = data;

            Log.i("!---2", "Latitude:  " + data[0] + "\nLongitude:  " + data[1]);

            if (fragment2.isVisible()) {
                fragment2.setData("Latitude:  " + data[0] + "\nLongitude:  " + data[1]);
            }
        }
    };
}
