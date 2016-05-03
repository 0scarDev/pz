package com.pz.cse145.pz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import pz.functions.BluetoothInfo;
import pz.functions.BluetoothScanAdapter;

public class BluetoothScanner extends AppCompatActivity {

    // Variables
    ArrayList<String> deviceName;
    ArrayList<String> deviceAddress;
    ArrayList<BluetoothInfo> deviceListing;

    ListView displayDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_scanner);

        // Obtain results of the Bluetooth scanning, if there was any
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            deviceName = extras.getStringArrayList("deviceName");
            deviceAddress = extras.getStringArrayList("deviceAddress");
        } else {
            deviceName = new ArrayList<String>();
            deviceAddress = new ArrayList<String>();
        }

        deviceListing = new ArrayList<BluetoothInfo>();

        // Transfer data into the ArrayList<BluetoothInfo>
        for(int position = 0; position < deviceAddress.size(); position++){
            if(deviceListing.size() == 0) {
                deviceListing.add(new BluetoothInfo(deviceName.get(position), deviceAddress.get(position)));
            } else {
                // Prevents duplicate scans
                for (int positionX = 0; positionX < deviceListing.size(); positionX++) {
                    if (deviceAddress.get(positionX).equals(deviceAddress.get(position))) break;
                    if (positionX == (deviceListing.size() - 1)) {
                        deviceListing.add(new BluetoothInfo(deviceName.get(position), deviceAddress.get(position)));
                    }
                }
            }
        }

        displayDevices = (ListView) findViewById(R.id.blue_scan_list);
        BluetoothScanAdapter adapter = new BluetoothScanAdapter(this, R.layout.listview_bluetooth_scan, deviceListing);
        //View header = (View) getLayoutInflater().inflate(R.layout.listview_bluetooth_scan, null);
        //displayDevices.addHeaderView(header);
        displayDevices.setAdapter(adapter);

        /*displayDevices = (ListView) findViewById(R.id.blue_scan_list);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, deviceAddress);
        // Set The Adapter
        displayDevices.setAdapter(arrayAdapter);*/

        // register onClickListener to handle click events on each item
        displayDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {
                // CONNECTION HERE
                Toast.makeText(getApplicationContext(), "Device Name: " + deviceName.get(position)
                        + "\nIP Address: " + deviceAddress.get(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        deviceName = deviceAddress = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth_scanner, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
