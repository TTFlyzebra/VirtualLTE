package com.flyzebra.virtuallte;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.zebra.VirtualLTEManager;
import android.zebra.VlteLanInfo;

public class MainActivity extends AppCompatActivity {
    private Switch vlte_switch;
    private VirtualLTEManager virtualLTEManager;
    private EditText wifissid, wifipsk, ipAddress, ipMask, gateway, dns1, dns2;
    private CheckBox network;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        virtualLTEManager = (VirtualLTEManager) getSystemService("virtuallte");
        vlte_switch = findViewById(R.id.vlte_switch);
        wifissid = findViewById(R.id.wifissid);
        wifipsk = findViewById(R.id.wifipsk);
        ipAddress = findViewById(R.id.ipAddress);
        ipMask = findViewById(R.id.ipMask);
        gateway = findViewById(R.id.gateway);
        dns1 = findViewById(R.id.dns1);
        dns2 = findViewById(R.id.dns2);
        network = findViewById(R.id.network);

        vlte_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    VlteLanInfo vlteLanInfo = new VlteLanInfo();
                    vlteLanInfo.wifissid = wifissid.getText().toString();
                    vlteLanInfo.wifipsk = wifipsk.getText().toString();
                    vlteLanInfo.ipAddress = ipAddress.getText().toString();
                    vlteLanInfo.ipMask = ipMask.getText().toString();
                    vlteLanInfo.gateway = gateway.getText().toString();
                    vlteLanInfo.dns1 = dns1.getText().toString();
                    vlteLanInfo.dns2 = dns2.getText().toString();
                    vlteLanInfo.network = network.isChecked()?1:0;
                    virtualLTEManager.setVirtualLTEInfo(vlteLanInfo);
                    virtualLTEManager.openVirtualLTE();
                } else {
                    virtualLTEManager.closeVirtualLTE();
                }
            }
        });
    }
}
