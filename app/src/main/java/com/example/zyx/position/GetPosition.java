package com.example.zyx.position;

import android.content.Intent;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorListener;
import android.util.Log;

import com.example.zyx.network.NetworkSender;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;


public class GetPosition extends AppCompatActivity{
    private TextView show = null;
    private TextView acc = null;
    private TextView mag = null;

    private LinearLayout linearLayout = null;

    private Timer timer = new Timer();



    final String tag = "IBMEyes";
    private PrintWriter os = null;
    private BufferedReader in = null;
    private Socket socket = null;
    private String content = "";
    private boolean status = false;

    public String url = "";
    public String rotationUrl = "";

    private NetworkSender networkSender = null;

    TimerTask task = new TimerTask() {

        @Override
        public void run() {
            status = true;
        }
    };
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Intent intent = GetPosition.this.getIntent();
        //Bundle bundle = intent.getExtras();
        //final String ip = bundle.getString("ip");
        //final int port = Integer.parseInt(bundle.getString("port"));
        //url = "http://" +  ip + ":"+ port + "?"
        //url = "http://192.168.0.105:8888?";
        //rotationUrl = "http://192.168.0.105:8888/rotation?";
        networkSender = new NetworkSender("166.111.83.209", 8888, this);
        networkSender.StartWorking();
        //touchscreen
        setContentView(R.layout.activity_get_position);
        show = (TextView)super.findViewById(R.id.show);
        acc = (TextView)super.findViewById(R.id.acc);
        mag = (TextView)super.findViewById(R.id.mag);


        linearLayout = (LinearLayout)super.findViewById(R.id.LinearLayout1);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int pointerCount = event.getPointerCount();
                if (pointerCount == 2) {
                    float x0 = event.getX(0);
                    float y0 = event.getY(0);
                    float x1 = event.getX(1);
                    float y1 = event.getY(1);
                    if(y0 > y1)
                    {
                        float t = x0;
                        x0 = x1;
                        x1 = t;
                        t = y0;
                        y0 = y1;
                        y1 = y0;
                    }
                    show.setText("left("+x0+","+y0+") right("+x1+","+y1+")");
                    double w = linearLayout.getWidth();
                    double h = linearLayout.getHeight();
                    //left
                    double ry = x0 / w;
                    ry = ry>0?ry:0;
                    ry = ry < 1?ry:1;
                    double rx2 = (y1 - h / 2) / (h /2);
                    rx2 = rx2 > 0?rx2:0;
                    rx2 = rx2 < 1?rx2:1;
                    double ry2 = x1 / w;
                    ry2 = ry2 > 0?ry2:0;
                    ry2 = ry2 < 1?ry2:1;
                    if( status == true)
                    {
                        status = false;
                        String str = new java.text.DecimalFormat("#.00").format(ry) + "&" + new java.text.DecimalFormat("#.00").format(rx2) + "&" + new java.text.DecimalFormat("#.00").format(ry2);
                        networkSender.AddQuery("position", str);
                    }
                    show.setText(ry + "," + rx2 + "," + ry2);

                    return true;
                }
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        System.out.println("down");
                        //show.setText("down:" + "(" + event.getX() + "," + event.getY() + ")");

                        if(os != null) {
                            //os.println("(" + event.getX() + "," + event.getY() + ")");
                            //os.flush();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        System.out.println("move");
                        show.setText("move:"+"("+event.getX() + "," + event.getY()+")");
                        double y1 = event.getY();
                        double x1 = event.getX();
                        double w = linearLayout.getWidth();
                        double h = linearLayout.getHeight();
                        double rx2 = (y1 - h / 2) / (h /2);
                        rx2 = rx2 > 0?rx2:0;
                        rx2 = rx2 < 1?rx2:1;
                        double ry2 = x1 / w;
                        ry2 = ry2 > 0?ry2:0;
                        ry2 = ry2 < 1?ry2:1;
                        if(status == true)
                        {
                            status = false;
                            String str = 0 + "&" +new java.text.DecimalFormat("#.00").format(rx2) + "&" + new java.text.DecimalFormat("#.00").format(ry2);
                            networkSender.AddQuery("position", str);
                            //os.flush();
                        }

                        break;
                    case MotionEvent.ACTION_UP:
                        System.out.println("up");
                        show.setText("up:"+"("+event.getX() + "," + event.getY()+")");
                        if(os != null) {
                            //os.println("(" + event.getX() + "," + event.getY() + ")");
                            //os.flush();
                        }
                        break;
                }

                return true;
            }
        });

            //mAttitudeIndicator = (AttitudeIndicator) findViewById(R.id.attitude_indicator);
            // = (TextView)findViewById(R.id.textView);
    }


}
