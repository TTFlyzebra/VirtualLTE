package com.flyzebra.virtuallte;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.zebra.DhcpResult;
import android.zebra.VirtualLTEManager;
import android.zebra.VlteLanInfo;

public class MainActivity extends AppCompatActivity implements VirtualLTEManager.VirtualLTEListener {
    private Switch vlte_switch;
    private VirtualLTEManager virtualLTEManager;
    private EditText wifissid, wifipsk, ipAddress, ipMask, gateway, dns1, dns2;
    private CheckBox network;
    private TextView infoText;

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
        infoText = findViewById(R.id.infotext);

        upView();

        virtualLTEManager.addVirtualLTEListener(this);
        vlte_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && virtualLTEManager.getVlteStatus() != VirtualLTEManager.CONNECT) {
                    VlteLanInfo vlteLanInfo = new VlteLanInfo();
                    vlteLanInfo.wifissid = wifissid.getText().toString();
                    vlteLanInfo.wifipsk = wifipsk.getText().toString();
                    vlteLanInfo.ipAddress = ipAddress.getText().toString();
                    vlteLanInfo.ipMask = ipMask.getText().toString();
                    vlteLanInfo.gateway = gateway.getText().toString();
                    vlteLanInfo.dns1 = dns1.getText().toString();
                    vlteLanInfo.dns2 = dns2.getText().toString();
                    vlteLanInfo.network = network.isChecked() ? 1 : 0;
                    virtualLTEManager.setVirtualLTEInfo(vlteLanInfo);
                    virtualLTEManager.openVirtualLTE();
                    infoText.setText("状态：正在连接......");
                } else {
                    virtualLTEManager.closeVirtualLTE();
                }
            }
        });
    }

    private void upView() {
        if (virtualLTEManager.getVlteStatus() == VirtualLTEManager.CONNECT) {
            vlte_switch.setChecked(true);
        } else {
            vlte_switch.setChecked(false);
        }
        if (virtualLTEManager.getVlteStatus() == VirtualLTEManager.CONNECT) {
            DhcpResult dhcpResult = virtualLTEManager.getDhcpResult();
            String message = String.format("状态：连接成功！\nIP:%s\nMASK:%s\nGATEWAY:%s\nDNS1:%s\nDNS2:%s",
                    dhcpResult.ipaddress, dhcpResult.mask, dhcpResult.gateway, dhcpResult.dns1, dhcpResult.dns2);
            infoText.setText(message);
        } else {
            infoText.setText("状态：未连接！");
        }
    }

    @Override
    public void notifyVlteStatus(int status) {
        upView();
    }

    @Override
    public void notifySignal(int signal) {

    }
}
