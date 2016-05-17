package com.pz.cse145.pz;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
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
    String nameHolder;
    String addressHolder;

    ListView displayDevices;

    PopupWindow connectionPopup;
    TextView text_00;

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

        // Clear any NULL variables
        /*for(int position = 0; position < deviceName.size(); position++){
            if(deviceName.get(position).equals("")){
                deviceName.set(position, this.getString(R.string.bluetooth_004));
            }
        }*/

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
                BluetoothInfo baseInfo = deviceListing.get(position);
                nameHolder = baseInfo.deviceName;
                addressHolder = baseInfo.deviceAddress;
                Toast.makeText(getApplicationContext(), "Device Name: " + nameHolder
                        + "\nDevice Address: " + addressHolder, Toast.LENGTH_LONG).show();
                popUpConnection();
            }
        });
    }

    // Pop-up for Connection
    public void popUpConnection(){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        int screenHeight = metrics.heightPixels;
        int screenWidth = metrics.widthPixels;

        LayoutInflater inflater = (LayoutInflater) BluetoothScanner.this.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.popup_connector, (ViewGroup) findViewById(R.id.popup_connector));
        connectionPopup = new PopupWindow(layout, screenWidth, screenHeight, true);
        connectionPopup.showAtLocation(layout, Gravity.CENTER, 0, 0);
        text_00 = (TextView) connectionPopup.getContentView().findViewById(R.id.text_00);
        text_00.setText(this.getString(R.string.connect_003) + " "
                + nameHolder + getBaseContext().getString(R.string.connect_004));
    }
    public void onConnectClose(View v){
        connectionPopup.dismiss();
    }
    public void onConnectProceed(View v){
        connectionPopup.dismiss();
        // Go to connection page
        Intent intent = new Intent(this, ControlPanel.class);
        intent.putExtra("deviceName", nameHolder);
        intent.putExtra("deviceAddress", addressHolder);
        intent.putExtra("connectType", 0);
        startActivity(intent);
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
