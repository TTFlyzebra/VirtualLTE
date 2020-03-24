package com.flyzebra.virtuallte;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.flyzebra.utils.ByteTools;

import java.nio.charset.StandardCharsets;

public class ByteStringActivity extends AppCompatActivity {
    private EditText editText;
    private TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_bytestring);
        editText = findViewById(R.id.edit01);
        textView = findViewById(R.id.text01);
        editText.setText("中国电信");
    }

    public void byte2string(View view) {
        try{
            byte[] bytes = ByteTools.hexString2Bytes(editText.getText()+"");
            String str = new String(bytes, StandardCharsets.UTF_8);
            textView.setText(str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void string2byte(View view) {
        try{
            byte[] bytes = editText.getText().toString().getBytes();
            String str = ByteTools.bytes2HexString(bytes);
            textView.setText(str);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
