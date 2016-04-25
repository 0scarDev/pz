package com.pz.cse145.pz;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class ManualTab extends Fragment {

    private SeekBar speedControl = null;
    Context context;

    public ManualTab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

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
        button_00.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Clock-Wise", Toast.LENGTH_SHORT).show();
            }
        });
        Button button_01 = (Button) view.findViewById(R.id.button_counter);
        button_01.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Counter Clock", Toast.LENGTH_SHORT).show();
            }
        });

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab_manual, container, false);
    }
}