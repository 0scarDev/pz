package com.pz.cse145.pz;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainScreen extends Activity {

    // Variables for Authentication
    public String fileName = "authentication.pz";

    public Typeface textFont;
    public String fontPath = "fonts/Rubik-Medium.ttf";

    TextView text_00, text_01, text_02;
    Button button_00, button_01, button_02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Set-up Font
        textFont = Typeface.createFromAsset(getAssets(), fontPath);

        text_00 = (TextView)findViewById(R.id.main_title);
        text_00.setTypeface(textFont);
        text_01 = (TextView)findViewById(R.id.auth_prompt);
        text_01.setTypeface(textFont);
        button_00 = (Button)findViewById(R.id.button_authen);
        button_00.setTypeface(textFont);
        button_01 = (Button)findViewById(R.id.button_qr);
        button_01.setTypeface(textFont);

        // Verify whether if there is an authentication file (saves settings)


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_screen, menu);
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

    // Initialize Authentication File, if the device is new
    public void onAuthenticate(View v){

        // Serial Device Test Message
        Toast.makeText(getApplicationContext(), "Serial Number:\n" + Build.SERIAL, Toast.LENGTH_SHORT).show();
    }

    // Initialize QR Scanning
    public void onScanQR(View v){

        // Test Message
        Toast.makeText(getApplicationContext(), "Under Construction", Toast.LENGTH_SHORT).show();
    }
}
