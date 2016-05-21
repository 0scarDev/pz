package com.pz.cse145.pz;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

import pz.functions.PZLibrary;

public class ControlPanel extends AppCompatActivity {

    // Transfer Variables
    public int connectType;
    public int speedNum = 0;
    public String deviceName;
    public String deviceAddress;

    TextView text_000, text_001, text_002;
    PZLibrary library;
    public BluetoothAdapter btAdapter = null;
    public BluetoothSocket btSocket = null;
    public OutputStream outStream = null;

    public UUID connectUUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_panel);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            connectType = extras.getInt("connectType");
            deviceName = extras.getString("deviceName");
            deviceAddress = extras.getString("deviceAddress");
        }

        library = new PZLibrary();

        text_000 = (TextView) findViewById(R.id.text_00);
        text_001 = (TextView) findViewById(R.id.text_01);
        text_002 = (TextView) findViewById(R.id.text_02);

        // Bluetooth Connection
        if(connectType == 0){
            text_000.setText(getBaseContext().getString(R.string.element_000));
            library.pzDeviceExtract(deviceName, deviceAddress, connectUUID);

            btAdapter = BluetoothAdapter.getDefaultAdapter();
            checkBTState();

        } else {
            text_000.setText(getBaseContext().getString(R.string.element_003));
        }
        text_001.setText(deviceName);
        text_002.setText(deviceAddress);
    }

    // OnClick Buttons for the Control
    public void onButton_Disconnect(View v){
        Toast.makeText(getApplicationContext(), "Disconnected" , Toast.LENGTH_LONG).show();
        finish();
    }
    public void onButton_000(View v){
        Toast.makeText(getApplicationContext(), "Transmitting 0" , Toast.LENGTH_LONG).show();
        speedNum = 130;
        sendData("SERV" + speedNum);
    }
    public void onButton_001(View v){
        Toast.makeText(getApplicationContext(), "Transmitting 1" , Toast.LENGTH_LONG).show();
    }
    public void onButton_002(View v){
        Toast.makeText(getApplicationContext(), "Transmitting 2" , Toast.LENGTH_LONG).show();
    }
    public void onButton_003(View v){
        Toast.makeText(getApplicationContext(), "Transmitting 3" , Toast.LENGTH_LONG).show();
    }
    public void onButton_004(View v){
        Toast.makeText(getApplicationContext(), "Transmitting 4" , Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_control_panel, menu);
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();

        // Set up a pointer to the remote node using it's address.
        BluetoothDevice device = btAdapter.getRemoteDevice(deviceAddress);

        // Two things are needed to make a connection:
        //   A MAC address, which we got above.
        //   A Service ID or UUID.  In this case we are using the
        //     UUID for SPP.
        try {
            UUID uuid = UUID.fromString("D031E051-158B-F550-BC0D-253B59224BD1");
            btSocket = device.createRfcommSocketToServiceRecord(uuid);
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and socket create failed: " + e.getMessage() + ".");
        }

        // Discovery is resource intensive.  Make sure it isn't going on
        // when you attempt to connect and pass your message.
        btAdapter.cancelDiscovery();

        // Establish the connection.  This will block until it connects.
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
            } catch (IOException e2) {
                errorExit("Fatal Error", "In onResume() and unable to close socket during connection failure" + e2.getMessage() + ".");
            }
        }

        try {
            outStream = btSocket.getOutputStream();
        } catch (IOException e) {
            errorExit("Fatal Error", "In onResume() and output stream creation failed:" + e.getMessage() + ".");
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (outStream != null) {
            try {
                outStream.flush();
            } catch (IOException e) {
                errorExit("Fatal Error", "In onPause() and failed to flush output stream: " + e.getMessage() + ".");
            }
        }

        try     {
            btSocket.close();
        } catch (IOException e2) {
            errorExit("Fatal Error", "In onPause() and failed to close socket." + e2.getMessage() + ".");
        }
    }

    private void checkBTState() {
        // Emulator doesn't support Bluetooth and will return null
        if(btAdapter==null) {
            errorExit("Fatal Error", "Bluetooth Not supported. Aborting.");
        } else {
            if (btAdapter.isEnabled()) {
            } else {
                //Prompt user to turn on Bluetooth
                Intent enableBtIntent = new Intent(btAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
    }

    private void errorExit(String title, String message){
        Toast msg = Toast.makeText(getBaseContext(),
                title + " - " + message, Toast.LENGTH_SHORT);
        msg.show();
        finish();
    }

    private void sendData(String message) {
        byte[] msgBuffer = message.getBytes();

        try {
            outStream.write(msgBuffer);
        } catch (IOException e) {
            String msg = "In onResume() and an exception occurred during write: " + e.getMessage();
            if (deviceAddress != null) {
                msg = msg + ".\n\nUpdate your server address from 00:00:00:00:00:00 to the correct address on line 37 in the java code";
            }
            msg = msg +  ".\n\nCheck that the SPP UUID: " + connectUUID.toString() + " exists on server.\n\n";

            errorExit("Fatal Error", msg);
        }
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
