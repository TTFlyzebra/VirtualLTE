package com.flyzebra.virtuallte;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void goNetworkConfigure(View view) {
        startActivity(new Intent(this,NetworkActivity.class));
    }

    public void goSimConfigure(View view) {
        startActivity(new Intent(this,SimConfigureActivity.class));
    }

    public void goATCommandTest(View view) {
        startActivity(new Intent(this,ATComandActivity.class));
    }

    public void goByteStringTool(View view) {
        startActivity(new Intent(this,ByteStringActivity.class));
    }
}
