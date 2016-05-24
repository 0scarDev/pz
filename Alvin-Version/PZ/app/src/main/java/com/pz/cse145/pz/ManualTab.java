package com.pz.cse145.pz;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pz.functions.BluetoothConnector;

public class ManualTab extends Fragment implements View.OnClickListener {

    private static final int REQUEST_ENABLE_BT = 12;
    private BluetoothAdapter adapter;
    BluetoothConnector connector;

    private BroadcastReceiver recieverScan;

    private SeekBar speedControl = null;
    ArrayList<String> deviceName;
    ArrayList<String> deviceAddress;

    public ManualTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        connector = new BluetoothConnector(getContext());
        deviceName = new ArrayList<String>();
        deviceAddress = new ArrayList<String>();

        View view = inflater.inflate(R.layout.tab_manual, container, false);

        Button button_00 = (Button) view.findViewById(R.id.button_clock);
        button_00.setOnClickListener(this);
        Button button_01 = (Button) view.findViewById(R.id.button_counter);
        button_01.setOnClickListener(this);
        Button button_02 = (Button) view.findViewById(R.id.button_scan);
        button_02.setOnClickListener(this);
        Button button_03 = (Button) view.findViewById(R.id.wifi_scan);
        button_03.setOnClickListener(this);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_clock:
                Toast.makeText(getContext(), "Clock-Wise", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_counter:
                Toast.makeText(getContext(), "Counter Clock", Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_scan:
                Intent intent = new Intent(getContext(), DeviceScanActivity.class);
                startActivity(intent);
                break;
            case R.id.wifi_scan:
                Toast.makeText(getContext(), "Under Construction", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        setBluetoothData();
    }

    private void setBluetoothData() {
        // Getting the Bluetooth adapter
        adapter = BluetoothAdapter.getDefaultAdapter();
        // Emulator doesn't support Bluetooth and will return null
        if (adapter == null) {
            Toast.makeText(getContext(), "Bluetooth NOT supported. Aborting.",
                    Toast.LENGTH_LONG).show();
        }
        // Starting the device discovery
        adapter.startDiscovery();

        // Scan for nearby devices
        recieverScan = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                //Finding devices
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    deviceName.add(device.getName());
                    deviceAddress.add(device.getAddress());
                }
            }
        };

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getContext().registerReceiver(recieverScan, filter);

        // Listing paired devices
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            Toast.makeText(getContext(), "Found device: " + device.getName() + " Add: "
                    + device.getAddress(), Toast.LENGTH_LONG).show();
        }
    }

}