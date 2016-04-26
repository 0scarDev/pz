package pz.functions;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.widget.Toast;

/**
 * Created by ayan7852 on 4/26/2016.
 */
public class BluetoothConnector {

    // Variable for Bluetooth
    private static boolean state = false;
    public Context context;

    public BluetoothConnector(Context context){
        this.context = context;
    }

    public boolean blueTooth() {
        BluetoothAdapter bluetooth = BluetoothAdapter.getDefaultAdapter();
        if (!bluetooth.isEnabled()) {
            Toast.makeText(context, "Bluetooth is disabled.", Toast.LENGTH_SHORT).show();
            state = true;
        } else if (bluetooth.isEnabled()) {
            String address = bluetooth.getAddress();
            String name = bluetooth.getName();
            Toast.makeText(context, name + " : " + address, Toast.LENGTH_SHORT).show();
            state = false;
        }
        return state;
    }
}
