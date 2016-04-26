package com.pz.cse145.pz;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.Set;

import pz.functions.BluetoothConnector;

public class ManualTab extends Fragment implements View.OnClickListener {

    private static final int REQUEST_ENABLE_BT = 12;
    private BluetoothAdapter adapter;
    BluetoothConnector connector;

    private SeekBar speedControl = null;

    public ManualTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        connector = new BluetoothConnector(getContext());

        // Seek Bar for Speed Control
        /*speedControl = (SeekBar) getView().findViewById(R.id.seek1);
        speedControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                progressChanged = progress;
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                Toast.makeText(getContext(), "Speed: " + progressChanged,
                        Toast.LENGTH_SHORT).show();
            }
        });*/

        View view = inflater.inflate(R.layout.tab_manual,
                container, false);
        Button button_00 = (Button) view.findViewById(R.id.button_clock);
        button_00.setOnClickListener(this);
        Button button_01 = (Button) view.findViewById(R.id.button_counter);
        button_01.setOnClickListener(this);
        Button button_02 = (Button) view.findViewById(R.id.button_scan);
        button_02.setOnClickListener(this);

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
                setBluetoothData();
                if (connector.blueTooth()) {
                    Intent enableBtIntent = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                }
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