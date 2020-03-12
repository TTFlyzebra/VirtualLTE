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
import android.zebra.VlteConfigure;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, VirtualLTEManager.VirtualLTEListener {
    private Switch vlte_switch;
    private VirtualLTEManager virtualLTEManager;
    private EditText wifissid, wifipsk, ipAddress, ipMask, gateway, dns1, dns2;
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
        infoText = findViewById(R.id.infotext);

        refreshView();

        virtualLTEManager.addVirtualLTEListener(this);
        vlte_switch.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onDestroy() {
        virtualLTEManager.removeVirtualLTEListener(this);
        vlte_switch.setOnCheckedChangeListener(null);
        super.onDestroy();
    }

    private void refreshView() {
        wifissid.setText(SystemPropTools.get("persist.sys.vlte.ssid", "TEST"));
        wifipsk.setText(SystemPropTools.get("persist.sys.vlte.psk", "TEST"));
        if (virtualLTEManager.getVlteStatus() == VirtualLTEManager.CONNECT) {
            vlte_switch.setEnabled(false);
            vlte_switch.setChecked(true);
            vlte_switch.setEnabled(true);
        } else {
            vlte_switch.setEnabled(false);
            vlte_switch.setChecked(false);
            vlte_switch.setEnabled(true);
        }
        if (virtualLTEManager.getVlteStatus() == VirtualLTEManager.CONNECT) {
            DhcpResult dhcpResult = virtualLTEManager.getDhcpResult();
            String message = String.format("状态：连接成功！\nIP：%s\nMASK：%s\nGATEWAY：%s\nDNS1：%s\nDNS2：%s",
                    dhcpResult.ipaddress, dhcpResult.mask, dhcpResult.gateway, dhcpResult.dns1, dhcpResult.dns2);
            infoText.setText(message);
        } else {
            infoText.setText("状态：未连接.");
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked && virtualLTEManager.getVlteStatus() != VirtualLTEManager.CONNECT) {
            VlteConfigure vlteLanInfo = new VlteConfigure();
            vlteLanInfo.wifissid = wifissid.getText().toString();
            vlteLanInfo.wifipsk = wifipsk.getText().toString();
            vlteLanInfo.ipAddress = ipAddress.getText().toString();
            vlteLanInfo.ipMask = ipMask.getText().toString();
            vlteLanInfo.gateway = gateway.getText().toString();
            vlteLanInfo.dns1 = dns1.getText().toString();
            vlteLanInfo.dns2 = dns2.getText().toString();
            virtualLTEManager.configureVLTE(vlteLanInfo);
            virtualLTEManager.openVirtualLTE();
            infoText.setText("状态：正在连接......");
        } else {
            infoText.setText("状态：未连接！");
            virtualLTEManager.closeVirtualLTE();
        }
    }

    @Override
    public void notifyVlteStatus(int status) {
        refreshView();
    }

    @Override
    public void notifySignal(int signal) {

    }

    @Override
    public void recvMessage(String message) {
        String str = infoText.getText() + "\n" + message;
        infoText.setText(str);
    }
}
