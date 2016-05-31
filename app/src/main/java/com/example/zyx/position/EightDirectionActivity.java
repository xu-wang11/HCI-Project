package com.example.zyx.position;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.zyx.network.NetworkSender;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class EightDirectionActivity extends AppCompatActivity implements View.OnClickListener, ActivityInterface {

    private NetworkSender networkSender = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = EightDirectionActivity.this.getIntent();
        Bundle bundle = intent.getExtras();
        final String ip = bundle.getString("ip");
        final int port = Integer.parseInt(bundle.getString("port"));
        setContentView(R.layout.activity_eight_direction);
        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);
        findViewById(R.id.btn8).setOnClickListener(this);
        findViewById(R.id.btn9).setOnClickListener(this);
        //findViewById(R.id.btn1).setOnClickListener(this);
        networkSender = new NetworkSender(ip, port, this);
        networkSender.StartWorking();
    }

    @Override
    public void onClick(View view) {
        int val = 0;
        int[] ids = {R.id.btn1, R.id.btn2, R.id.btn3,
                R.id.btn4, R.id.btn6,
                R.id.btn7, R.id.btn8, R.id.btn9};
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] == view.getId()) {
                val = i;
                break;
            }
        }
        networkSender.AddQuery("eightdirection", val + "");
    }

    @Override
    public void ModeChange(int mode) {
        Intent intent = new Intent();
        intent.putExtra("mode", mode);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public int GetMode() {
        return 5;
    }
}
