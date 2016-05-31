package com.example.zyx.position;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;

import com.example.zyx.network.NetworkSender;

public class UpDownActivity extends AppCompatActivity implements  ActivityInterface{

    private NetworkSender networkSender = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_down);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = UpDownActivity.this.getIntent();
        Bundle bundle = intent.getExtras();
        String ip = bundle.getString("ip");
        int port = Integer.parseInt(bundle.getString("port"));
        networkSender = new NetworkSender(ip, port, this);
        networkSender.StartWorking();

        findViewById(R.id.Up).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction()== MotionEvent.ACTION_DOWN)
                {
                    networkSender.AddQuery("updown", "0");
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    networkSender.AddQuery("updown", "2");
                }
                return true;
            }
        });

        findViewById(R.id.down).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    networkSender.AddQuery("updown", "1");
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    networkSender.AddQuery("updown", "2");
                }
                return true;
            }
        });


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
        return 4;
    }

}
