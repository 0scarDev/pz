package com.pz.cse145.pz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class ControlPanel extends AppCompatActivity {

    // Transfer Variables
    public int connectType;
    public String deviceName;
    public String deviceAddress;

    TextView text_000, text_001, text_002;

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

        text_000 = (TextView) findViewById(R.id.text_00);
        text_001 = (TextView) findViewById(R.id.text_01);
        text_002 = (TextView) findViewById(R.id.text_02);

        if(connectType == 0){
            text_000.setText(getBaseContext().getString(R.string.element_000));
        } else {
            text_000.setText(getBaseContext().getString(R.string.element_003));
        }
        text_001.setText(deviceName);
        text_002.setText(deviceAddress);
    }

    // OnClick Buttons for the Control
    public void onButton_000(View v){
        Toast.makeText(getApplicationContext(), "Transmitting 0" , Toast.LENGTH_LONG).show();
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
