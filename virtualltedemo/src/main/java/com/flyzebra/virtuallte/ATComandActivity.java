package com.flyzebra.virtuallte;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.zebra.VirtualLTEManager;

import androidx.appcompat.app.AppCompatActivity;

public class ATComandActivity extends AppCompatActivity {
    private VirtualLTEManager virtualLTEManager;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atcomand);
        virtualLTEManager = (VirtualLTEManager) getSystemService("virtuallte");
    }


}
