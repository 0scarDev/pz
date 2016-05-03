package pz.functions;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pz.cse145.pz.R;

import java.util.ArrayList;

public class BluetoothScanAdapter extends ArrayAdapter<BluetoothInfo> {

    // Variables of the Adapter
    Context context;
    int layoutResourceId;
    ArrayList<BluetoothInfo> bluetoothInfos;

    public BluetoothScanAdapter(Context context, int layoutResourceId, ArrayList<BluetoothInfo> bluetoothInfos) {
        super(context, layoutResourceId, bluetoothInfos);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.bluetoothInfos = bluetoothInfos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        DeviceHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);

            holder = new DeviceHolder();
            holder.blueName = (TextView)row.findViewById(R.id.bluetooth_name);
            holder.blueAddress = (TextView)row.findViewById(R.id.bluetooth_address);
            row.setTag(holder);
        } else {
            holder = (DeviceHolder)row.getTag();
        }

        BluetoothInfo bluetoothInfo = bluetoothInfos.get(position);
        holder.blueName.setText(bluetoothInfo.deviceName);
        holder.blueAddress.setText(bluetoothInfo.deviceAddress);
        return row;
    }

    static class DeviceHolder {
        TextView blueName;
        TextView blueAddress;
    }
}
