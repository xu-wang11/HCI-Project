package com.example.zyx.position;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;


import com.example.zyx.network.NetworkSender;
import com.example.zyx.toolkit.DepthFromScrollEvent;

public class JustDepth extends AppCompatActivity implements DepthFromScrollEvent.Listener, ActivityInterface{

    private NetworkSender networkSender = null;

    private DepthFromScrollEvent depthMonitor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_just_depth);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = JustDepth.this.getIntent();
        Bundle bundle = intent.getExtras();
        String ip = bundle.getString("ip");
        int port = Integer.parseInt(bundle.getString("port"));
        RelativeLayout relativeLayout = (RelativeLayout)super.findViewById(R.id.justdepthlayout);
        depthMonitor = new DepthFromScrollEvent(relativeLayout);
        networkSender = new NetworkSender(ip, port, this);
        networkSender.StartWorking();
    }

    @Override
    public void onDepthChanged(double normalized_depth) {
        //depth = normalized_depth;
        networkSender.AddQuery("depth", new java.text.DecimalFormat("#.00").format(normalized_depth));
    }

    @Override
    protected void onStart() {
        super.onStart();
        depthMonitor.startListening(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        depthMonitor.stopListening();
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
        return 3;
    }

}
