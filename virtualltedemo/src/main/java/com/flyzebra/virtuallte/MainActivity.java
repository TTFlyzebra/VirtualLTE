package com.flyzebra.virtuallte;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.zebra.VirtualLTEManager;

public class MainActivity extends AppCompatActivity {
    private Switch vlte_switch;
    private VirtualLTEManager virtualLTEManager;
    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        virtualLTEManager = (VirtualLTEManager) getSystemService("virtuallte");
        vlte_switch = findViewById(R.id.vlte_switch);
        vlte_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    virtualLTEManager.openVirtualLTE();
                }else{
                    virtualLTEManager.closeVirtualLTE();
                }
            }
        });
    }
}
