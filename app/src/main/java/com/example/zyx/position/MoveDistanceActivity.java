package com.example.zyx.position;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.zyx.network.NetworkSender;

public class MoveDistanceActivity extends AppCompatActivity implements  ActivityInterface{

    private NetworkSender networkSender = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_move_distance);

        Intent intent = MoveDistanceActivity.this.getIntent();
        Bundle bundle = intent.getExtras();
        String ip = bundle.getString("ip");
        int port = Integer.parseInt(bundle.getString("port"));

        networkSender = new NetworkSender(ip, port, this);
        networkSender.StartWorking();

        findViewById(R.id.movedistance).setOnTouchListener(new View.OnTouchListener() {

            private float x;

            private float y;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    x = event.getX();
                    y = event.getY();
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    float nx = event.getX();
                    float ny = event.getY();
                    networkSender.AddQuery("movedistance", Math.abs(ny - y) / findViewById(R.id.movedistance).getHeight() + "");
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
        return 6;
    }
}
