package com.example.zyx.position;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zyx.network.NetworkSender;
import com.example.zyx.toolkit.DepthFromScrollEvent;
import com.example.zyx.toolkit.Orientation;

import org.w3c.dom.Text;

public class RotationActivity extends AppCompatActivity  implements Orientation.Listener, DepthFromScrollEvent.Listener, ActivityInterface{


    private TextView rotation_vector = null;

    private Button calibrate_btn = null;

    private TextView calibrate_label = null;

    private Orientation mOrientation = null;

    private NetworkSender networkSender = null;

    private DepthFromScrollEvent depthMonitor = null;

    private double depth = 0;

    private double calibrate_yaw = 0;

    private double calibrate_roll = 0;

    private double calibrate_pitch = 0;

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
        calibrate_btn = (Button)super.findViewById(R.id.button2);
        calibrate_label = (TextView)super.findViewById(R.id.textView3);
        calibrate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calibrate_pitch = mOrientation.GetPitch();
                calibrate_roll = mOrientation.GetRoll();
                calibrate_yaw = mOrientation.GetYaw();
                String str = new java.text.DecimalFormat("#.00").format(calibrate_pitch) + "&" + new java.text.DecimalFormat("#.00").format(calibrate_roll) + "&" + new java.text.DecimalFormat("#.00").format(calibrate_yaw);
                calibrate_label.setText(str);
            }
        });
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
        String str = new java.text.DecimalFormat("#.00").format(pitch - calibrate_pitch) + "&" + new java.text.DecimalFormat("#.00").format(roll - calibrate_roll) + "&" + new java.text.DecimalFormat("#.00").format(yaw -calibrate_yaw) + "&"
                + new java.text.DecimalFormat("#.00").format(depth);
        networkSender.AddQuery("rotation", str);
        System.out.println("add query");

    }

    @Override
    public void onDepthChanged(double normalized_depth) {
        depth = normalized_depth;
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
        return 2;
    }
}
