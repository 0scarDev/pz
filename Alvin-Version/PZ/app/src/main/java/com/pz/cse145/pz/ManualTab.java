package com.pz.cse145.pz;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Set;

import pz.functions.BluetoothConnector;

public class ManualTab extends Fragment implements View.OnClickListener {

    private static final int REQUEST_ENABLE_BT = 12;
    private BluetoothAdapter adapter;
    BluetoothConnector connector;

    private BluetoothAdapter adapterScan;
    private BroadcastReceiver recieverScan;
    private ArrayAdapter arrayAdapter;
    private String[] arrayDevice = new String[] {};
    //ListView listView;

    private SeekBar speedControl = null;

    public ManualTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        connector = new BluetoothConnector(getContext());

        View view = inflater.inflate(R.layout.tab_manual, container, false);

        arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.tab_manual, arrayDevice);
        //listView = (ListView) view.findViewById(R.id.device_list);
        //listView.setAdapter(arrayAdapter);

        Button button_00 = (Button) view.findViewById(R.id.button_clock);
        button_00.setOnClickListener(this);
        Button button_01 = (Button) view.findViewById(R.id.button_counter);
        button_01.setOnClickListener(this);
        Button button_02 = (Button) view.findViewById(R.id.button_scan);
        button_02.setOnClickListener(this);
        Button button_03 = (Button) view.findViewById(R.id.button_switch);
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
            case R.id.button_switch:
                setBluetoothData();
                if (connector.blueTooth()) {
                    Intent enableBtIntent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
                break;
            case R.id.button_scan:
                adapterScan = BluetoothAdapter.getDefaultAdapter();
                if (adapter == null) {
                    Toast.makeText(getContext(), "Bluetooth NOT supported. Aborting.",
                            Toast.LENGTH_LONG).show();
                }
                adapterScan.startDiscovery();
                recieverScan = new BroadcastReceiver() {
                    public void onReceive(Context context, Intent intent) {
                        String action = intent.getAction();
                        //Finding devices
                        if (BluetoothDevice.ACTION_FOUND.equals(action))
                        {
                            // Get the BluetoothDevice object from the Intent
                            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                            // Add the name and address to an array adapter to show in a ListView
                            Toast.makeText(getContext(), device.getName() + "\n" + device.getAddress(), Toast.LENGTH_LONG).show();
                        }
                    }
                };

                IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                getContext().registerReceiver(recieverScan, filter);

                //listView.setAdapter(arrayAdapter);
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
        Toast.makeText(getContext(), "Adapter: " + adapter.toString() + "\n\nName: "
                + adapter.getName() + "\nAddress: " + adapter.getAddress(), Toast.LENGTH_LONG).show();
        // Check for Bluetooth support in the first place
        // Emulator doesn't support Bluetooth and will return null
        if (adapter == null) {
            Toast.makeText(getContext(), "Bluetooth NOT supported. Aborting.",
                    Toast.LENGTH_LONG).show();
        }
        // Starting the device discovery
        adapter.startDiscovery();
        // Listing paired devices
        Set<BluetoothDevice> devices = adapter.getBondedDevices();
        for (BluetoothDevice device : devices) {
            Toast.makeText(getContext(), "Found device: " + device.getName() + " Add: "
                    + device.getAddress(), Toast.LENGTH_LONG).show();
        }
    }

}