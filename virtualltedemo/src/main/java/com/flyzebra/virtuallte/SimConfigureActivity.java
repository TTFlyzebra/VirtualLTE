package com.flyzebra.virtuallte;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SimConfigureActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private CheckBox check_4g;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simconfigure);
        check_4g = findViewById(R.id.check_4g);
        check_4g.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        check_4g.setEnabled(false);
        check_4g.setChecked(SystemPropTools.get("persist.sys.vlte.switch","close").endsWith("open"));
        check_4g.setEnabled(true);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        SystemPropTools.set("persist.sys.vlte.switch",isChecked?"open":"close");
    }
}
