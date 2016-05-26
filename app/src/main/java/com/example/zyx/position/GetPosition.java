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
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;


public class GetPosition extends AppCompatActivity implements Orientation.Listener{
    private TextView show = null;
    private TextView acc = null;
    private TextView mag = null;
    private TextView rotation_vector = null;
    private LinearLayout linearLayout = null;

    private Timer timer = new Timer();

    private Orientation mOrientation;

    final String tag = "IBMEyes";
    private PrintWriter os = null;
    private BufferedReader in = null;
    private Socket socket = null;
    private String content = "";
    private boolean status = false;
    public NetworkHelper helper = new NetworkHelper();
    public ArrayList<String> requests = new ArrayList<String>();

    public String url = "";
    public String rotationUrl = "";

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
        url = "http://192.168.0.105:8888?";
        rotationUrl = "http://192.168.0.105:8888/rotation?";
        //network
            /*
        new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    socket = new Socket(ip, port);
                    os = new PrintWriter(socket.getOutputStream());
                    in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                new Thread(networkTask).start();
            }
        }).start();
        */


        // sensors
        timer.schedule(task, 0, 20);
        Runnable connectRunnable = new Runnable() {
            public void run()
            {
                while(true){
                    if(requests.size() > 0) {
                        String r = requests.get(requests.size() - 1);
                        requests.clear();

                        helper.syncGetAllAlbums(r);
                    }
                    else
                    {
                        try {
                            //Thread.sleep(10);
                        }catch (Exception e)
                        {
                            //Log.d(e.getMessage());
                        }
                    }
                }
            }
        };
        //handler.post(connectRunnable);
        new Thread(connectRunnable).start();
        //touchscreen
        setContentView(R.layout.activity_get_position);


        show = (TextView)super.findViewById(R.id.show);
        acc = (TextView)super.findViewById(R.id.acc);
        mag = (TextView)super.findViewById(R.id.mag);
        rotation_vector = (TextView)super.findViewById(R.id.rv);

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
                        requests.add(url+str);
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
                            requests.add(url + str);
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
            mOrientation = new Orientation(this);
            //mAttitudeIndicator = (AttitudeIndicator) findViewById(R.id.attitude_indicator);
            // = (TextView)findViewById(R.id.textView);
    }

    Runnable networkTask = new Runnable() {

        @Override
        public void run() {
           try{
               while (true){
                   if(socket.isConnected()){
                       if(!socket.isOutputShutdown()){
                           if((content = in.readLine()) != null){
                               content += "\n";
                               handler.sendMessage(handler.obtainMessage());
                           }
                       }
                   }
               }
           }
           catch (Exception e)
           {
               Log.i("mylog", "haha");
           }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i("mylog", "请求结果为-->" + val);
            // TODO
            // UI界面的更新等相关操作
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        mOrientation.startListening(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mOrientation.stopListening();
    }


    @Override
    public void onOrientationChanged(float pitch, float roll, float yaw) {
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
        rotation_vector.setText(df.format(pitch) + ":" + df.format(roll) + ":" + df.format(yaw));
        String str = new java.text.DecimalFormat("#.00").format(pitch) + "&" + new java.text.DecimalFormat("#.00").format(roll) + "&" + new java.text.DecimalFormat("#.00").format(yaw);
        requests.add(rotationUrl+str);
    }
}
