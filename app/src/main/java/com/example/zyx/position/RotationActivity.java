package com.example.zyx.position;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zyx.network.NetworkSender;
import com.example.zyx.toolkit.DepthFromScrollEvent;
import com.example.zyx.toolkit.Orientation;

public class RotationActivity extends AppCompatActivity  implements Orientation.Listener, DepthFromScrollEvent.Listener{

    private TextView rotation_vector = null;

    private Orientation mOrientation = null;

    private NetworkSender networkSender = null;

    private DepthFromScrollEvent depthMonitor = null;

    private double depth = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rotation);
        Intent intent = RotationActivity.this.getIntent();
        Bundle bundle = intent.getExtras();
        String ip = bundle.getString("ip");
        int port = Integer.parseInt(bundle.getString("port"));
        rotation_vector = (TextView)super.findViewById(R.id.rv);
        mOrientation = new Orientation(this);
        networkSender = new NetworkSender(ip, port, this);
        networkSender.StartWorking();
        RelativeLayout relativeLayout = (RelativeLayout)super.findViewById(R.id.rotaionview);
        depthMonitor = new DepthFromScrollEvent(relativeLayout);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mOrientation.startListening(this);
        depthMonitor.startListening(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mOrientation.stopListening();
        depthMonitor.stopListening();
    }


    @Override
    public void onOrientationChanged(float pitch, float roll, float yaw) {
        java.text.DecimalFormat   df   =new   java.text.DecimalFormat("#.00");
        rotation_vector.setText(df.format(pitch) + ":" + df.format(roll) + ":" + df.format(yaw));
        String str = new java.text.DecimalFormat("#.00").format(pitch) + "&" + new java.text.DecimalFormat("#.00").format(roll) + "&" + new java.text.DecimalFormat("#.00").format(yaw) + "&"
                + new java.text.DecimalFormat("#.00").format(depth);
        networkSender.AddQuery("rotation", str);
        System.out.println("add query");

    }

    @Override
    public void onDepthChanged(double normalized_depth) {
        depth = normalized_depth;
    }
}
